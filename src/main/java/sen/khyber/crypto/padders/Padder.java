package sen.khyber.crypto.padders;

public interface Padder {
    
    public byte[] pad(byte[] bytes, int blockSize);
    
    public byte[] unpad(byte[] paddedBytes, int blockSize);
    
}