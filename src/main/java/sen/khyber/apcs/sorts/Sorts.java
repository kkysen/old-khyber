package sen.khyber.apcs.sorts;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Consumer;

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
    
    @Deprecated
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
    
    private static int binaryInsertionIndex(final int[] a, final int fromIndex, final int toIndex,
            final int val) {
        int low = fromIndex;
        int high = toIndex - 1;
        while (low <= high) {
            final int mid = low + high >>> 1;
            final int midVal = a[mid];
            if (midVal < val) {
                low = mid + 1;
            } else if (midVal > val) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return low;
    }
    
    @SuppressWarnings("unused")
    @Deprecated
    private static int linearInsertionIndex(final int[] a, final int index) {
        final int val = a[index];
        for (int i = index - 1; i >= 0; i--) {
            if (val > a[i]) {
                return i + 1;
            }
        }
        return 0;
    }
    
    private static int insertionIndex(final int[] a, final int index, final int offset) {
        return binaryInsertionIndex(a, 0, index - offset, a[index]);
    }
    
    @SuppressWarnings("unused")
    @Deprecated
    private static void insert(final int[] a, final int index) {
        final int insertionIndex = insertionIndex(a, index, 0);
        final int val = a[index];
        System.arraycopy(a, insertionIndex, a, insertionIndex + 1, index - insertionIndex);
        a[insertionIndex] = val;
    }
    
    // copy "length" elements of "a" from "startIndex" "shift" elements to the right
    // then insert "val" right before the beginning of the shift (after shifted)
    private static void shiftAndInsert(final int[] a, final int startIndex, final int shift,
            final int length, final int val) {
        final int endIndex = startIndex + shift;
        System.arraycopy(a, startIndex, a, endIndex, length);
        a[endIndex - 1] = val;
    }
    
    private static void insert(final int[] a, int index1, final int index2) {
        // two insertions at once to minimize copying
        int insertionIndex1 = insertionIndex(a, index1, 0);
        int insertionIndex2 = insertionIndex(a, index2, 1);
        int val1 = a[index1];
        int val2 = a[index2];
        
        // switch so that val1 < val2
        boolean switched = false;
        if (val1 > val2) {
            switched = true;
            final int tempVal = val1;
            val1 = val2;
            val2 = tempVal;
            final int tempInsertionIndex = insertionIndex1;
            insertionIndex1 = insertionIndex2;
            insertionIndex2 = tempInsertionIndex;
        }
        
        // if second elem is not moving
        if (index2 == insertionIndex2 + 1) {
            if (switched) {
                index1++; // in case elems got switched and first elem is not moving
            }
            shiftAndInsert(a, insertionIndex1, 1, index1 - insertionIndex1, val1);
            return; // exit early because other elem in place
        }
        
        // shift by 2 from insert2 to index1 
        shiftAndInsert(a, insertionIndex2, 2, index1 - insertionIndex2, val2);
        
        // shift by 1 from insert1 to insert2
        shiftAndInsert(a, insertionIndex1, 1, insertionIndex2 - insertionIndex1, val1);
    }
    
    public static void insertionSort(final int[] a) {
        final int len = a.length;
        final boolean evenLen = (len & 1) == 0;
        int startIndex = 1;
        // take care of first (second) elem so that double insertions are fine
        if (evenLen) {
            if (a[0] > a[1]) {
                swap(a, 0, 1);
            }
            startIndex++;
        }
        for (int i = startIndex; i < len; i++) {
            insert(a, i, ++i);
        }
    }
    
    @Deprecated
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
                prev = a[i];
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
        //      /** To be removed in a future release. */
        //    private static void legacyMergeSort(final Object[] a,
        //                                        final int fromIndex, final int toIndex) {
        //        final Object[] aux = copyOfRange(a, fromIndex, toIndex);
        //        mergeSort(aux, a, fromIndex, toIndex, -fromIndex);
        //    }
        //
        //    /**
        //     * Tuning parameter: list size at or below which insertion sort will be
        //     * used in preference to mergesort.
        //     * To be removed in a future release.
        //     */
        //    private static final int INSERTIONSORT_THRESHOLD = 7;
        //
        //    /**
        //     * Src is the source array that starts at index 0
        //     * Dest is the (possibly larger) array destination with a possible offset
        //     * low is the index in dest to start sorting
        //     * high is the end index in dest to end sorting
        //     * off is the offset to generate corresponding low, high in src
        //     * To be removed in a future release.
        //     */
        //    @SuppressWarnings({"unchecked", "rawtypes"})
        //    private static void mergeSort(final Object[] src,
        //                                  final Object[] dest,
        //                                  int low,
        //                                  int high,
        //                                  final int off) {
        //        final int length = high - low;
        //
        //        // Insertion sort on smallest arrays
        //        if (length < INSERTIONSORT_THRESHOLD) {
        //            for (int i=low; i<high; i++) {
        //                for (int j=i; j>low &&
        //                         ((Comparable) dest[j-1]).compareTo(dest[j])>0; j--) {
        //                    swap(dest, j, j-1);
        //                }
        //            }
        //            return;
        //        }
        //
        //        // Recursively sort halves of dest into src
        //        final int destLow  = low;
        //        final int destHigh = high;
        //        low  += off;
        //        high += off;
        //        final int mid = low + high >>> 1;
        //        mergeSort(dest, src, low, mid, -off);
        //        mergeSort(dest, src, mid, high, -off);
        //
        //        // If list is already sorted, just copy from src to dest.  This is an
        //        // optimization that results in faster sorts for nearly ordered lists.
        //        if (((Comparable)src[mid-1]).compareTo(src[mid]) <= 0) {
        //            System.arraycopy(src, low, dest, destLow, length);
        //            return;
        //        }
        //
        //        // Merge sorted halves (now in src) into dest
        //        for(int i = destLow, p = low, q = mid; i < destHigh; i++) {
        //            if (q >= high || p < mid && ((Comparable)src[p]).compareTo(src[q])<=0) {
        //                dest[i] = src[p++];
        //            } else {
        //                dest[i] = src[q++];
        //            }
        //        }
        //    }
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
    
    private static long time(final Consumer<int[]> sorter, final int[] a) {
        final long startTime = System.nanoTime();
        sorter.accept(a);
        return System.nanoTime() - startTime;
    }
    
    private static void testSort(final Consumer<int[]> testSorter,
            final Consumer<int[]> baselineSorter, final int size) {
        System.out.println("size: " + size);
        final int[] a1 = random.ints(size).toArray();
        //final int[] a1 = IntStream.range(0, size).toArray();
        final int[] a2 = Arrays.copyOf(a1, a1.length);
        final long testTime = time(testSorter, a1);
        System.out.println("testTime: " + testTime / 1_000_000.);
        final long baselineTime = time(baselineSorter, a2);
        System.out.println("baselineTime: " + baselineTime / 1_000_000.);
        final double slowFactor = (double) testTime / baselineTime;
        System.out.println(slowFactor < 1 ? 1 / slowFactor + " faster" : slowFactor + " slower");
        System.out.println();
        if (!Arrays.equals(a1, a2)) {
            System.out.println("failed : " + Arrays.toString(a1));
            System.out.println("correct: " + Arrays.toString(a2));
            throw new AssertionError("sorting algorithm failed");
        }
    }
    
    public static void testSort(final Consumer<int[]> sorter, final int size) {
        testSort(sorter, Arrays::sort, size);
    }
    
    public static void print(final int[] a) {
        System.out.println(Arrays.toString(a));
    }
    
    //    @Benchmark
    public static void test() {
        //testSort(Sorts::insertionSort, Arrays::sort, 1000000);
        testSort(Sorts::insertionSort, 10);
        testSort(Sorts::insertionSort, 100);
        testSort(Sorts::insertionSort, 1000);
        testSort(Sorts::insertionSort, 10000);
        testSort(Sorts::insertionSort, 100000);
        testSort(Sorts::insertionSort, 200000);
        //testSort(Arrays::parallelSort, 1_000_000_00);
    }
    
    public static void main(final String[] args) throws Exception {
        //        Main.main(args);
//        test();
//        test();
//        test();
        testSort(Sorts::bubbleSort, 20000);
        System.out.println("done");
    }
    
}
