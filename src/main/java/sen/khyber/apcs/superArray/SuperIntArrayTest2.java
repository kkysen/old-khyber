package sen.khyber.apcs.superArray;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class SuperIntArrayTest2 {
    
    private static final Random random = new Random();
    
    /**
     * I don't remember exactly how the assert statement
     * or JUnit work and how to make it throw exceptions,
     * so this one throws an AssertionException instead
     */
    public static void assertEqual(final Object a, final Object b, final String message) {
        if (!a.equals(b)) {
            throw new AssertionError("a=" + a + ", b=" + b + " " + message);
        }
    }
    
    public static void assertEqual(final Object a, final Object b) {
        assertEqual(a, b, "");
    }
    
    public static void assertTrue(final boolean bool) {
        if (!bool) {
            throw new AssertionError();
        }
    }
    
    public static int reflectCapacity(final SuperIntArray list) throws NoSuchFieldException,
            SecurityException, IllegalArgumentException, IllegalAccessException {
        final Field dataField = list.getClass().getSuperclass().getDeclaredField("data");
        dataField.setAccessible(true);
        final Object[] data = (Object[]) dataField.get(list);
        return data.length;
    }
    
    // will run out of memory of too many loops b/c capacity keeps increasing exponentially
    public static void testEnsureCapacity() throws NoSuchFieldException, SecurityException,
            IllegalArgumentException, IllegalAccessException {
        final SuperIntArray list = new SuperIntArray();
        for (int i = 0; i < 10; i++) {
            final int randCapacity = random.nextInt(1000);
            //System.out.println(randCapacity + " " + reflectCapacity(list));
            list.ensureCapacity(randCapacity);
            assertTrue(reflectCapacity(list) >= randCapacity);
        }
    }
    
    public static void testTrimToSize() throws NoSuchFieldException, SecurityException,
            IllegalArgumentException, IllegalAccessException {
        for (int i = 0; i < 100; i++) {
            final int randSize = random.nextInt(1000);
            final SuperIntArray list = random.ints(randSize).boxed()
                    .collect(Collectors.toCollection(SuperIntArray::new));
            list.trimToSize();
            assertEqual(reflectCapacity(list), randSize);
        }
    }
    
    public static void testGet() {
        final List<Integer> otherList = random.ints(1000).boxed().collect(Collectors.toList());
        final SuperIntArray list = new SuperIntArray(otherList);
        for (int i = 0; i < list.size(); i++) {
            assertEqual(list.get(i), otherList.get(i));
        }
    }
    
    public static void testSet() {
        final SuperIntArray list1 = random.ints(1000).boxed()
                .collect(Collectors.toCollection(SuperIntArray::new));
        final SuperIntArray list2 = random.ints(1000).boxed()
                .collect(Collectors.toCollection(SuperIntArray::new));
        for (int i = 0; i < list1.size(); i++) {
            list1.set(i, list2.get(i));
        }
        assertEqual(list1, list2);
    }
    
    public static void testToArray() {
        final int[] ints = random.ints(1000).toArray();
        final SuperIntArray list = new SuperIntArray(ints.length);
        for (int i = 0; i < ints.length; i++) {
            list.add(ints[i]);
        }
        assertTrue(Arrays.equals(ints, list.toIntArray()));
    }
    
    public static void testAddAll() {
        final List<Integer> arrayList = random.ints(1000).boxed()
                .collect(Collectors.toCollection(ArrayList::new));
        final SuperIntArray list = new SuperIntArray();
        list.addAll(arrayList);
        for (int i = 0; i < list.size(); i++) {
            assertEqual(list.get(i), arrayList.get(i));
        }
    }
    
    public static void testRemove() {
        final int size = 10;
        final SuperIntArray list = random.ints(size).boxed()
                .collect(Collectors.toCollection(SuperIntArray::new));
        final SuperIntArray listCopy = new SuperIntArray(list);
        final List<Integer> randList = new ArrayList<>(size / 2);
        for (int i = 0; i < size / 2; i++) {
            randList.add(random.nextInt(size - i));
        }
        final SuperIntArray removedList = new SuperIntArray(size / 2);
        for (final int i : randList) {
            removedList.add(list.remove(i));
        }
        /*System.out.println("list: " + list);
        System.out.println("removedList: " + removedList);
        System.out.println("listCopy: " + listCopy);*/
        assertEqual(listCopy.size() - list.size(), randList.size());
        list.addAll(removedList);
        /*System.out.println("list: " + list);
        System.out.println("removedList: " + removedList);
        System.out.println("listCopy: " + listCopy);*/
        assertEqual(new HashSet<>(list), new HashSet<>(listCopy));
    }
    
    public static void main(final String[] args) throws Exception {
        testEnsureCapacity();
        testTrimToSize();
        for (int i = 0; i < 100; i++) {
            testGet();
            testSet();
            testToArray();
            testAddAll();
            testRemove();
        }
    }
    
}
