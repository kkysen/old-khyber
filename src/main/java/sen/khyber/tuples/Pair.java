package sen.khyber.tuples;

import lombok.Data;

/**
 * 
 * 
 * @author Khyber Sen
 * @param <A> type of first element
 * @param <B> type of second element
 */
@Data
public class Pair<A, B> {
    
    private A first;
    private B second;
    
    public Pair(final A a, final B b) {
        first = a;
        second = b;
    }
    
}