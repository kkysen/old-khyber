package sen.khyber.crypto.ciphers;

/**
 * 
 * 
 * @author Khyber Sen
 */
public interface Cipher {
    
    public default int getBlockSize() {
        return 0;
    }
    
    public byte[] encrypt(byte[] plainbytes);
    
    public byte[] decrypt(byte[] cipherbytes);
    
}
