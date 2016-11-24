package sen.khyber.crypto.ciphers.substitution.monoalphabetic;

public class AtbashCipher implements MonoalphabeticCipher {
    
    public AtbashCipher() {}
    
    private byte[] crypt(byte[] bytes) {
        byte[] newBytes = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            newBytes[i] = (byte) ~ bytes[i];
        }
        return newBytes;
    }
    
    @Override
    public byte[] encrypt(byte[] plainbytes) {
        return crypt(plainbytes);
    }

    @Override
    public byte[] decrypt(byte[] cipherbytes) {
        return crypt(cipherbytes);
    }

}
