package sen.khyber.crypto.modes;

import sen.khyber.crypto.ByteArrays;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class PropagatingCipherBlockChainingMode implements Mode {
    
    private static final Random random = new Random();
    
    public PropagatingCipherBlockChainingMode() {}
    
    private static byte[][] prepend(final byte[][] byteArrays, final byte[] byteArray) {
        final byte[][] newByteArrays = new byte[byteArrays.length + 1][];
        newByteArrays[0] = byteArray;
        for (int i = 0; i < byteArrays.length; i++) {
            newByteArrays[i + 1] = byteArrays[i];
        }
        return newByteArrays;
    }
    
    private byte[][] encrypt(byte[][] plainBlocks, final Function<byte[], byte[]> encryptor) {
        final int blockSize = plainBlocks[0].length;
        plainBlocks = prepend(plainBlocks, new byte[blockSize]); // empty (0) IV
        final byte[][] cipherBlocks = new byte[plainBlocks.length + 1][];
        final byte[] IV = new byte[plainBlocks[0].length];
        random.nextBytes(IV);
        cipherBlocks[0] = IV;
        for (int i = 1; i < cipherBlocks.length; i++) {
            final byte[] plainBlock = plainBlocks[i];
            ByteArrays.xor(plainBlock, plainBlocks[i - 1]);
            ByteArrays.xor(plainBlock, cipherBlocks[i - 1]);
            cipherBlocks[i] = encryptor.apply(plainBlock);
        }
        return cipherBlocks;
    }
    
    private byte[][] decrypt(final byte[][] cipherBlocks, final Function<byte[], byte[]> decryptor) {
        final int blockSize = cipherBlocks[0].length;
        final byte[][] plainBlocks = new byte[cipherBlocks.length + 1][];
        plainBlocks[0] = new byte[blockSize];
        for (int i = 1; i < cipherBlocks.length; i++) {
            final byte[] plainBlock = decryptor.apply(cipherBlocks[i]);
            ByteArrays.xor(plainBlock, plainBlocks[i - 1]);
            ByteArrays.xor(plainBlock, cipherBlocks[i - 1]);
            plainBlocks[i] = plainBlock;
        }
        return Arrays.copyOfRange(plainBlocks, 1, plainBlocks.length); // remove empty (0) IV
    }
    
    @Override
    public Iterable<byte[]> encrypt(final List<byte[]> plainBlocks, final Function<byte[], byte[]> encryptor) {
        return Arrays
                .asList(encrypt(plainBlocks.toArray(new byte[plainBlocks.size()][]), encryptor));
    }
    
    @Override
    public Iterable<byte[]> decrypt(final List<byte[]> cipherBlocks, final Function<byte[], byte[]> decryptor) {
        return Arrays
                .asList(decrypt(cipherBlocks.toArray(new byte[cipherBlocks.size()][]), decryptor));
    }
    
}
