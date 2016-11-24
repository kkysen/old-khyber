package sen.khyber.util;


public class Parallel {
    
    private static final Runtime runtime = Runtime.getRuntime();
    
    public static int numCores() {
        return runtime.availableProcessors();
    }
    
    public static long maxMemory() {
        return runtime.maxMemory();
    }
    
    public static long totalMemory() {
        return runtime.totalMemory();
    }
    
    public static long freeMemory() {
        return runtime.freeMemory();
    }
    
}
