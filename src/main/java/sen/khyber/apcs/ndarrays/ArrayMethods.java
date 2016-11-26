package sen.khyber.apcs.ndarrays;

import java.util.Arrays;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class ArrayMethods {
    
    public static int rowSum(final int[][] a, final int i) {
        final int[] row = a[i];
        int sum = 0;
        for (int j = 0; j < row.length; j++) {
            sum += row[j];
        }
        return sum;
    }
    
    public static int columnSum(final int[][] a, final int j) {
        int sum = 0;
        for (int i = 0; i < a.length; i++) {
            if (j < a[i].length) {
                sum += a[i][j];
            }
        }
        return sum;
    }
    
    public static int[] allRowsSum(final int[][] a) {
        final int[] sums = new int[a.length];
        for (int i = 0; i < sums.length; i++) {
            sums[i] = rowSum(a, i);
        }
        return sums;
    }
    
    public static boolean isRowMagic(final int[][] a) {
        if (a.length == 0) {
            return true;
        }
        final int firstRowSum = rowSum(a, 0);
        for (int i = 1; i < a.length; i++) {
            if (rowSum(a, i) != firstRowSum) {
                return false;
            }
        }
        return true;
    }
    
    private static int maxNumCols(final int[][] a) {
        int maxNumCols = 0;
        for (int i = 0; i < a.length; i++) {
            int numCols;
            if ((numCols = a[i].length) > maxNumCols) {
                maxNumCols = numCols;
            }
        }
        return maxNumCols;
    }
    
    public static boolean isColumnMagic(final int[][] a) {
        final int numCols = maxNumCols(a);
        if (numCols == 0) {
            return true;
        }
        final int firstColSum = columnSum(a, 0);
        for (int j = 1; j < numCols; j++) {
            if (columnSum(a, j) != firstColSum) {
                return false;
            }
        }
        return true;
    }
    
    private static void assertTrue(final boolean bool) {
        if (!bool) {
            throw new AssertionError();
        }
    }
    
    public static void allRowsSumTest() {
        final int[][] a = new int[][] {
            {4, 2, 5, 7, 9, 6, 4, 2, 7},
            {4, 2, 1, 5, 7, 6, 3, 8, 0},
            {5, 4, 6},
            {},
            {0}
        };
        final int[] correctSums = new int[] {46, 36, 15, 0, 0};
        assertTrue(Arrays.equals(allRowsSum(a), correctSums));
    }
    
    public static void isColumnMagicTest() {
        int[][] magic = new int[][] {};
        assertTrue(isColumnMagic(magic));
        magic = new int[][] {
            {1, 25},
            {2, 15},
            {3, 26, 66},
            {4},
            {5},
            {6},
            {7},
            {8},
            {9},
            {10},
            {11}
        };  // 66 per column
        assertTrue(isColumnMagic(magic));
    }
    
    public static void main(final String[] args) {
        allRowsSumTest();
        isColumnMagicTest();
    }
    
}
