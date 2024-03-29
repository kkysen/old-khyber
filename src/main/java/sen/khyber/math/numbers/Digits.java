package sen.khyber.math.numbers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class Digits {
    
    public static void toSet(int i, final int base, final Set<Integer> digits) {
        while (i > 0) {
            digits.add(i % base);
            i /= base;
        }
    }
    
    public static void toSet(long L, final int base, final Set<Integer> digits) {
        while (L > 0) {
            digits.add((int) L % base);
            L /= base;
        }
    }
    
    public static void toSet(final int i, final Set<Integer> digits) {
        toSet(i, 10, digits);
    }
    
    public static void toSet(final long L, final Set<Integer> digits) {
        toSet(L, 10, digits);
    }
    
    public static Set<Integer> toSet(final int... ints) {
        final Set<Integer> digits = new HashSet<>();
        for (final int i : ints) {
            toSet(i, digits);
        }
        return digits;
    }
    
    public static Set<Integer> toSet(final long... longs) {
        final Set<Integer> digits = new HashSet<>();
        for (final long L : longs) {
            toSet(L, digits);
        }
        return digits;
    }
    
    public static List<Integer> toListBase(int i, final int base) {
        final List<Integer> digits = new ArrayList<>();
        while (i > 0) {
            digits.add(i % base);
            i /= base;
        }
        Collections.reverse(digits);
        return digits;
    }
    
    public static List<Integer> toListBase(long L, final int base) {
        final List<Integer> digits = new ArrayList<>();
        while (L > 0) {
            digits.add((int) L % base);
            L /= base;
        }
        Collections.reverse(digits);
        return digits;
    }
    
    public static List<Integer> toList(final int i) {
        return toListBase(i, 10);
    }
    
    public static List<Integer> toList(final long L) {
        return toListBase(L, 10);
    }
    
    public static Stream<List<Integer>> toList(final int... ints) {
        return Arrays.stream(ints).mapToObj(Digits::toList);
    }
    
    public static Stream<List<Integer>> toList(final long... longs) {
        return Arrays.stream(longs).mapToObj(Digits::toList);
    }
    
    /*public static String encode(List<Integer> digits, Map<Integer, Character> encoding, char defaultChar) {
        StringBuilder sb = new StringBuilder();
        digits.parallelStream().map(digit -> encoding.getOrDefault(digit, defaultChar))
                               .forEach(sb::append);
        return sb.toString();
    }*/
    
    public static void main(final String[] args) throws Exception {
        System.out
                .println(toList(125, 328, 13245, 67634, 24356, 6343).collect(Collectors.toList()));
        System.out.println(toList(0));
        
        /*int numFields = 1000;
        
        Map<Integer, Character> encoding = new HashMap<>();
        for (int i = 0; i < 26; i++) {
            encoding.put(i, (char) ('A' + i));
        }
        System.out.println(encode(toListBase(234365, 26), encoding, '?'));
        List<String> fields = new ArrayList<>();
        for (int i = 0; i < numFields; i++) {
            String encoded = encode(toList(i), encoding, '?');
            System.out.println(i + toList(i).toString() + ": " + encoded);
            fields.add("public int " + encoded);
        }
        List<String> values = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < numFields; i++) {
            values.add(String.valueOf(random.nextInt()));
        }
        String csv = "public String name," + String.join(",", fields) + "\nTest," + String.join(",", values);
        MyFiles.write(Paths.get("testForEnum.csv"), csv);*/
        
    }
    
}
