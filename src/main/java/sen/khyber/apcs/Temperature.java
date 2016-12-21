package sen.khyber.apcs;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class Temperature {
    
    public static double CtoF(final double t) {
        return t * 9 / 5 + 32;
    }
    
    public static double FtoC(final double t) {
        return (t - 32) * 5 / 9;
    }
    
    public static void main(final String[] args) {
        System.out.println(CtoF(-40));
        System.out.println(CtoF(100));
        System.out.println(FtoC(212));
        System.out.println(FtoC(32));
    }
    
}
