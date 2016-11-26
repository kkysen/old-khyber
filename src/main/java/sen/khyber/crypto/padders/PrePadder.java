package sen.khyber.crypto.padders;

import java.nio.ByteBuffer;
import java.util.Arrays;

import sen.khyber.crypto.ByteArrays;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class PrePadder implements Padder {
    
    public PrePadder() {}
    
    @Override
    public byte[] pad(final byte[] bytes, final int blockSize) {
        final byte[] length = ByteBuffer.allocate(4).putInt(bytes.length).array();
        // copies bytes.length (as a byte[]) to the end of a new byte[blockSize]
        final byte[] prePadding = ByteArrays.copyToEnd(blockSize, length);
        // fills the end padding with 0s
        final byte[] endPadding = ByteArrays.fill(0, bytes.length % blockSize);
        // concats the pre and end padding to the bytes
        return ByteArrays.concat(prePadding, bytes, endPadding);
    }
    
    @Override
    public byte[] unpad(final byte[] paddedBytes, final int blockSize) {
        // retrieved prePadding
        final byte[] prePadding = Arrays.copyOfRange(paddedBytes, 0, blockSize);
        // reads the prePadding bytes into a ByteBuffer and gets the intValue
        final int length = ByteBuffer.wrap(prePadding).getInt();
        // removes the padding according the found length
        return Arrays.copyOfRange(paddedBytes, blockSize, length + blockSize);
    }
    
}
