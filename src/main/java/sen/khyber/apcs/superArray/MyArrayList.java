package sen.khyber.apcs.superArray;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

/**
 * a rough implementation of ArrayList renamed MyArrayList
 * some methods of List are skipped and throw an UnsupportedOperationException
 * not all of the extra methods of ArrayList (not in List) are implemented
 * either,
 * but the basic ArrayList / List methods are all implemented
 * 
 * @author Khyber Sen
 * @param <E> type of element in this List
 */
public class MyArrayList<E> implements List<E> {
    
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
    
    private static final int DEFAULT_CAPACITY = 10;
    
    protected int size;
    
    protected Object[] data;
    
    @SuppressWarnings("unchecked")
    public MyArrayList(final int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException(
                    "initialCapacity: " + initialCapacity + " cannot be less than zero");
        }
        data = new Object[initialCapacity];
    }
    
    public MyArrayList() {
        this(DEFAULT_CAPACITY);
    }
    
    public MyArrayList(final Collection<? extends E> coll) {
        this((int) (coll.size() * 1.1));
        addAll(coll);
    }
    
    public MyArrayList(final MyArrayList<? extends E> list) {
        this((int) (list.size * 1.1));
        addAll(list);
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        trimToSize();
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(data);
        result = prime * result + size;
        return result;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        @SuppressWarnings("unchecked")
        final MyArrayList<E> other = (MyArrayList<E>) obj;
        if (size != other.size) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (!data[i].equals(other.data[i])) {
                return false;
            }
        }
        return true;
    }
    
    private void grow(final int minCapacity) {
        final int oldCapacity = data.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0) {
            newCapacity = minCapacity;
        }
        if (newCapacity - MAX_ARRAY_SIZE > 0) {
            newCapacity = MAX_ARRAY_SIZE;
        }
        data = Arrays.copyOf(data, newCapacity);
    }
    
    public void ensureCapacity(final int newCapacity) {
        if (newCapacity > DEFAULT_CAPACITY) {
            grow(newCapacity);
        }
    }
    
    public void trimToSize() {
        data = Arrays.copyOf(data, size);
    }
    
    @Override
    public int size() {
        return size;
    }
    
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    
    private String rangeExceptionMessage(final int index) {
        return "index: " + index + ", size: " + size;
    }
    
    private void rangeCheck(final int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException(rangeExceptionMessage(index));
        }
    }
    
    private void rangeCheckForAdd(final int index) {
        if (index > size) {
            throw new IndexOutOfBoundsException(rangeExceptionMessage(index));
        }
    }
    
    @SuppressWarnings("unchecked")
    protected E data(final int i) {
        return (E) data[i];
    }
    
    @Override
    public E get(final int index) {
        rangeCheck(index);
        return data(index);
    }
    
    @Override
    public E set(final int index, final E e) {
        rangeCheck(index);
        final E oldValue = data(index);
        data[index] = e;
        return oldValue;
    }
    
    @Override
    public boolean add(final E e) {
        if (size == data.length) {
            ensureCapacity(size + 1);
        }
        data[size++] = e;
        return true;
    }
    
    @Override
    public void add(final int index, final E e) {
        rangeCheckForAdd(index);
        if (size == data.length) {
            ensureCapacity(size + 1);
        }
        if (index != size) {
            System.arraycopy(data, index, data, index + 1, size - index);
        }
        data[index] = e;
        size++;
    }
    
    @Override
    public E remove(final int index) {
        rangeCheck(index);
        final E e = data(index);
        if (index != --size) {
            System.arraycopy(data, index + 1, data, index, size - index);
        }
        data[size] = null;
        return e;
    }
    
    @Override
    public boolean addAll(int index, final Collection<? extends E> coll) {
        rangeCheckForAdd(index);
        final Iterator<? extends E> iter = coll.iterator();
        final int collSize = coll.size();
        if (size + collSize > data.length) {
            ensureCapacity(size + collSize);
        }
        final int end = index + collSize;
        if (size > 0 && index != size) {
            System.arraycopy(data, index, data, end, size - index);
        }
        while (index < end) {
            data[index++] = iter.next();
        }
        size += collSize;
        return collSize > 0;
    }
    
    public boolean addAll(final int index, final MyArrayList<? extends E> list) {
        final int listSize = list.size();
        if (size + listSize > data.length) {
            ensureCapacity(size + listSize);
        }
        final int end = index + listSize;
        if (size > 0 && index != size) {
            System.arraycopy(data, index, data, end, size - index);
        }
        System.arraycopy(list.data, 0, data, index, list.size);
        size += listSize;
        return true;
    }
    
    @Override
    public boolean addAll(final Collection<? extends E> coll) {
        return addAll(size, coll);
    }
    
    public boolean addAll(final MyArrayList<? extends E> list) {
        return addAll(size, list);
    }
    
    @Override
    public boolean contains(final Object o) {
        return indexOf(o) != -1;
    }
    
    private class ArrayListIterator implements Iterator<E> {
        
        private int i = 0;
        private boolean hasNextCalled = false;
        
        @Override
        public boolean hasNext() {
            return i != size;
        }
        
        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            hasNextCalled = true;
            return data(i++);
        }
        
        @Override
        public void remove() {
            if (i == 0 || !hasNextCalled) {
                throw new IllegalStateException();
            }
            remove(--i);
            hasNextCalled = false;
        }
        
    }
    
    @Override
    public Iterator<E> iterator() {
        return new ArrayListIterator();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public E[] toArray() {
        return (E[]) Arrays.copyOf(data, size);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(final T[] arr) {
        if (arr.length < size) {
            return (T[]) Arrays.copyOf(data, size, arr.getClass());
        }
        System.arraycopy(data, 0, arr, 0, size);
        if (arr.length > size) {
            arr[size] = null;
        }
        return arr;
    }
    
    @Override
    public boolean remove(final Object o) {
        remove(indexOf(o));
        return true;
    }
    
    @Override
    public boolean containsAll(final Collection<?> c) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean removeAll(final Collection<?> c) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean retainAll(final Collection<?> c) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            data[i] = null;
        }
        size = 0;
    }
    
    @Override
    public int indexOf(final Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (data[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(data[i])) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    @Override
    public int lastIndexOf(final Object o) {
        if (o == null) {
            for (int i = size - 1; i >= 0; i--) {
                if (data[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                if (o.equals(data[i])) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    @Override
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ListIterator<E> listIterator(final int index) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public List<E> subList(final int fromIndex, final int toIndex) {
        if (fromIndex < 0 || fromIndex >= size
                || toIndex <= fromIndex || toIndex < size) {
            throw new IndexOutOfBoundsException();
        }
        final int subSize = toIndex - fromIndex;
        final MyArrayList<E> subList = new MyArrayList<>(subSize);
        System.arraycopy(data, fromIndex, subList.data, 0, subSize);
        subList.size = subSize;
        return subList;
    }
    
    @Override
    public List<E> clone() {
        return new MyArrayList<>(this);
    }
    
    @Override
    public String toString() {
        final StringJoiner sj = new StringJoiner(", ", "[", "]");
        for (int i = 0; i < size; i++) {
            sj.add(data[i].toString());
        }
        return sj.toString();
    }
    
    public String toStringDebug() {
        final StringJoiner sj = new StringJoiner(", ", "[", "]");
        for (final Object e : data) {
            if (e == null) {
                sj.add("_");
            } else {
                sj.add(e.toString());
            }
        }
        return sj.toString();
    }
    
    public static void main(final String[] args) {
        final MyArrayList<Integer> list = new MyArrayList<>();
        for (int i = 50; i < 100; i += 14) {
            list.add(i);
        }
        System.out.println(list);
        System.out.println(list.toStringDebug());
        
        final MyArrayList<List<Integer>> list2 = new MyArrayList<>();
        for (int i = 0; i < 10; i++) {
            list2.add(list.clone());
            list.add(i);
            System.out.println(list2.toStringDebug());
        }
        System.out.println(list2);
        System.out.println(list2.toStringDebug());
    }
    
}
