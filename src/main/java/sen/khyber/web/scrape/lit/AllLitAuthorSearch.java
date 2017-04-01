package sen.khyber.web.scrape.lit;

import sen.khyber.web.Internet;
import sen.khyber.web.scrape.lit.Lit.Category;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import okhttp3.Request;
import okhttp3.Response;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class AllLitAuthorSearch {
    
    private static final Path UID_CACHE = Lit.DOWNLOAD_DIR.resolve("uids.txt");
    private static final Path AUTHOR_CACHE = Lit.DOWNLOAD_DIR.resolve("authors.txt");
    
    private static final int MIN_SKIP_LENGTH = 400;
    private static final long MIN_NUM_PAGES_SKIP_LENGTH = 0; // FIXME find out what it is
    
    private static final int MAX_DIGITS = 7; // FIXME check
    private static final int MAX_UID = (int) Math.pow(10, MAX_DIGITS) - 1;
    private static final int MAX_BITS = Integer.SIZE - Integer.numberOfLeadingZeros(MAX_UID);
    private static final int MAX_BYTES = (int) Math.ceil((double) MAX_BITS / Byte.SIZE);
    
    private final int[] uidsCounter = new int[MAX_UID];
    private final int[] uids = new int[MAX_UID * 2];
    private final int[] uidIndices = new int[MAX_UID];
    private int numUids;
    private boolean foundUids;
    
    private final List<String> failedUrls = new ArrayList<>();
    private final List<Category> failedCategories = new ArrayList<>();
    
    private LitAuthor[] authors;
    
    // singleton
    
    private AllLitAuthorSearch() {}
    
    private static final AllLitAuthorSearch INSTANCE = new AllLitAuthorSearch();
    
    public static AllLitAuthorSearch get() {
        return INSTANCE;
    }
    
    /**
     * finds all uids in this page matching the format "uid=[0-9]*&"
     * 
     * @param url url
     * @throws IOException IOException
     */
    private void findAllUidsInPage(final String url) throws IOException {
        final byte[] bytes = Internet.bytes(url);
        System.out.println("reading " + url);
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i++] == '?' && bytes[i++] == 'u') {
                i += 3;
                final byte first = bytes[i];
                if (first < '0' || first > '9') {
                    break;
                }
                int uid = 0;
                // read uid until reach '&'
                final int start = i;
                for (;; i++) {
                    final byte b = bytes[i];
                    if (b == '&') {
                        break;
                    }
                    uid = uid * 10 + b - '0';
                }
                uidsCounter[uid]++;
                // skip ahead a little to avoid checking other stuff
                i += MIN_SKIP_LENGTH - (i - start);
            }
        }
    }
    
    private int getNumPagesInCategory(final String baseUrl) throws IOException {
        final String url = baseUrl + "1-page";
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("Range", "bytes=0-" + MIN_NUM_PAGES_SKIP_LENGTH)
                .build();
        final Response response = Internet.client.newCall(request).execute();
        final byte[] bytes = response.body().bytes();
        int numPages = 0;
        int i = 0;
        try {
            while (bytes[i++] != 'Z' || bytes[i++] != '<') {}
        } catch (final IndexOutOfBoundsException e) {
            return 0;
        }
        i -= 9;
        final int end = i;
        while (bytes[--i] != '/') {}
        for (i++; i < end; i++) {
            numPages = numPages * 10 + bytes[i] - '0';
        }
        System.out.println("# pages: " + numPages);
        //        return IntStream.rangeClosed(1, numPages)
        //                .mapToObj(pageNum -> baseUrl + pageNum + "-page");
        return numPages;
    }
    
    private void tryFindUids(final Stream<String> urls,
            final List<String> failedUrls) {
        urls.parallel().forEach(url -> {
            try {
                findAllUidsInPage(url);
            } catch (final IOException e) {
                failedUrls.add(url);
                e.printStackTrace();
            }
        });
    }
    
    private static MappedByteBuffer malloc(final Path path, final long length) throws IOException {
        final RandomAccessFile file = new RandomAccessFile(path.toFile(), "rw");
        final FileChannel channel = file.getChannel();
        final MappedByteBuffer memory = channel.map(MapMode.READ_WRITE, 0, length);
        file.close();
        return memory;
    }
    
    private static MappedByteBuffer mallocWholeFile(final Path path) throws IOException {
        return malloc(path, path.toFile().length());
    }
    
    private void invertUidIndices() {
        for (int i = 0; i < numUids; i++) {
            uidIndices[uids[i << 1]] = i;
        }
    }
    
    public void serializeUids() throws IOException {
        final long start = System.nanoTime();
        
        int totalNumPages = 0;
        final Category[] categories = Category.values();
        final int[] numPagesInEachCategory = new int[categories.length];
        for (int i = 0; i < categories.length; i++) {
            final int numPages = getNumPagesInCategory(categories[i].getUrl());
            numPagesInEachCategory[i] = numPages;
            totalNumPages += numPages;
        }
        
        final String[] pageUrls = new String[totalNumPages];
        int j = 0;
        for (int i = 0; i < categories.length; i++) {
            final String baseUrl = categories[i].getUrl();
            final int numPages = numPagesInEachCategory[i];
            for (int pageNum = 1; pageNum <= numPages; pageNum++) {
                pageUrls[j++] = baseUrl + pageNum + "-page";
            }
        }
        
        final List<String> failedUrls = new ArrayList<>();
        tryFindUids(Stream.of(pageUrls), failedUrls);
        
        int iterNum = 0;
        while (failedUrls.size() > 0) {
            final String[] failedUrlsCopy = failedUrls.toArray(new String[failedUrls.size()]);
            failedUrls.clear();
            try {
                Thread.sleep((long) (iterNum++ * 1e3));
            } catch (final InterruptedException e1) {
                e1.printStackTrace();
            }
            tryFindUids(Stream.of(failedUrlsCopy), failedUrls);
        }
        
        final long time = System.nanoTime() - start;
        System.out.println(time / 1e9);
        foundUids = true;
        
        int i = 0;
        for (int uid = 0; uid < uidsCounter.length; uid++) {
            final int count = uidsCounter[uid];
            if (count != 0) {
                uids[i++] = uid;
                uids[i++] = count;
            }
        }
        numUids = i >>> 1;
        final int length = (i + 1) * Integer.BYTES;
        final IntBuffer out = malloc(UID_CACHE, length).asIntBuffer();
        out.put(numUids);
        out.put(uids, 0, i);
        invertUidIndices();
    }
    
    public void deserializeUids() throws IOException {
        final IntBuffer in = mallocWholeFile(UID_CACHE).asIntBuffer();
        numUids = in.get();
        final int length = numUids << 1;
        in.get(uids, 0, length);
        invertUidIndices();
    }
    
    private static void tryFindAuthor(final int uidNum, final int[] uids,
            final ByteBuffer[] authorBuffers, final AtomicInteger count) throws IOException {
        final int i = uidNum << 1;
        final int uid = uids[i];
        final int numStories = uids[i + 1];
        System.out.println("reading author " + uid + '\t' + count.incrementAndGet());
        final ByteBuffer authorBuffer = new LitAuthorSerializer(uid, numStories).serialize();
        authorBuffers[uidNum] = authorBuffer;
    }
    
    private static void tryFindAuthors(final IntStream uidNums, final int[] uids,
            final ByteBuffer[] authorBuffers, final AtomicInteger count,
            final List<Integer> failedUids) {
        uidNums.parallel()
                .forEach(uidNum -> {
                    try {
                        tryFindAuthor(uidNum, uids, authorBuffers, count);
                    } catch (final IOException e) {
                        failedUids.add(uidNum);
                        e.printStackTrace();
                    } catch (final ArrayIndexOutOfBoundsException e) {
                        final int i = uidNum << 1;
                        System.err.println("uid: " + uids[i] + ", numStories: " + uids[i + 1]);
                        //throw e;
                        e.printStackTrace();
                    }
                });
    }
    
    public void serializeAuthors() throws IOException {
        final AtomicInteger count = new AtomicInteger();
        final ByteBuffer[] authorBuffers = new ByteBuffer[numUids];
        final List<Integer> failedUids = new ArrayList<>();
        final long start = System.nanoTime();
        final int limit = 10000;
        tryFindAuthors(IntStream.range(0, numUids).limit(limit), uids, authorBuffers, count,
                failedUids);
        final long time = System.nanoTime() - start;
        System.out.println(time / limit / 1e9);
        while (failedUids.size() > 0) {
            final Integer[] failedUidsCopy = failedUids.toArray(new Integer[failedUids.size()]);
            failedUids.clear();
            tryFindAuthors(Stream.of(failedUidsCopy).mapToInt(Integer::intValue), uids,
                    authorBuffers, count, failedUids);
        }
        int length = numUids + 1 << 1;
        final int[] offsets = new int[numUids];
        for (int i = 0; i < numUids; i++) {
            offsets[i] = length;
            length += authorBuffers[i].capacity();
        }
        final ByteBuffer out = malloc(AUTHOR_CACHE, length);
        final IntBuffer intOut = out.asIntBuffer();
        intOut.put(numUids);
        intOut.put(offsets);
        for (final ByteBuffer buffer : authorBuffers) {
            out.put(buffer);
        }
        System.out.println("done");
        out.rewind();
        deserializeAuthors(out);
    }
    
    private void deserializeAuthors(final ByteBuffer in) {
        if (in.capacity() == 0) {
            authors = new LitAuthor[0];
            return;
        }
        final int numAuthors = in.getInt();
        in.position(in.position() + numAuthors * Integer.BYTES);
        authors = new LitAuthor[numAuthors];
        for (int i = 0; i < numAuthors; i++) {
            authors[i] = new LitAuthor(in);
        }
    }
    
    public void deserializeAuthors() throws IOException {
        deserializeAuthors(mallocWholeFile(AUTHOR_CACHE));
    }
    
    private LitAuthor deserializeAuthor(final ByteBuffer in, final int uid) {
        in.rewind();
        final int offsetOffset = (1 + uidIndices[uid]) * Integer.BYTES;
        final int offset = in.getInt(offsetOffset);
        in.position(offset);
        return new LitAuthor(in);
    }
    
    private static interface IORunnable {
        
        public void run() throws IOException;
        
    }
    
    private void findAll(final Path path, final IORunnable deserializer,
            final IORunnable serializer) {
        if (Files.exists(path)) {
            try {
                deserializer.run();
                return;
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        try {
            serializer.run();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void findAllUids() {
        if (foundUids == true) {
            return;
        }
        findAll(UID_CACHE, this::deserializeUids, this::serializeUids);
    }
    
    private void findAllAuthors() throws IOException {
        findAllUids();
        findAll(AUTHOR_CACHE, this::deserializeAuthors, this::serializeAuthors);
    }
    
    private void ensureAllAuthorsFound() throws IOException {
        if (authors == null) {
            findAllAuthors();
        }
    }
    
    public LitAuthor[] allAuthors() throws IOException {
        ensureAllAuthorsFound();
        return authors;
    }
    
    public Stream<LitAuthor> authorStream() throws IOException {
        return Stream.of(allAuthors());
    }
    
    public Stream<LitStory> storyStream() throws IOException {
        return authorStream().flatMap(LitAuthor::storyStream);
    }
    
    public static void main(final String[] args) throws IOException {
        final int numThreads = 1;//300;
        final String numThreadsProperty = "java.util.concurrent.ForkJoinPool.common.parallelism";
        final String oldNumThreads = System.getProperty(numThreadsProperty);
        final String newNumThreads = String.valueOf(numThreads);
        System.setProperty(numThreadsProperty, newNumThreads);
        AUTHOR_CACHE.toFile().delete();
        final AllLitAuthorSearch search = new AllLitAuthorSearch();
        search.authorStream().forEach(System.out::println);
        //search.findAllUids();
    }
    
}
