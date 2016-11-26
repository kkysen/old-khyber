package sen.khyber.crypto.ciphers.substitution.monoalphabetic;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class AtbashCipher implements MonoalphabeticCipher {
    
    public AtbashCipher() {}
    
    private byte[] crypt(final byte[] bytes) {
        final byte[] newBytes = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            newBytes[i] = (byte) ~bytes[i];
        }
        return newBytes;
    }
    
    @Override
    public byte[] encrypt(final byte[] plainbytes) {
        return crypt(plainbytes);
    }
    
    @Override
    public byte[] decrypt(final byte[] cipherbytes) {
        return crypt(cipherbytes);
    }
    
}
