package sen.khyber.crypto.ciphers;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class NullCipher implements Cipher {
    
    public NullCipher() {}
    
    @Override
    public byte[] encrypt(final byte[] plainbytes) {
        return plainbytes;
    }
    
    @Override
    public byte[] decrypt(final byte[] cipherbytes) {
        return cipherbytes;
    }
    
}
