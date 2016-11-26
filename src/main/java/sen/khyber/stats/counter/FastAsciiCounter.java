package sen.khyber.stats.counter;

import sen.khyber.util.Parallel;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class FastAsciiCounter {
    
    private static final int NUM_CORES = Parallel.numCores();
    private static final long MAX_MEM = Parallel.maxMemory();
    private static final int BUF_SIZE = (int) (MAX_MEM / 4 > Integer.MAX_VALUE
            ? Integer.MAX_VALUE : MAX_MEM / 4);
    
    private static final int[] arrCounter = new int[256];
    
    private final Map<Character, AtomicLong> counter = new ConcurrentHashMap<>();
    
    private long startTime;
    
    public FastAsciiCounter() {
        for (int i = -128; i < 128; i++) {
            counter.put((char) i, new AtomicLong());
        }
    }
    
    private void addBytes(final byte[] bytes, final int startIndex, final int endIndex) {
        final int[] arrCounter = FastAsciiCounter.arrCounter.clone();
        for (int i = startIndex; i < endIndex; i++) {
            arrCounter[bytes[i] + 128]++;
        }
        for (int i = 0; i < 256; i++) {
            counter.get((char) (i - 128)).getAndAdd(arrCounter[i]);
        }
    }
    
    private static int[][] getIntervals(final int length) {
        final int[][] intervals = new int[NUM_CORES][];
        final int intervalLength = length / NUM_CORES;
        for (int i = 0; i < NUM_CORES; i++) {
            final int start = i * intervalLength;
            intervals[i] = new int[] {start, start + intervalLength};
        }
        intervals[NUM_CORES - 1][1] = length;
        return intervals;
    }
    
    private void addBytes(final byte[] bytes) {
        Stream.of(getIntervals(bytes.length))
                .parallel()
                .forEach(interval -> {
                    //System.out.println("interval: [" + interval[0] + ", " + interval[1] + ")");
                    addBytes(bytes, interval[0], interval[1]);
                });
    }
    
    private static double percent(final double percent, final int numDecimals) {
        final double pow = Math.pow(10, numDecimals);
        final int intPercent = (int) (percent * 100 * pow);
        return intPercent / pow;
    }
    
    public void addString(final String s) {
        addBytes(s.getBytes());
    }
    
    public String time() {
        final long longMillis = System.currentTimeMillis() - startTime;
        final int millis = (int) (longMillis % 1000);
        int seconds = (int) (longMillis / 1000);
        final int minutes = seconds / 60;
        seconds %= 60;
        return minutes + ":" + seconds + "." + millis;
    }
    
    public void addFile(final Path path) throws IOException {
        final long fileSize = Files.size(path);
        final BufferedInputStream in = new BufferedInputStream(Files.newInputStream(path),
                BUF_SIZE);
        final byte[] bytes = new byte[BUF_SIZE];
        System.out.println("reading " + (fileSize >> 20) + " MB...");
        startTime = System.currentTimeMillis();
        for (long L = 0; L < fileSize;) {
            L += in.read(bytes);
            System.out
                    .print("read " + (L >> 20) + " MB " + percent((double) L / fileSize, 2) + "%");
            //System.out.println(counter);
            //System.out.println();
            addBytes(bytes);
            System.out.println(" done adding bytes " + time());
        }
    }
    
    public void addFile(final String path) throws IOException {
        addFile(Paths.get(path));
    }
    
    public <C extends CharCounter> C toCharCounter(final C charCounter) {
        charCounter.addMap(counter);
        return charCounter;
    }
    
    public CharCounter toCharCounter() {
        final CharCounter charCounter = new CharCounter();
        return toCharCounter(charCounter);
    }
    
    public static void main(final String[] args) throws Exception {
        final FastAsciiCounter counter = new FastAsciiCounter();
        counter.addFile("C:/Users/kkyse/OneDrive/CS/www.gutenberg.lib.md.us/"
                + "OneBook/OneBook.txt");
        final CharCounter asciiCounter = counter.toCharCounter();
        asciiCounter.saveSorted(Paths.get("asciiCounter.csv"));
        //asciiCounter.sortedCounts().forEach(System.out::println);
        System.out.println(asciiCounter.length());
    }
    
}
