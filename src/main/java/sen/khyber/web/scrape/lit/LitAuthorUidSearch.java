package sen.khyber.web.scrape.lit;

import sen.khyber.io.MyFiles;
import sen.khyber.util.Timer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class LitAuthorUidSearch {
    
    private static final OkHttpClient client = new OkHttpClient();
    
    private static final String URL = Lit.Url.AUTHOR.getUrl();
    
    private static boolean exists(final String url) throws IOException {
        final Request request = new Request.Builder().url(url).addHeader("Range", "bytes=0-2")
                .build();
        final InputStream in = client.newCall(request).execute().body().byteStream();
        final byte[] bytes = new byte[2];
        in.read(bytes);
        // if author doesn't exist, first line will start <b> instead of <?xml
        // b == 98
        return bytes[1] != 98;
    }
    
    private static boolean exists(final int uid) throws IOException {
        return new Fiber<Boolean>() {
            
            private static final long serialVersionUID = 1L;
            
            @Override
            protected Boolean run() throws SuspendExecution, InterruptedException {
                return exists(URL + uid);
            };
            
        }.get();
    }
    
    private static IntStream searchForUids(final int startUid, final int endUid,
            final int numThreads)
            throws IOException {
        final String numThreadsProperty = "java.util.concurrent.ForkJoinPool.common.parallelism";
        final String oldNumThreads = System.getProperty(numThreadsProperty);
        final String newNumThreads = String.valueOf(numThreads);
        System.setProperty(numThreadsProperty, newNumThreads);
        final IntStream uids = IntStream.range(startUid, endUid)
                .parallel()
                .filter(uid -> {
                    try {
                        return LitAuthorUidSearch.exists(uid);
                    } catch (final IOException e) {
                        return false;
                    }
                })
        //.sorted()
        ;
        System.setProperty(numThreadsProperty, oldNumThreads);
        return uids;
    }
    
    private static IntStream searchForUids(final int startUid, final int endUid)
            throws IOException {
        int numThreads = endUid - startUid >> 4;
        if (numThreads > 1 << 12) {
            numThreads = 1 << 12;
        }
        return searchForUids(startUid, endUid, numThreads);
    }
    
    // will take 175 hours at 1000 uids / 63 seconds
    private static IntStream searchForAllUids() throws IOException {
        return searchForUids(1, 10_000_000);
    }
    
    public static void saveAllUids(final Path path) throws IOException {
        MyFiles.write(path, searchForAllUids());
    }
    
    private static Stream<LitAuthor> searchForAllAuthors(final IntStream uids) {
        return uids.parallel()
                .mapToObj(uid -> URL + uid + "&page=submissions")
                .map(url -> {
                    try {
                        return new LitAuthor(url);
                    } catch (final IOException e) {
                        return null;
                    }
                }).filter(author -> author != null);
    }
    
    public static Stream<LitAuthor> searchForAllAuthors() throws IOException {
        return searchForAllAuthors(searchForAllUids());
    }
    
    public static Stream<LitAuthor> searchForAllAuthors(final Path path) throws IOException {
        return searchForAllAuthors(Files.lines(path).mapToInt(Integer::parseInt));
    }
    
    public static void main(final String[] args) throws Exception {
        Timer.time();
        searchForUids(1, 1_000_000, 1000).forEach(System.out::println);
        Timer.printlnTime();
    }
    
}
