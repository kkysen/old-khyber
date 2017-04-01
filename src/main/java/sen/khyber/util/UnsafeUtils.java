package sen.khyber.util;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

/**
 * 
 * 
 * @author Khyber Sen
 */
@SuppressWarnings("restriction")
public final class UnsafeUtils {
    
    private UnsafeUtils() {}
    
    private static Unsafe unsafe;
    static {
        try {
            final Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException
                | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static Unsafe get() {
        return unsafe;
    }
    
    public static long sizeOf(final Object o) {
        return unsafe.getAddress(normalize(unsafe.getInt(o, 8L)) + 24L);
    }
    
    public static long normalize(final int i) {
        return i >= 0 ? i : ~0L >>> 32 & i;
    }
    
    public static void main(final String[] args) {
        final Object o = new Object();
        System.out.println(unsafe.getLong(o, 8L));
        //System.out.println(unsafe.getLong(8L));
    }
    
}
