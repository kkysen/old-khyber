package sen.khyber.crypto.padders;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class PKCS7_BytePadder extends BytePadder {
    
    public PKCS7_BytePadder() {}
    
    @Override
    public byte getFillerByte(final int bytesToAdd) {
        return (byte) bytesToAdd;
    }
    
}
