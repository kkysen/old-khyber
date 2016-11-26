package sen.khyber.crypto.ciphers;

import sen.khyber.crypto.ByteArrays;
import sen.khyber.crypto.modes.Mode;

/**
 * 
 * 
 * @author Khyber Sen
 */
public abstract class BlockCipher implements Cipher {
    
    private final Mode mode;
    
    public BlockCipher(final Mode mode) {
        this.mode = mode;
    }
    
    public abstract byte[] encryptBlock(byte[] plainBlock);
    
    public abstract byte[] decryptBlock(byte[] cipherBlock);
    
    @Override
    public byte[] encrypt(final byte[] plainbytes) {
        final Iterable<byte[]> cipherBlocks = mode
                .encrypt(ByteArrays.toBlocks(plainbytes, getBlockSize()), this::encryptBlock);
        return ByteArrays.concat(cipherBlocks);
    }
    
    @Override
    public byte[] decrypt(final byte[] cipherbytes) {
        final Iterable<byte[]> plainBlocks = mode
                .decrypt(ByteArrays.toBlocks(cipherbytes, getBlockSize()), this::decryptBlock);
        return ByteArrays.concat(plainBlocks);
    }
    
}
