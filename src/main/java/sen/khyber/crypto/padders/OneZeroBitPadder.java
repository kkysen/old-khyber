package sen.khyber.crypto.padders;

public class OneZeroBitPadder extends BitPadder {
    
    public OneZeroBitPadder() {}
    
    @Override
    public byte getFirstByte() {
        return (byte) 128;
    }

}
