package sen.khyber.apcs.superArray;

import java.util.Collection;

/**
 * more closely implements our in-class SuperIntArray,
 * because it extends MyArrayList<Integer>
 * The one method that isn't the same after autoboxing is toArray(),
 * because it returns an Integer[] instead of an int[],
 * and due to Java's insufficient autoboxing, generic type erasure,
 * and lack of true value types, it is impossible to write.
 * Therefore, I wrote a toIntArray() method that returns an int[]
 * 
 * @see #toIntArray()
 * 
 * @author Khyber Sen
 */
public class SuperIntArray extends MyArrayList<Integer> {
    
    public SuperIntArray() {}
    
    public SuperIntArray(final int capacity) {
        super(capacity);
    }
    
    public SuperIntArray(final Collection<Integer> coll) {
        super(coll);
    }
    
    public SuperIntArray(final MyArrayList<Integer> other) {
        super(other);
    }
    
    public int[] toIntArray() {
        return parallelStream().mapToInt(Integer::valueOf).toArray();
    }
    
}