package sen.khyber.crypto.padders;

/**
 * 
 * 
 * @author Khyber Sen
 */
public interface Padder {
    
    public byte[] pad(byte[] bytes, int blockSize);
    
    public byte[] unpad(byte[] paddedBytes, int blockSize);
    
}