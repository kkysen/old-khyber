package sen.khyber.crypto.modes;

import sen.khyber.crypto.ByteArrays;

import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class CipherBlockChainingMode implements Mode {
    
    private static final Random random = new Random();
    
    public CipherBlockChainingMode() {}
    
    @Override
    public Iterable<byte[]> encrypt(final List<byte[]> blocks, final Function<byte[], byte[]> encryptor) {
        final byte[] IV = new byte[blocks.get(0).length];
        random.nextBytes(IV);
        blocks.add(0, IV);
        for (int i = 1; i < blocks.size(); i++) {
            blocks.set(i, encryptor.apply(ByteArrays.xor(blocks.get(i - 1), blocks.get(i))));
        }
        return blocks;
    }
    
    @Override
    public Iterable<byte[]> decrypt(final List<byte[]> cipherBlocks, final Function<byte[], byte[]> decryptor) {
        final List<byte[]> plainBlocks = cipherBlocks.parallelStream().map(decryptor)
                .collect(Collectors.toList());
        for (int i = 1; i < plainBlocks.size(); i++) {
            plainBlocks.set(i, ByteArrays.xor(plainBlocks.get(i), cipherBlocks.get(i - 1)));
        }
        plainBlocks.remove(0);
        return plainBlocks;
    }
    
}
