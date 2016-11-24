package sen.khyber.stats.counter;

import java.util.Comparator;
import java.util.Map;

public class MapEntryComparator<T, N extends Comparable<? super N>> implements Comparator<Map.Entry<T, N>> {
    
    public MapEntryComparator() {}
    
    @SuppressWarnings("unchecked")
    public int compare(Map.Entry<T, N> entry1, Map.Entry<T, N> entry2) {
        int cmp = entry2.getValue().compareTo(entry1.getValue());
        if (cmp != 0) {
            return cmp;
        } else {
            T t1 = entry1.getKey();
            T t2 = entry2.getKey();
            if (t1 instanceof Comparable) {
                // Comparable<T> tmpT1 = Comparable<T>.getClass().cast(t1);
                // unchecked warning, but should be safe
                Comparable<T> tmpT1 = (Comparable<T>) t1;
                return tmpT1.compareTo(t2);
            }
            if (t1.equals(t2)) return 0;
            return -1;
        }
    }
    
}
