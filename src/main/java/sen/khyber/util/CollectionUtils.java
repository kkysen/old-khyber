package sen.khyber.util;

import java.util.Collection;
import java.util.HashSet;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class CollectionUtils {
    
    public static <T> HashSet<T> toHashSet(final Collection<T> collection) {
        if (collection instanceof HashSet) {
            return (HashSet<T>) collection;
        }
        return new HashSet<>(collection);
    }
    
}
