package sen.khyber.crypto.padders;

import java.util.Random;

public class ISO_10126_BytePadder extends BytePadder {
    
    private static final Random random = new Random();
    
    public ISO_10126_BytePadder() {}
    
    @Override
    public byte getFillerByte(int bytesToAdd) {
        return (byte) random.nextInt();
    }

}
