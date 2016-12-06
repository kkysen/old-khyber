package sen.khyber.misc;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Misc {
    
    public static void main(final String[] args) {
        final List<Integer> list = IntStream.range(0, 10).boxed().collect(Collectors.toList());
        Collections.shuffle(list);
    }
    
}
