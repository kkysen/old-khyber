package sen.khyber.crypto.ciphers.substitution.polyalphabetic;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class VigenereCipher implements PolyalphabeticCipher {
    
    private static final Charset charset = StandardCharsets.UTF_8;
    
    protected byte[] key;
    
    public VigenereCipher(byte[] key) {
        this.key = key;
    }
    
    public VigenereCipher(String key) {
        this(key.getBytes(charset));
    }
    
    protected byte rot(byte b, byte rotNum, boolean forEncryption) {
        if (forEncryption) {
            b += rotNum;
        } else {
            b -= rotNum;
        }
        return b;
    }
    
    private byte[] crypt(byte[] bytes, boolean forEncryption) {
        byte[] newBytes = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            newBytes[i] = rot(bytes[i], key[i % key.length], forEncryption);
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

}
