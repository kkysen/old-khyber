package sen.khyber.crypto.ciphers.substitution.monoalphabetic;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class CaesarCipher implements MonoalphabeticCipher {
    
    private final byte rotNum;
    
    public CaesarCipher(final byte rotNum) {
        this.rotNum = rotNum;
    }
    
    public CaesarCipher(final int i) {
        this((byte) i);
    }
    
    private byte rot(byte b, final boolean forward) {
        if (forward) {
            b += rotNum;
        } else {
            b -= rotNum;
        }
        return b;
    }
    
    private byte[] crypt(final byte[] bytes, final boolean forEncryption) {
        final byte[] newBytes = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            newBytes[i] = rot(bytes[i], forEncryption);
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
