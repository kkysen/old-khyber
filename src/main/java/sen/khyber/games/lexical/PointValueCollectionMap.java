package sen.khyber.games.lexical;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class PointValueCollectionMap extends PointValueMap {
    
    private int defaultPointValue;
    
    private PointValueCollectionMap() {}
    
    public PointValueCollectionMap(final Collection<Map.Entry<Integer, Integer>> coll) {
        minLength = Integer.MAX_VALUE;
        int maxPointValue = Integer.MIN_VALUE;
        for (final Map.Entry<Integer, Integer> entry : coll) {
            final int length = entry.getKey();
            final int pointValue = entry.getValue();
            if (length < minLength) {
                minLength = length;
            }
            if (pointValue > maxPointValue) {
                maxPointValue = pointValue;
            }
            map.put(entry.getKey(), pointValue);
        }
        defaultPointValue = maxPointValue;
    }
    
    public PointValueCollectionMap(final Map<Integer, Integer> map) {
        this.map.putAll(map);
        minLength = Collections.min(map.keySet());
        defaultPointValue = Collections.max(map.values());
    }
    
    private static Map<Integer, Integer> arrToMap(final int[][] arr) {
        final Map<Integer, Integer> map = new HashMap<>(arr.length);
        for (int i = 0; i < arr.length; i++) {
            map.put(arr[i][0], arr[i][0]);
        }
        return map;
    }
    
    public PointValueCollectionMap(final int[][] arr) {
        this(arrToMap(arr));
    }
    
    @Override
    public int getValidPointValue(final int length) {
        return map.getOrDefault(length, defaultPointValue);
    }
    
    @Override
    public PointValueCollectionMap clone() {
        final PointValueCollectionMap copy = new PointValueCollectionMap();
        copy.map = new HashMap<>(map);
        copy.minLength = minLength;
        copy.defaultPointValue = defaultPointValue;
        return copy;
    }
    
}
