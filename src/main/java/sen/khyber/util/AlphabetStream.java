package sen.khyber.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class AlphabetStream {
    
    private static String get(int n, final char start) {
        final char[] buf = new char[(int) Math.floor(Math.log(25 * (n + 1)) / Math.log(26))];
        for (int i = buf.length - 1; i >= 0; i--) {
            n--;
            buf[i] = (char) (start + n % 26);
            n /= 26;
        }
        return new String(buf);
    }
    
    public static String getUpper(final int n) {
        return get(n, 'A');
    }
    
    private static String getLower(final int n) {
        return get(n, 'a');
    }
    
    public static Stream<String> upper(final int length) {
        return IntStream.range(1, length + 1).parallel().mapToObj(AlphabetStream::getUpper);
    }
    
    public static Stream<String> lower(final int length) {
        return IntStream.range(1, length + 1).parallel().mapToObj(AlphabetStream::getLower);
    }
    
    public static List<String> asUpperList(final int length) {
        final List<String> alphabet = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            alphabet.add(getUpper(i));
        }
        return alphabet;
    }
    
    public static List<String> asLowerList(final int length) {
        final List<String> alphabet = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            alphabet.add(getLower(i));
        }
        return alphabet;
    }
    
    public static void main(final String[] args) throws Exception {
        final List<String> alphabet = asUpperList(1000);
        for (int i = 0; i < alphabet.size(); i++) {
            System.out.print(alphabet.get(i) + " ");
            if (i % 30 == 0) {
                System.out.println();
            }
        }
    }
    
}
