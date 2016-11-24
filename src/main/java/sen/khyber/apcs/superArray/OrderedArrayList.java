package sen.khyber.apcs.superArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class OrderedArrayList<E extends Comparable<? super E>> extends MyArrayList<E> {
    
    public OrderedArrayList() {
        super();
    }
    
    public OrderedArrayList(final int initialCapacity) {
        super(initialCapacity);
    }
    
    public OrderedArrayList(final Collection<? extends E> coll) {
        super(coll);
    }
    
    public OrderedArrayList(final MyArrayList<? extends E> list) {
        super(list);
    }
    
    private int binarySearch(final E e) {
        int lo = 0;
        int hi = size - 1;
        int mid;
        int cmp;
        while (lo <= hi) {
            mid = lo + hi >>> 1;
            cmp = e.compareTo(data(mid));
            if (cmp > 0) {
                lo = mid + 1;
            } else if (cmp < 0) {
                hi = mid - 1;
            } else {
                return mid;
            }
        }
        return - (lo + 1);
    }
    
    private int findIndex(final E e) {
        final int i = binarySearch(e);
        return i < 0 ? (- i) - 1 : i;
    }
    
    @Override
    public boolean add(final E e) {
        super.add(findIndex(e), e);
        return true;
    }
    
    @Override
    public void add(final int index, final E e) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public E set(final int index, final E e) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean addAll(final int index, final Collection<? extends E> coll) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean addAll(final int index, final MyArrayList<? extends E> list) {
        throw new UnsupportedOperationException();
    }
    
    @SuppressWarnings("unchecked")
    private boolean addAll(final Collection<? extends E> coll, final boolean sorted) {
        final int collSize = coll.size();
        if (collSize == 0) {
            return false;
        }
        final Iterator<? extends E> iter;
        if (sorted) {
            iter = coll.iterator();
        } else {
            final List<E> collList;
            if (coll instanceof List<?>) {
                collList = (List<E>) coll;
            } else {
                collList = new ArrayList<>(coll);
            }
            // for faster merge sort later
            // this probably uses timsort though
            Collections.sort(collList);
            iter = collList.iterator();
        }
        int newSize;
        if ((newSize = size + collSize) > data.length) {
            ensureCapacity(newSize);
        }
        E next = iter.next();
        // in-place, single merge sort
        for (int i = 0; i < size && iter.hasNext(); i++) {
            final int compare = data(i).compareTo(next);
            if (compare > 0) {
                super.add(i, next);
                next = iter.next();
            }
        }
        int i = size;
        while (iter.hasNext()) {
            data[i++] = iter.next();
        }
        size = i;
        return true;
    }
    
    public boolean addAll(final OrderedArrayList<? extends E> list) {
        return addAll(list, true);
    }
    
    @Override
    public boolean addAll(final Collection<? extends E> coll) {
        return addAll(coll, false);
    }
    
    private int anyIndexOf(final Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        if (!(o instanceof Comparable<?>)) {
            return -1;
        }
        @SuppressWarnings("unchecked")
        final E e = (E) o;
        final int i = binarySearch(e);
        return i < 0 ? -1 : i;
    }
    
    @Override
    public int indexOf(final Object o) {
        int i = anyIndexOf(o);
        if (i == -1 || i == 0) {
            return i;
        }
        for (E e = data(--i); o.equals(e) && i >= 0; e = data(--i)) {}
        return i + 1;
    }
    
    @Override
    public int lastIndexOf(final Object o) {
        int i = anyIndexOf(o);
        if (i == -1 || i == size - 1) {
            return i;
        }
        for (E e = data(++i); o.equals(e) && i < size; e = data(++i)) {}
        return i - 1;
    }
    
    public static void main(final String[] args) {
        final OrderedArrayList<Integer> list = new OrderedArrayList<>();
        list.addAll(Arrays.asList(3, 1, 5, 8, 3, 9, 100, 100, 100));
        list.add(5);
        list.add(3);
        list.add(10);
        list.add(-20);
        list.add(100);
        //list.addAll(new java.util.Random().ints(10000000).boxed().collect(Collectors.toList()));
        new java.util.Random().ints(100).forEach(list::add);
        //System.out.println(list);
        System.out.println(list.indexOf(100));
        System.out.println(list.lastIndexOf(100));
        final List<Integer> copy1 = new ArrayList<>(list);
        copy1.sort(null);
        final List<Integer> copy2 = new ArrayList<>(list);
        System.out.println(list);
        System.out.println(copy1.equals(copy2));
        System.out.println(list.indexOf(Integer.MAX_VALUE));
    }
    
}
