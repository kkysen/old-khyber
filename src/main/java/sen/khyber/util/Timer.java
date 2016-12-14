package sen.khyber.util;


/**
 * 
 * 
 * @author Khyber Sen
 */
public class Timer {
    
    private static long START_TIME = System.nanoTime();
    
    public static void start() {
        START_TIME = System.nanoTime();
    }
    
    public static long time() {
        return System.nanoTime() - START_TIME;
    }
    
    public static String timeString() {
        return "time: " + time() / 1_000_000.0 + " seconds";
    }
    
    public static void printlnTime() {
        System.out.println(timeString());
    }
    
    public static void printlnTime(final String message) {
        System.out.println(message + ": " + timeString());
    }
    
    public static void printTime() {
        System.out.print(timeString());
    }
    
}
