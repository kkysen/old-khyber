package sen.khyber.crypto.ciphers;


public class NullCipher implements Cipher {
    
    public NullCipher() {}

    @Override
    public byte[] encrypt(byte[] plainbytes) {
        return plainbytes;
    }

    @Override
    public byte[] decrypt(byte[] cipherbytes) {
        return cipherbytes;
    }

}
