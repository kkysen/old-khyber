package sen.khyber.crypto.padders;

public class ZeroBitPadder extends BitPadder {
    
    public ZeroBitPadder() {}
    
    @Override
    public byte getFirstByte() {
        return (byte) 0;
    }

}
