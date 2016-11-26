package sen.khyber.crypto.modes;

import java.util.List;
import java.util.function.Function;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class ElectronicCodebookMode implements Mode {
    
    public ElectronicCodebookMode() {}
    
    @Override
    public Iterable<byte[]> encrypt(final List<byte[]> plainBlocks, final Function<byte[], byte[]> encryptor) {
        return plainBlocks.parallelStream().map(encryptor)::iterator;
    }
    
    @Override
    public Iterable<byte[]> decrypt(final List<byte[]> cipherBlocks, final Function<byte[], byte[]> decryptor) {
        return cipherBlocks.parallelStream().map(decryptor)::iterator;
    }
    
}
