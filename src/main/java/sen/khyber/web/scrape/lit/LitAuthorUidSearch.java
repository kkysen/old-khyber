package sen.khyber.web.scrape.lit;

import sen.khyber.io.MyFiles;
import sen.khyber.util.Timer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class LitAuthorUidSearch {
    
    private static final String URL = Lit.AUTHOR_URL;
    
    private static boolean exists(final String urlStr) throws IOException {
        final URL url = new URL(urlStr);
        final InputStream in = url.openStream();
        //final BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        in.read();
        // if author doesn't exist, first line will start <b> instead of <?xml
        // b == 98
        return in.read() != 98;
    }
    
    private static boolean exists(final int uid) throws IOException {
        return exists(URL + uid);
    }
    
    private static IntStream searchForUids(final int startUid, final int endUid, final int numThreads)
            throws IOException {
        final String numThreadsProperty = "java.util.concurrent.ForkJoinPool.common.parallelism";
        final String oldNumThreads = System.getProperty(numThreadsProperty);
        final String newNumThreads = String.valueOf(numThreads);
        System.setProperty(numThreadsProperty, newNumThreads);
        final IntStream uids =  IntStream.range(startUid, endUid)
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
    
    private static IntStream searchForUids(final int startUid, final int endUid) throws IOException {
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
