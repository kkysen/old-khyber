package sen.khyber.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class AlphabetStream {
    
    private static String get(int n, char start) {
        char[] buf = new char[(int) Math.floor(Math.log(25 * (n + 1)) / Math.log(26))];
        for (int i = buf.length - 1; i >= 0; i--) {
            n--;
            buf[i] = (char) (start + n % 26);
            n /= 26;
        }
        return new String(buf);
    }
    
    public static String getUpper(int n) {
        return get(n, 'A');
    }
    
    private static String getLower(int n) {
        return get(n, 'a');
    }
    
    public static Stream<String> upper(int length) {
        return IntStream.range(1, length + 1).parallel().mapToObj(AlphabetStream::getUpper);
    }
    
    public static Stream<String> lower(int length) {
        return IntStream.range(1, length + 1).parallel().mapToObj(AlphabetStream::getLower);
    }
    
    public static List<String> asUpperList(int length) {
        List<String> alphabet = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            alphabet.add(getUpper(i));
        }
        return alphabet;
    }
    
    public static List<String> asLowerList(int length) {
        List<String> alphabet = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            alphabet.add(getLower(i));
        }
        return alphabet;
    }
    
    public static void main(String[] args) throws Exception {
        List<String> alphabet = asUpperList(1000);
        for (int i = 0; i < alphabet.size(); i++) {
            System.out.print(alphabet.get(i) + " ");
            if (i % 30 == 0) {
                System.out.println();
            }
        }
    }
    
}
