package sen.khyber.crypto.padders;

import sen.khyber.crypto.ByteArrays;

public abstract class BitPadder implements Padder {
    
    public BitPadder() {}
    
    public abstract byte getFirstByte();
    
    @Override
    public byte[] pad(byte[] bytes, int blockSize) {
        int bytesToAdd = blockSize - (bytes.length % blockSize);
        if (bytesToAdd == 0) {
            bytesToAdd = blockSize;
        }
        byte[] padding = ByteArrays.fill(0, bytesToAdd);
        padding[0] = getFirstByte();
        return ByteArrays.concat(bytes, padding);
    }

    @Override
    public byte[] unpad(byte[] paddedBytes, int blockSize) {
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
