package sen.khyber.crypto.padders;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class OneZeroBitPadder extends BitPadder {
    
    public OneZeroBitPadder() {}
    
    @Override
    public byte getFirstByte() {
        return (byte) 128;
    }
    
}
