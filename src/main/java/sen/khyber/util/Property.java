package sen.khyber.util;

import java.util.Comparator;
import java.util.function.Function;

/**
 * 
 * 
 * @author Khyber Sen
 * @param <T> type of Object with this property
 * @param <P> type of property itself
 */
public interface Property<T, P> {
    
    public Function<T, P> getGetter();
    
    public Comparator<T> getOrder();
    
}