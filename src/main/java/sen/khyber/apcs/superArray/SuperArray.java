package sen.khyber.apcs.superArray;

import java.util.Collection;


public class SuperArray extends MyArrayList<String> {
    
    public SuperArray(final int initialCapacity) {
        super(initialCapacity);
    }
    
    public SuperArray() {}
    
    public SuperArray(final Collection<? extends String> coll) {
        super(coll);
    }
    
    public SuperArray(final MyArrayList<? extends String> list) {
        super(list);
    }
    
}
