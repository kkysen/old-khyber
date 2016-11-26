package sen.khyber.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class ParallelTest {
    
    public static void main(final String[] args) {
        final long time = System.currentTimeMillis();
        IntStream.range(0, 100).parallel().forEach(x -> {
            final List<Integer> list = new ArrayList<>(1_000_000);
            for (int i = 0; i < 1_000_000; i++) {
                list.add(i * i);
            }
        });
        System.out.println(System.currentTimeMillis() - time);
    }
    
}
