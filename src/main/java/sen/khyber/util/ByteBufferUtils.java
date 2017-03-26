package sen.khyber.util;

import java.nio.ByteBuffer;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class ByteBufferUtils {
    
    public static String getShortString(final ByteBuffer in) {
        final byte[] bytes = new byte[in.getShort()];
        in.get(bytes);
        return new String(bytes);
    }
    
    public static void putShortString(final ByteBuffer out, final String s) {
        final byte[] bytes = s.getBytes();
        out.putShort((short) bytes.length);
        out.put(bytes);
        if (s.length() != bytes.length) {
            System.out.println(s);
            for (final char c : s.toCharArray()) {
                System.out.println(c + "" + (int) c);
            }
            final int x = 1;
        }
    }
    
    public static void putShortBytes(final ByteBuffer out, final byte[] bytes) {
        out.putShort((short) bytes.length);
        out.put(bytes);
    }
    
}
