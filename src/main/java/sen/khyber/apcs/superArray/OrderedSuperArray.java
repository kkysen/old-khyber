package sen.khyber.apcs.superArray;

import java.util.Arrays;
import java.util.Collection;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class OrderedSuperArray extends OrderedArrayList<String> {
    
    public OrderedSuperArray() {}
    
    public OrderedSuperArray(final Collection<? extends String> coll) {
        super(coll);
    }
    
    public OrderedSuperArray(final int initialCapacity) {
        super(initialCapacity);
    }
    
    public OrderedSuperArray(final MyArrayList<? extends String> list) {
        super(list);
    }
    
    public OrderedSuperArray(final String[] arr) {
        super(Arrays.asList(arr));
    }
    
}
