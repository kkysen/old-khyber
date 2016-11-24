package sen.khyber.algos.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class QuickSorter {
    
    public static boolean[] sort(final boolean[] arr) {
        int i = 0, j = arr.length - 1;
        while (i <= j) {
            while (arr[i] == false) {
                i++;
            }
            while (arr[j] = true) {
                j--;
            }
            if (i <= j) {
                arr[i++] = false;
                arr[j--] = true;
            }
        }
        return arr;
    }
    
    private static void sort(final byte[] arr, final int left, final int right) {
        int i = left, j = right;
        final byte pivot = arr[(i + j) / 2];
        byte temp;
        while (i <= j) {
            while (arr[i] < pivot) {
                i++;
            }
            while (arr[j] > pivot) {
                j--;
            }
            if (i <= j) {
                temp = arr[i];
                arr[i++] = arr[j];
                arr[j--] = temp;
            }
        }
        if (left < j) {
            sort(arr, left, j);
        }
        if (i < right) {
            sort(arr, i, right);
        }
    }
    
    public static byte[] sort(final byte[] arr) {
        sort(arr, 0, arr.length - 1);
        return arr;
    }
    
    private static void sort(final short[] arr, final int left, final int right) {
        int i = left, j = right;
        final short pivot = arr[(i + j) / 2];
        short temp;
        while (i <= j) {
            while (arr[i] < pivot) {
                i++;
            }
            while (arr[j] > pivot) {
                j--;
            }
            if (i <= j) {
                temp = arr[i];
                arr[i++] = arr[j];
                arr[j--] = temp;
            }
        }
        if (left < j) {
            sort(arr, left, j);
        }
        if (i < right) {
            sort(arr, i, right);
        }
    }
    
    public static short[] sort(final short[] arr) {
        sort(arr, 0, arr.length - 1);
        return arr;
    }
    
    private static void sort(final char[] arr, final int left, final int right) {
        int i = left, j = right;
        final char pivot = arr[(i + j) / 2];
        char temp;
        while (i <= j) {
            while (arr[i] < pivot) {
                i++;
            }
            while (arr[j] > pivot) {
                j--;
            }
            if (i <= j) {
                temp = arr[i];
                arr[i++] = arr[j];
                arr[j--] = temp;
            }
        }
        if (left < j) {
            sort(arr, left, j);
        }
        if (i < right) {
            sort(arr, i, right);
        }
    }
    
    public static char[] sort(final char[] arr) {
        sort(arr, 0, arr.length - 1);
        return arr;
    }
    
    private static void sort(final int[] arr, final int left, final int right) {
        int i = left, j = right;
        final int pivot = arr[(i + j) / 2];
        int temp;
        while (i <= j) {
            while (arr[i] < pivot) {
                i++;
            }
            while (arr[j] > pivot) {
                j--;
            }
            if (i <= j) {
                temp = arr[i];
                arr[i++] = arr[j];
                arr[j--] = temp;
            }
        }
        if (left < j) {
            sort(arr, left, j);
        }
        if (i < right) {
            sort(arr, i, right);
        }
    }
    
    public static int[] sort(final int[] arr) {
        sort(arr, 0, arr.length - 1);
        return arr;
    }
    
    private static void sort(final long[] arr, final int left, final int right) {
        int i = left, j = right;
        final long pivot = arr[(i + j) / 2];
        long temp;
        while (i <= j) {
            while (arr[i] < pivot) {
                i++;
            }
            while (arr[j] > pivot) {
                j--;
            }
            if (i <= j) {
                temp = arr[i];
                arr[i++] = arr[j];
                arr[j--] = temp;
            }
        }
        if (left < j) {
            sort(arr, left, j);
        }
        if (i < right) {
            sort(arr, i, right);
        }
    }
    
    public static long[] sort(final long[] arr) {
        sort(arr, 0, arr.length - 1);
        return arr;
    }
    
    private static void sort(final float[] arr, final int left, final int right) {
        int i = left, j = right;
        final float pivot = arr[(i + j) / 2];
        float temp;
        while (i <= j) {
            while (arr[i] < pivot) {
                i++;
            }
            while (arr[j] > pivot) {
                j--;
            }
            if (i <= j) {
                temp = arr[i];
                arr[i++] = arr[j];
                arr[j--] = temp;
            }
        }
        if (left < j) {
            sort(arr, left, j);
        }
        if (i < right) {
            sort(arr, i, right);
        }
    }
    
    public static float[] sort(final float[] arr) {
        sort(arr, 0, arr.length - 1);
        return arr;
    }
    
    private static void sort(final double[] arr, final int left, final int right) {
        int i = left, j = right;
        final double pivot = arr[(i + j) / 2];
        double temp;
        while (i <= j) {
            while (arr[i] < pivot) {
                i++;
            }
            while (arr[j] > pivot) {
                j--;
            }
            if (i <= j) {
                temp = arr[i];
                arr[i++] = arr[j];
                arr[j--] = temp;
            }
        }
        if (left < j) {
            sort(arr, left, j);
        }
        if (i < right) {
            sort(arr, i, right);
        }
    }
    
    public static double[] sort(final double[] arr) {
        sort(arr, 0, arr.length - 1);
        return arr;
    }
    
    private static <T extends Comparable<? super T>> void sort(final T[] arr, final int left, final int right) {
        int i = left, j = right;
        final T pivot = arr[(i + j) / 2];
        T temp;
        while (i <= j) {
            while (arr[i].compareTo(pivot) < 0) {
                i++;
            }
            while (arr[j].compareTo(pivot) > 0) {
                j--;
            }
            if (i <= j) {
                temp = arr[i];
                arr[i++] = arr[j];
                arr[j--] = temp;
            }
        }
        if (left < j) {
            sort(arr, left, j);
        }
        if (i < right) {
            sort(arr, i, right);
        }
    }
    
    public static <T extends Comparable<? super T>> T[] sort(final T[] arr) {
        sort(arr, 0, arr.length - 1);
        return arr;
    }
    
    public static <T extends Comparable<? super T>> void sort(final List<T> list, final int left, final int right) {
        int i = left, j = right;
        final T pivot = list.get((i + j) / 2);
        T temp;
        while (i <= j) {
            while (list.get(i).compareTo(pivot) < 0) {
                i++;
            }
            while (list.get(j).compareTo(pivot) > 0) {
                j--;
            }
            if (i <= j) {
                temp = list.get(i);
                list.set(i++, list.get(j));
                list.set(j--, temp);
            }
        }
        if (left < j) {
            sort(list, left, j);
        }
        if (i < right) {
            sort(list, i, right);
        }
    }
    
    public static <T extends Comparable<? super T>> List<T> sort(final Collection<? extends T> coll) {
        final List<T> list = new ArrayList<>(coll);
        sort(list, 0, list.size() - 1);
        return list;
    }
    
    public static void main(final String[] args) {
        
        final Integer[] arr = {3, 1, 6, 4, 8, 6, 2, 76, 34, 98, -111};
        sort(arr);
        System.out.println(Arrays.toString(arr));
        
        final List<Integer> list = Arrays.asList(arr);
        System.out.println(sort(list));
        
    }
    
}