package sen.khyber.crypto.ciphers.substitution.monoalphabetic;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class MixedAlphabetCipher implements MonoalphabeticCipher {
    
    private static final Charset charset = StandardCharsets.UTF_8;
    
    private final List<Byte> key = new ArrayList<>(256);
    
    public MixedAlphabetCipher(final byte[] initKey) {
        final Set<Byte> keySet = new LinkedHashSet<>();
        for (final byte b : initKey) {
            keySet.add(b);
        }
        for (final byte b : keySet) {
            key.add(b);
        }
        for (byte b = -128;; b++) {
            if (!keySet.contains(b)) {
                key.add(b);
            }
            // b (being a byte) will loop back to -128 and continue infinitely otherwise
            if (b == 127) {
                break;
            }
        }
    }
    
    public MixedAlphabetCipher(final String key) {
        this(key.getBytes(charset));
    }
    
    public byte cryptByte(final byte b, final boolean forEncryption) {
        if (forEncryption) {
            return key.get(b + 128);
        } else {
            return (byte) (key.indexOf(b) - 128);
        }
    }
    
    private byte[] crypt(final byte[] bytes, final boolean forEncryption) {
        final byte[] newBytes = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            newBytes[i] = cryptByte(bytes[i], forEncryption);
        }
        return newBytes;
    }
    
    @Override
    public byte[] encrypt(final byte[] plainbytes) {
        return crypt(plainbytes, true);
    }
    
    @Override
    public byte[] decrypt(final byte[] cipherbytes) {
        return crypt(cipherbytes, false);
    }
    
    public static void main(final String[] args) {
        
        final MixedAlphabetCipher mixed = new MixedAlphabetCipher("password");
        final byte b = -126;
        final byte c = mixed.cryptByte(b, true);
        final byte d = mixed.cryptByte(c, false);
        System.out.println(b + ", " + c + ", " + d);
        
    }
    
}
