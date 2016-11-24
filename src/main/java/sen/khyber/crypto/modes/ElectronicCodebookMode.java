package sen.khyber.crypto.modes;

import java.util.List;
import java.util.function.Function;

public class ElectronicCodebookMode implements Mode {
    
    public ElectronicCodebookMode() {}

    @Override
    public Iterable<byte[]> encrypt(List<byte[]> plainBlocks, Function<byte[], byte[]> encryptor) {
        return plainBlocks.parallelStream().map(encryptor)::iterator;
    }

    @Override
    public Iterable<byte[]> decrypt(List<byte[]> cipherBlocks, Function<byte[], byte[]> decryptor) {
        return cipherBlocks.parallelStream().map(decryptor)::iterator;
    }

}
