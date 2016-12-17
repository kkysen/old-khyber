package sen.khyber.apcs.sorts;

import sen.khyber.util.Timer;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Consumer;

import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.Benchmark;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class Sorts {
    
    private static final Random random = new Random();
    
    public static String name() {
        return "06.Last.First";
    }
    
    private static void swap(final int[] a, final int i, final int j) {
        final int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
    
    private static int minIndex(final int[] a, final int startIndex, final int endIndex) {
        int min = a[startIndex];
        int val;
        int index = startIndex;
        for (int i = startIndex + 1; i < endIndex; i++) {
            if ((val = a[i]) < min) {
                min = val;
                index = i;
            }
        }
        return index;
    }
    
    public static void selectionSort(final int[] a) {
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            swap(a, i, minIndex(a, i, len));
        }
    }
    
    public static void selectionSortInlined(final int[] a) {
        int temp;
        for (int i = 0; i < a.length - 1; i++) {
            int minIndex = i;
            for (int j = i; j < a.length; j++) {
                if (a[i] < a[minIndex]) {
                    minIndex = i;
                }
            }
            temp = a[i];
            a[i] = a[minIndex];
            a[minIndex] = temp;
        }
    }
    
    private static int insertionIndex(final int[] a, final int index) {
        final int val = a[index];
        for (int i = index - 1; i >= 0; i--) {
            if (val > a[i]) {
                return i + 1;
            }
        }
        return 0;
    }
    
    private static void insert(final int[] a, final int index) {
        final int insertionIndex = insertionIndex(a, index);
        final int val = a[index];
        System.arraycopy(a, insertionIndex, a, insertionIndex + 1, index - insertionIndex);
        a[insertionIndex] = val;
    }
    
    public static void insertionSort(final int[] a) {
        final int len = a.length;
        for (int i = 1; i < len; i++) {
            insert(a, i);
        }
    }
    
    public static void insertionSortInlined(final int[] a) {
        for (int i = 1; i < a.length; i++) {
            final int prev = a[i];
            final int index = i;
            while (index > 0 && prev < a[index - 1]) {
                a[i] = a[i - 1];
                i--;
            }
            a[index] = prev;
        }
    }
    
    private static boolean bubbleSortPass(final int[] a, final int endIndex) {
        boolean sorted = true;
        int prev = a[0];
        for (int i = 1; i < endIndex; i++) {
            if (prev > (prev = a[i])) {
                swap(a, i - 1, i);
                sorted = false;
            }
        }
        return sorted;
    }
    
    public static void bubbleSort(final int[] a) {
        for (int i = a.length; i >= 2; i--) {
            if (bubbleSortPass(a, i)) {
                break;
            }
        }
    }
    
    private static void quickSort(final int[] a, final int left, final int right) {
        int i = left, j = right;
        final int pivot = a[(i + j) / 2];
        int temp;
        while (i <= j) {
            while (a[i] < pivot) {
                i++;
            }
            while (a[j] > pivot) {
                j--;
            }
            if (i <= j) {
                temp = a[i];
                a[i++] = a[j];
                a[j--] = temp;
            }
        }
        if (left < j) {
            quickSort(a, left, j);
        }
        if (i < right) {
            quickSort(a, i, right);
        }
    }
    
    public static void quickSort(final int[] a) {
        quickSort(a, 0, a.length - 1);
    }
    
    private static int[] mergeSort(final int[] a, final int[] b) {
        final int[] merged = new int[a.length + b.length];
        int i = 0;
        int j = 0;
        int aVal;
        int bVal;
        for (int k = 0; k < merged.length; k++) {
            if ((aVal = a[i]) < (bVal = b[j])) {
                merged[k] = aVal;
                i++;
            } else {
                merged[k] = bVal;
                j++;
            }
        }
        return merged;
    }
    
    public static void mergeSort(int[] a) {
        final int splitIndex = a.length / 2;
        final int[] b = Arrays.copyOfRange(a, splitIndex, a.length);
        a = Arrays.copyOfRange(a, 0, splitIndex);
        // FIXME
    }
    
    public static void bucketSort(final int[] a, final int startRange, final int endRange) {
        final int len = a.length;
        final int range = startRange - endRange;
        final int[] buckets = new int[range];
        for (int i = 0; i < len; i++) {
            buckets[a[i] - range]++;
        }
        for (int i = 0, from = 0, to = 0; i < buckets.length; i++, from = to) {
            to = buckets[i];
            Arrays.fill(a, from, to, i + startRange);
        }
    }
    
    private static final int mask = 1;
    
    private static int[] radixSort(final int[] a, int bits) {
        if (bits == 31) {
            return a;
        }
        final int[][] buckets = new int[mask + 1][a.length + 1];
        for (int i = 0; i < a.length; i++) {
            final int n = a[i];
            final int whichBucket = n >>> bits & mask;
            final int index = buckets[whichBucket][0];
            buckets[whichBucket][index] = n;
        }
        final int[] sorted = new int[a.length];
        int index = 0;
        for (final int[] bucket : buckets) {
            final int len = bucket.length - 1;
            System.arraycopy(sorted, index, radixSort(bucket, ++bits), 1, len);
            index += len;
        }
        return sorted;
    }
    
    public static int[] radixSort(final int[] a) {
        return radixSort(a, 0);
    }
    
//    private static int[] warmUp() {
//        int[] a = random.ints(1_000_000).toArray();
//        for (int i = 0; i < 10; i++) {
//            Arrays.sort(a);
//            a = random.ints(1_000_000).toArray();
//        }
//        Arrays.sort(a);
//        return a;
//    }
    
//    private static void testSortNoWarmUp(final Consumer<int[]> testSorter,
//            final Consumer<int[]> baselineSorter, final int size) {
//        final int[] a1 = random.ints(size).toArray();
//        final int[] a2 = Arrays.copyOf(a1, a1.length);
//        Timer.start();
//        testSorter.accept(a1);
//        final long testTime = Timer.time();
//        Timer.printlnTime("testTime");
//        Timer.start();
//        testSorter.accept(a2);
//        final long baselineTime = Timer.time();
//        Timer.printlnTime("baselineTime");
//        System.out.println((double) testTime / baselineTime + " slower");
//        if (!Arrays.equals(a1, a2)) {
//            throw new AssertionError("sorting algorithm failed");
//        }
//    }
    
//    public static void testSort(final Consumer<int[]> testSorter, final Consumer<int[]> baselineSorter,
//            final int size) {
//        warmUp();
//        testSortNoWarmUp(testSorter, baselineSorter, size);
//    }
    
    private static void testSort(final Consumer<int[]> testSorter,
            final Consumer<int[]> baselineSorter, final int size) {
        final int[] a1 = random.ints(size).toArray();
        final int[] a2 = Arrays.copyOf(a1, a1.length);
        Timer.start();
        testSorter.accept(a1);
        final long testTime = Timer.time();
        Timer.printlnTime("testTime");
        Timer.start();
        testSorter.accept(a2);
        final long baselineTime = Timer.time();
        Timer.printlnTime("baselineTime");
        System.out.println((double) testTime / baselineTime + " slower");
        if (!Arrays.equals(a1, a2)) {
            throw new AssertionError("sorting algorithm failed");
        }
    }
    
    public static void testSort(final Consumer<int[]> sorter, final int size) {
        testSort(sorter, Arrays::sort, size);
    }
    
    public static void print(final int[] a) {
        System.out.println(Arrays.toString(a));
    }
    
    @Benchmark
    public static void test() {
        testSort(Sorts::insertionSort, Arrays::sort, 50000);
    }
    
    public static void main(final String[] args) throws Exception {
        Main.main(args);
        
//        System.out.println("done");
//        final int[] a = new int[] {3, 5, 1, 6, 8, 3, 9};
//        insertionSort(a);
//        print(a);
    }
    
}
