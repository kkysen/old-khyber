package sen.khyber.games.lexical;

import java.util.HashMap;
import java.util.function.Function;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class PointValueFunctionMap extends PointValueMap {
    
    private Function<Integer, Integer> func;
    
    private PointValueFunctionMap() {}
    
    public PointValueFunctionMap(final Function<Integer, Integer> func, final int minLength) {
        this.func = func;
        this.minLength = minLength;
    }
    
    @Override
    public int getValidPointValue(final int length) {
        return map.computeIfAbsent(length, func);
    }
    
    @Override
    public PointValueFunctionMap clone() {
        final PointValueFunctionMap copy = new PointValueFunctionMap();
        copy.map = new HashMap<>(map);
        copy.func = func;
        copy.minLength = minLength;
        return copy;
    }
    
}
