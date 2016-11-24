package sen.khyber.apcs.superArray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class OrderedSuperArrayTest2 {
    
    private static final Random random = new Random();
    
    private static void checkSorted(final OrderedSuperArray list) {
        final List<String> sortedList;
        final List<String> unsortedList;
        if (list instanceof Collection<?>) {
            sortedList = new ArrayList<>(list);
            unsortedList = new ArrayList<>(list);
        } else {
            sortedList = new ArrayList<>(list.size());
            unsortedList = new ArrayList<>(list.size());
            for (int i = 0; i < list.size(); i++) {
                final String s = list.get(i);
                sortedList.add(s);
                unsortedList.add(s);
            }
        }
        sortedList.sort(null);
        if (!sortedList.equals(unsortedList)) {
            throw new AssertionError();
        }
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
        randomTest(100000, 25);
    }
    
}
