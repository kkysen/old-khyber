package sen.khyber.crypto.padders;

import sen.khyber.crypto.ByteArrays;

public abstract class BytePadder implements Padder {
    
    public BytePadder() {}
    
    public abstract byte getFillerByte(int bytesToAdd);
    
    @Override
    public byte[] pad(byte[] bytes, int blockSize) {
        int bytesToAdd = blockSize - (bytes.length % blockSize);
        if (bytesToAdd == 0) {
            bytesToAdd = blockSize;
        }
        byte[] padding = new byte[bytesToAdd];
        for (int i = 0; i < padding.length - 1; i++) {
            padding[i] = getFillerByte(bytesToAdd);
        }
        padding[padding.length - 1] = (byte) bytesToAdd;
        return ByteArrays.concat(bytes, padding);
    }

    @Override
    public byte[] unpad(byte[] paddedBytes, int blockSize) {
        int bytesToRemove = paddedBytes[paddedBytes.length - 1];
        return ByteArrays.slice(paddedBytes, 0, - bytesToRemove);
    }
    
}
