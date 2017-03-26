package sen.khyber.web.scrape.lit;

import sen.khyber.web.Internet;
import sen.khyber.web.scrape.lit.Lit.Category;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;
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
    private static final int MAX_UID = (int) Math.pow(10, MAX_DIGITS);
    private static final int MAX_BITS = Integer.SIZE - Integer.numberOfLeadingZeros(MAX_UID);
    private static final int MAX_BYTES = (int) Math.ceil((double) MAX_BITS / Byte.SIZE);
    
    private final Set<Integer> uids = ConcurrentHashMap.newKeySet();
    private boolean foundUids;
    
    private final List<String> failedUrls = new ArrayList<>();
    private final List<Category> failedCategories = new ArrayList<>();
    
    private List<LitAuthor> authors;
    
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
                uids.add(uid);
                // skip ahead a little to avoid checking other stuff
                i += MIN_SKIP_LENGTH - (i - start);
            }
        }
    }
    
    private Stream<String> getCategoryPageUrls(final String baseUrl) throws IOException {
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
            return Stream.empty();
        }
        i -= 9;
        final int end = i;
        while (bytes[--i] != '/') {}
        for (i++; i < end; i++) {
            numPages = numPages * 10 + bytes[i] - '0';
        }
        System.out.println("# pages: " + numPages);
        return IntStream.rangeClosed(1, numPages)
                .mapToObj(pageNum -> baseUrl + pageNum + "-page");
    }
    
    private void findAllUidsInCategory(final String baseUrl) throws IOException {
        System.out.println("\treading " + baseUrl);
        getCategoryPageUrls(baseUrl)
                .parallel()
                .forEach(url -> {
                    try {
                        findAllUidsInPage(url);
                    } catch (final IOException e) {
                        failedUrls.add(url);
                        e.printStackTrace();
                    }
                });
    }
    
    public void findAllUids() {
        if (foundUids == true) {
            return;
        }
        if (Files.exists(UID_CACHE)) {
            try {
                deserializeUids();
                return;
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        final long start = System.nanoTime();
        for (final Category category : Category.values()) {
            try {
                findAllUidsInCategory(category.getUrl());
            } catch (final IOException e) {
                failedCategories.add(category);
                e.printStackTrace();
            }
        }
        
        // try failed ones again
        final int iterNum = 0;
        while (failedUrls.size() != 0) {
            try {
                Thread.sleep((long) (iterNum * 1e3));
            } catch (final InterruptedException e1) {
                e1.printStackTrace();
            }
            final List<String> retryingUrls = new ArrayList<>(failedUrls);
            failedUrls.clear();
            retryingUrls.parallelStream().forEach(url -> {
                try {
                    findAllUidsInPage(url);
                } catch (final IOException e) {
                    failedUrls.add(url);
                    e.printStackTrace();
                }
            });
        }
        
        final long time = System.nanoTime() - start;
        System.out.println(time / 1e9);
        foundUids = true;
    }
    
    private void findAllAuthors() {
        findAllUids();
        final AtomicInteger i = new AtomicInteger();
        authors = uids.parallelStream()
                .limit(2000)
                .unordered()
                .map(uid -> {
                    try {
                        System.out.println("reading author " + uid + '\t' + i.incrementAndGet());
                        return new LitAuthor(uid);
                    } catch (final IOException | RuntimeException e) {
                        e.printStackTrace();
                        return null;
                    }
                }).filter(author -> author != null)
                .collect(Collectors.toList());
    }
    
    public void finishFindingAuthors() throws IOException {
        findAllUids();
        
        //        if (!Files.exists(AUTHOR_CACHE)) {
        //            malloc(AUTHOR_CACHE, 1);
        //        }
        deserializeAuthors();
        
        final Set<Integer> existingUids = new HashSet<>();
        for (final LitAuthor author : authors) {
            existingUids.add(author.getUid());
        }
        
        // save copy of all uids to put back later
        final Set<Integer> uidsCopy = new HashSet<>(uids);
        
        // filter out existing uids so that only find new authors
        uids.removeAll(existingUids);
        
        final List<LitAuthor> existingAuthors = authors;
        findAllAuthors();
        
        // add smaller list to bigger list
        final List<LitAuthor> newAuthors = authors;
        List<LitAuthor> mergingAuthors;
        if (existingAuthors.size() > newAuthors.size()) {
            authors = existingAuthors;
            mergingAuthors = newAuthors;
        } else {
            authors = newAuthors;
            mergingAuthors = existingAuthors;
        }
        
        authors.addAll(mergingAuthors);
        serializeAuthors();
        
        // copy back uids
        uids.clear();
        uids.addAll(uidsCopy);
        serializeUids();
    }
    
    public void updateAuthors() throws IOException {
        Files.delete(UID_CACHE);
        finishFindingAuthors();
    }
    
    public List<LitAuthor> allAuthors() throws IOException {
        if (authors == null) {
            finishFindingAuthors();
        }
        return authors;
    }
    
    private static MappedByteBuffer malloc(final Path path, final long length) throws IOException {
        final RandomAccessFile file = new RandomAccessFile(path.toFile(), "rw");
        final FileChannel channel = file.getChannel();
        final MappedByteBuffer memory = channel.map(MapMode.READ_WRITE, 0, length);
        file.close();
        return memory;
    }
    
    private static void deserialize(final Consumer<ByteBuffer> deserializer, final Path path)
            throws IOException {
        deserializer.accept(malloc(path, path.toFile().length()));
    }
    
    public void serializeUids(final ByteBuffer out) {
        final int[] uidsArray = new int[uids.size()];
        final Iterator<Integer> uidsIter = uids.iterator();
        for (int i = 0; i < uidsArray.length; i++) {
            uidsArray[i] = uidsIter.next();
        }
        Arrays.sort(uidsArray);
        out.putInt(uidsArray.length);
        for (final int uid : uidsArray) {
            out.putInt(uid);
        }
    }
    
    public void deserializeUids(final ByteBuffer in) {
        final int numUids = in.getInt();
        for (int i = 0; i < numUids; i++) {
            uids.add(in.getInt());
        }
    }
    
    public void serializeAuthors(final ByteBuffer out) {
        authors.sort(null);
        final int numAuthors = authors.size();
        out.putInt(numAuthors);
        out.mark();
        out.position(out.position() + numAuthors * Integer.BYTES);
        final int[] offsets = new int[numAuthors];
        for (int i = 0; i < numAuthors; i++) {
            offsets[i] = out.position();
            authors.get(i).serialize(out);
        }
        final int end = out.position();
        out.reset();
        for (final int offset : offsets) {
            out.putInt(offset);
        }
        out.position(end);
    }
    
    public void deserializeAuthors(final ByteBuffer in) {
        if (in.capacity() == 0) {
            authors = new ArrayList<>();
            return;
        }
        final int numAuthors = in.getInt();
        final int[] offsets = new int[numAuthors];
        for (int i = 0; i < offsets.length; i++) {
            offsets[i] = in.getInt();
        }
        authors = IntStream.of(offsets)
                .parallel()
                .mapToObj(offset -> {
                    final ByteBuffer inDuplicate = in.duplicate();
                    inDuplicate.position(offset);
                    return new LitAuthor(inDuplicate);
                })
                .collect(Collectors.toList());
    }
    
    public void serializeUids() throws IOException {
        final long length = (uids.size() + 1) * Integer.BYTES;
        serializeUids(malloc(UID_CACHE, length));
    }
    
    public void deserializeUids() throws IOException {
        System.out.println("deserializing uids");
        deserialize(this::deserializeUids, UID_CACHE);
        System.out.println("deserialized uids");
    }
    
    public void serializeAuthors() throws IOException {
        int length = (authors.size() + 1) * Integer.BYTES;
        for (final LitAuthor author : authors) {
            length += author.serializedLength();
        }
        serializeAuthors(malloc(AUTHOR_CACHE, length));
    }
    
    public void deserializeAuthors() throws IOException {
        System.out.println("deserializing authors");
        deserialize(this::deserializeAuthors, AUTHOR_CACHE);
        System.out.println("deserialized authors");
    }
    
    public static void main(final String[] args) throws IOException {
        final AllLitAuthorSearch search = new AllLitAuthorSearch();
        //        search.findAllUids();
        //        System.out.println("# uids: " + search.uids.size());
        //        search.serializeUids();
        //        final AllLitAuthorSearch newSearch = new AllLitAuthorSearch();
        //        newSearch.deserializeUids();
        //        System.out.println(newSearch.uids.equals(search.uids));
        //search.allAuthors();
        search.deserializeAuthors();
        search.authors.forEach(System.out::println);
    }
    
}
