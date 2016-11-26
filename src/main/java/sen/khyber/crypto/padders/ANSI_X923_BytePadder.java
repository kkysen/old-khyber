package sen.khyber.crypto.padders;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class ANSI_X923_BytePadder extends BytePadder {
    
    public ANSI_X923_BytePadder() {}
    
    @Override
    public byte getFillerByte(final int bytesToAdd) {
        return (byte) 0;
    }
    
}
