package sen.khyber.crypto.padders;

public class ANSI_X923_BytePadder extends BytePadder {
    
    public ANSI_X923_BytePadder() {}
    
    @Override
    public byte getFillerByte(int bytesToAdd) {
        return (byte) 0;
    }

}
