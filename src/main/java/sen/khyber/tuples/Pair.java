package sen.khyber.tuples;

import lombok.Data;

@Data
public class Pair<A, B> {
    
    private A first;
    private B second;
    
    public Pair(A a, B b) {
        first = a;
        second = b;
    }
    
}