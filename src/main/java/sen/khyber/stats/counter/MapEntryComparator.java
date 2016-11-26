package sen.khyber.stats.counter;

import java.util.Comparator;
import java.util.Map;

/**
 * 
 * 
 * @author Khyber Sen
 * @param <T> key type
 * @param <N> a Comparable number
 */
public class MapEntryComparator<T, N extends Comparable<? super N>>
        implements Comparator<Map.Entry<T, N>> {
    
    public MapEntryComparator() {}
    
    @Override
    @SuppressWarnings("unchecked")
    public int compare(final Map.Entry<T, N> entry1, final Map.Entry<T, N> entry2) {
        final int cmp = entry2.getValue().compareTo(entry1.getValue());
        if (cmp != 0) {
            return cmp;
        } else {
            final T t1 = entry1.getKey();
            final T t2 = entry2.getKey();
            if (t1 instanceof Comparable) {
                // Comparable<T> tmpT1 = Comparable<T>.getClass().cast(t1);
                // unchecked warning, but should be safe
                final Comparable<T> tmpT1 = (Comparable<T>) t1;
                return tmpT1.compareTo(t2);
            }
            if (t1.equals(t2)) {
                return 0;
            }
            return -1;
        }
    }
    
}
