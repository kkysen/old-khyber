package sen.khyber.games.lexical;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

/**
 * 
 * 
 * @author Khyber Sen
 */
public abstract class PointValueMap implements Cloneable {
    
    protected @Getter int minLength;
    
    protected Map<Integer, Integer> map = new HashMap<>();
    
    protected PointValueMap() {}
    
    public int getPointValue(final int length) {
        if (length <= minLength) {
            return 0;
        }
        return getValidPointValue(length);
    }
    
    protected abstract int getValidPointValue(final int length);
    
    public boolean put(final int length, final int pointValue) {
        map.put(length, pointValue);
        return true;
    }
    
}
