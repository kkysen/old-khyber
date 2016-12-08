package sen.khyber.apcs.barcode;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 
 * 
 * @author Khyber Sen
 * @author Khinshan Khan
 */
public class BarcodeTest {
    
    /**
     * @author Khyber Sen
     */
    private static void test1() {
        final Random random = new Random();
        final Supplier<Barcode> generator = () -> new Barcode(
                String.valueOf(random.nextInt()).substring(1, 6));
        final List<Barcode> list = Stream.generate(generator).limit(1000).collect(Collectors.toList());
        list.sort(null);
        list.forEach(System.out::println);
    }
    
    /**
     * @author Khinshan Khan
     */
    private static void test2() {
        final Barcode b = new Barcode("08451");
        final Barcode c = new Barcode("99999");
        final Barcode d = new Barcode("11111");
        System.out.println(b); //084518 |||:::|::|::|::|:|:|::::|||::|:|
        System.out.println(b.toString().compareTo("084518 |||:::|::|::|::|:|:|::::|||::|:|")); //0
        System.out.println(b.compareTo(b)); //0
        System.out.println(c.compareTo(b));
        System.out.println(d.compareTo(b));
        /*length
        Barcode e = new Barcode("123456");
        System.out.println(e);
        */
        /*length
        Barcode e = new Barcode("1234");
        System.out.println(e);
        */
        /*type
        Barcode e= new Barcode("12.45");
        System.out.println(e);
        */
    }
    
    public static void main(final String[] args) {
        test1();
        System.out.println("\n\n\n");
        test2();
    }
    
}
