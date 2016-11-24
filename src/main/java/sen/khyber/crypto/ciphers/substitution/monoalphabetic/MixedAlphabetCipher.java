package sen.khyber.crypto.ciphers.substitution.monoalphabetic;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MixedAlphabetCipher implements MonoalphabeticCipher {
    
    private static final Charset charset = StandardCharsets.UTF_8;
    
    private List<Byte> key = new ArrayList<>(256);
    
    public MixedAlphabetCipher(byte[] initKey) {
        Set<Byte> keySet = new LinkedHashSet<>();
        for (byte b : initKey) {
            keySet.add(b);
        }
        for (byte b : keySet) {
            key.add(b);
        }
        for (byte b = -128;; b++) {
            if (! keySet.contains(b)) {
                key.add(b);
            }
            // b (being a byte) will loop back to -128 and continue infinitely otherwise
            if (b == 127) {
                break;
            }
        }
    }
    
    public MixedAlphabetCipher(String key) {
        this(key.getBytes(charset));
    }
    
    public byte cryptByte(byte b, boolean forEncryption) {
        if (forEncryption) {
            return key.get(((int) b) + 128);
        } else {
            return (byte) (key.indexOf(b) - 128);
        }
    }
    
    private byte[] crypt(byte[] bytes, boolean forEncryption) {
        byte[] newBytes = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            newBytes[i] = cryptByte(bytes[i], forEncryption);
        }
        return newBytes;
    }

    @Override
    public byte[] encrypt(byte[] plainbytes) {
        return crypt(plainbytes, true);
    }

    @Override
    public byte[] decrypt(byte[] cipherbytes) {
        return crypt(cipherbytes, false);
    }
    
    public static void main(String[] args) {
        
        MixedAlphabetCipher mixed = new MixedAlphabetCipher("password");
        byte b = -126;
        byte c = mixed.cryptByte(b, true);
        byte d = mixed.cryptByte(c, false);
        System.out.println(b + ", " + c + ", " + d);
        
    }

}
