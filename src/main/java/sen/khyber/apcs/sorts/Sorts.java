package sen.khyber.apcs.sorts;

import sen.khyber.util.Timer;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Consumer;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class Sorts {
    
    private static Random random = new Random();
    
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
    
    private static int insertionIndex(final int[] a, final int index) {
        final int val = a[index];
        //        System.out.println("val: " + val);
        for (int i = index - 1; i >= 0; i--) {
            //            System.out.println("a[i]: " + a[i]);
            if (val > a[i]) {
                return i + 1;
            }
        }
        return 0;
    }
    
    private static void insert(final int[] a, final int index) {
        final int insertionIndex = insertionIndex(a, index);
        //        System.out.println("index: " + index);
        //        System.out.println("insertionIndex: " + insertionIndex);
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
        
    }
    
    private static int[] warmUp() {
        int[] a = random.ints(1_000_000).toArray();
        for (int i = 0; i < 10; i++) {
            Arrays.sort(a);
            a = random.ints(1_000_000).toArray();
        }
        Arrays.sort(a);
        return a;
    }
    
    private static void testNoWarmUp(final Consumer<int[]> testSorter,
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
    
    public static void test(final Consumer<int[]> testSorter, final Consumer<int[]> baselineSorter,
            final int size) {
        warmUp();
        testNoWarmUp(testSorter, baselineSorter, size);
    }
    
    public static void test(final Consumer<int[]> sorter, final int size) {
        test(sorter, Arrays::sort, size);
    }
    
    public static void print(final int[] a) {
        System.out.println(Arrays.toString(a));
    }
    
    public static void main(final String[] args) {
        test(Sorts::insertionSort, Arrays::sort, 500000);
        final int[] a = new int[] {3, 5, 1, 6, 8, 3, 9};
//        insertionSort(a);
//        print(a);
    }
    
}
