package sen.khyber.util;


/**
 * 
 * 
 * @author Khyber Sen
 */
public class Timer {
    
    private static final long START_TIME = System.currentTimeMillis();
    
    public static long time() {
        return System.currentTimeMillis() - START_TIME;
    }
    
    public static String timeString() {
        return "time: " + time() / 1000.0;
    }
    
    public static void printlnTime() {
        System.out.println(timeString());
    }
    
    public static void printTime() {
        System.out.print(timeString());
    }
    
}
