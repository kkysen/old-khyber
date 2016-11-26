package sen.khyber.crypto.padders;

import sen.khyber.crypto.ByteArrays;

/**
 * 
 * 
 * @author Khyber Sen
 */
public abstract class BytePadder implements Padder {
    
    public BytePadder() {}
    
    public abstract byte getFillerByte(int bytesToAdd);
    
    @Override
    public byte[] pad(final byte[] bytes, final int blockSize) {
        int bytesToAdd = blockSize - bytes.length % blockSize;
        if (bytesToAdd == 0) {
            bytesToAdd = blockSize;
        }
        final byte[] padding = new byte[bytesToAdd];
        for (int i = 0; i < padding.length - 1; i++) {
            padding[i] = getFillerByte(bytesToAdd);
        }
        padding[padding.length - 1] = (byte) bytesToAdd;
        return ByteArrays.concat(bytes, padding);
    }
    
    @Override
    public byte[] unpad(final byte[] paddedBytes, final int blockSize) {
        final int bytesToRemove = paddedBytes[paddedBytes.length - 1];
        return ByteArrays.slice(paddedBytes, 0, -bytesToRemove);
    }
    
}
