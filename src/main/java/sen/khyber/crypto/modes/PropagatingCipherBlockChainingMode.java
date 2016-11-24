package sen.khyber.crypto.modes;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import sen.khyber.crypto.ByteArrays;

public class PropagatingCipherBlockChainingMode implements Mode {
    
    private static final Random random = new Random();
    
    public PropagatingCipherBlockChainingMode() {}
    
    private static byte[][] prepend(byte[][] byteArrays, byte[] byteArray) {
        byte[][] newByteArrays = new byte[byteArrays.length + 1][];
        newByteArrays[0] = byteArray;
        for (int i = 0; i < byteArrays.length; i++) {
            newByteArrays[i+1] = byteArrays[i];
        }
        return newByteArrays;
    }
    
    private byte[][] encrypt(byte[][] plainBlocks, Function<byte[], byte[]> encryptor) {
        int blockSize = plainBlocks[0].length;
        plainBlocks = prepend(plainBlocks, new byte[blockSize]); // empty (0) IV
        byte[][] cipherBlocks = new byte[plainBlocks.length + 1][];
        byte[] IV = new byte[plainBlocks[0].length];
        random.nextBytes(IV);
        cipherBlocks[0] = IV;
        for (int i = 1; i < cipherBlocks.length; i++) {
            byte[] plainBlock = plainBlocks[i];
            ByteArrays.xor(plainBlock, plainBlocks[i-1]);
            ByteArrays.xor(plainBlock, cipherBlocks[i-1]);
            cipherBlocks[i] = encryptor.apply(plainBlock);
        }
        return cipherBlocks;
    }

    private byte[][] decrypt(byte[][] cipherBlocks, Function<byte[], byte[]> decryptor) {
        int blockSize = cipherBlocks[0].length;
        byte[][] plainBlocks = new byte[cipherBlocks.length + 1][];
        plainBlocks[0] = new byte[blockSize];
        for (int i = 1; i < cipherBlocks.length; i++) {
            byte[] plainBlock = decryptor.apply(cipherBlocks[i]);
            ByteArrays.xor(plainBlock, plainBlocks[i-1]);
            ByteArrays.xor(plainBlock, cipherBlocks[i-1]);
            plainBlocks[i] = plainBlock;
        }
        return Arrays.copyOfRange(plainBlocks, 1, plainBlocks.length); // remove empty (0) IV
    }
    
    @Override
    public Iterable<byte[]> encrypt(List<byte[]> plainBlocks, Function<byte[], byte[]> encryptor) {
        return Arrays.asList(encrypt(plainBlocks.toArray(new byte[plainBlocks.size()][]), encryptor));
    }

    @Override
    public Iterable<byte[]> decrypt(List<byte[]> cipherBlocks, Function<byte[], byte[]> decryptor) {
        return Arrays.asList(decrypt(cipherBlocks.toArray(new byte[cipherBlocks.size()][]), decryptor));
    }

}