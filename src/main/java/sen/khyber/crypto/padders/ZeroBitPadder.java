package sen.khyber.crypto.padders;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class ZeroBitPadder extends BitPadder {
    
    public ZeroBitPadder() {}
    
    @Override
    public byte getFirstByte() {
        return (byte) 0;
    }
    
}
