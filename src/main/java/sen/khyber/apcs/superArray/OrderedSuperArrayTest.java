package sen.khyber.apcs.superArray;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import sen.khyber.io.MyFiles;
import sen.khyber.language.gutenberg.OneBook;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class OrderedSuperArrayTest {
    
    private static final Random random = new Random();
    
    private static void addFile(final OrderedSuperArray list, final Path path) throws IOException {
        // must use addAll, which uses timsort and mergesort
        list.addAll(Arrays.asList(MyFiles.read(path).split("\\W+")));
        
        // insertion sort too slow
        /*for (final String word : MyFiles.read(path).split("\\W+")) {
            list.add(word);
        }*/
    }
    
    private static void checkSorted(final OrderedSuperArray list) {
        final List<String> sortedList = new ArrayList<>(list);
        final List<String> unsortedList = new ArrayList<>(list);
        sortedList.sort(null);
        if (!sortedList.equals(unsortedList)) {
            throw new AssertionError();
        }
    }
    
    public static void gutenbergTest(final int numBooks) throws IOException {
        final OrderedSuperArray gutenbergWords = new OrderedSuperArray();
        final AtomicInteger i = new AtomicInteger();
        OneBook.gutenbergStream(numBooks).forEach(path -> {
            try {
                System.out.println(i.incrementAndGet() + ": " + path);
                addFile(gutenbergWords, path);
            } catch (final IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        checkSorted(gutenbergWords);
        final List<String> sortedList = new ArrayList<>(gutenbergWords);
        System.out.println(gutenbergWords.size());
        System.out.println(sortedList.parallelStream().mapToInt(s -> s.length()).sum());
    }
    
    public static void gutenbergTest() throws IOException {
        gutenbergTest(3);
    }
    
    private static String randomString(final int length) {
        final byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return new String(bytes);
    }
    
    public static void randomTest(final int size, final int maxLength) {
        final OrderedSuperArray list = new OrderedSuperArray(size);
        for (int i = 0; i < size; i++) {
            list.add(randomString(maxLength));
        }
        checkSorted(list);
    }
    
    public static void main(final String[] args) throws Exception {
        //gutenbergTest();
        randomTest(100000, 25);
    }
    
}
