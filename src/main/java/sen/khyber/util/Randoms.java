package sen.khyber.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Randoms {
    
    private static final Random random = new Random();
    private static final Charset charset = StandardCharsets.UTF_8;
    
    public static String string(int length) {
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return new String(bytes, charset);
    }
    
}
