package sen.khyber.crypto.ciphers.substitution.polyalphabetic;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class VigenereCipher implements PolyalphabeticCipher {
    
    private static final Charset charset = StandardCharsets.UTF_8;
    
    protected byte[] key;
    
    public VigenereCipher(final byte[] key) {
        this.key = key;
    }
    
    public VigenereCipher(final String key) {
        this(key.getBytes(charset));
    }
    
    protected byte rot(byte b, final byte rotNum, final boolean forEncryption) {
        if (forEncryption) {
            b += rotNum;
        } else {
            b -= rotNum;
        }
        return b;
    }
    
    private byte[] crypt(final byte[] bytes, final boolean forEncryption) {
        final byte[] newBytes = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            newBytes[i] = rot(bytes[i], key[i % key.length], forEncryption);
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
    
}
