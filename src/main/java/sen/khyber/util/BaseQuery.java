package sen.khyber.util;

import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 
 * 
 * @author Khyber Sen
 */
public interface BaseQuery<T, Q extends BaseQuery<T, Q>> extends Query<T> {
    
    @Override
    public Q parallel();
    
    @Override
    public Q sequential();
    
    @Override
    public Q distinct();
    
    @Override
    public default Q sorted() {
        return sorted(null);
    }
    
    @Override
    public Q sorted(Comparator<? super T> comparator);
    
    @Override
    public Q filter(Predicate<? super T> predicate);
    
    @Override
    public default Q filterOut(final Predicate<? super T> predicate) {
        return filter(predicate.negate());
    }
    
    @Override
    public Q peek(final Consumer<? super T> action);
    
    @Override
    public Q limit(final long maxSize);
    
    @Override
    public Q skip(long n);
    
}
