package sen.khyber.crypto.padders;

import sen.khyber.crypto.ByteArrays;

/**
 * 
 * 
 * @author Khyber Sen
 */
public abstract class BitPadder implements Padder {
    
    public BitPadder() {}
    
    public abstract byte getFirstByte();
    
    @Override
    public byte[] pad(final byte[] bytes, final int blockSize) {
        int bytesToAdd = blockSize - bytes.length % blockSize;
        if (bytesToAdd == 0) {
            bytesToAdd = blockSize;
        }
        final byte[] padding = ByteArrays.fill(0, bytesToAdd);
        padding[0] = getFirstByte();
        return ByteArrays.concat(bytes, padding);
    }
    
    @Override
    public byte[] unpad(final byte[] paddedBytes, final int blockSize) {
        int i = paddedBytes.length - 1;
        while (paddedBytes[i] == (byte) 0) {
            i--;
        }
        if (paddedBytes[i] == getFirstByte()) {
            i--;
        }
        return ByteArrays.slice(paddedBytes, 0, i);
    }
    
}
