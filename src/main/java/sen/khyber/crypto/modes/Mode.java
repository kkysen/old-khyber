package sen.khyber.crypto.modes;

import java.util.List;
import java.util.function.Function;

/**
 * 
 * 
 * @author Khyber Sen
 */
public interface Mode {
    
    public Iterable<byte[]> encrypt(List<byte[]> plainBlocks, Function<byte[], byte[]> encryptor);
    
    public Iterable<byte[]> decrypt(List<byte[]> cipherBlocks, Function<byte[], byte[]> decryptor);
    
}
