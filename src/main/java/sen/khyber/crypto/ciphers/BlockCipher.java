package sen.khyber.crypto.ciphers;

import sen.khyber.crypto.ByteArrays;
import sen.khyber.crypto.modes.Mode;

public abstract class BlockCipher implements Cipher {

    private Mode mode;
    
    public BlockCipher(Mode mode) {
        this.mode = mode;
    }
    
    public abstract byte[] encryptBlock(byte[] plainBlock);
    
    public abstract byte[] decryptBlock(byte[] cipherBlock);
    
    @Override
    public byte[] encrypt(byte[] plainbytes) {
        Iterable<byte[]> cipherBlocks = mode.encrypt(ByteArrays.toBlocks(plainbytes, getBlockSize()), this::encryptBlock);
        return ByteArrays.concat(cipherBlocks);
    }
    
    @Override
    public byte[] decrypt(byte[] cipherbytes) {
        Iterable<byte[]> plainBlocks = mode.decrypt(ByteArrays.toBlocks(cipherbytes, getBlockSize()), this::decryptBlock);
        return ByteArrays.concat(plainBlocks);
    }

}
