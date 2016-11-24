package sen.khyber.crypto.ciphers.substitution.monoalphabetic;

public class CaesarCipher implements MonoalphabeticCipher {
    
    private byte rotNum;
    
    public CaesarCipher(byte rotNum) {
        this.rotNum = rotNum;
    }
    
    public CaesarCipher(int i) {
        this((byte) i);
    }
    
    private byte rot(byte b, boolean forward) {
        if (forward) {
            b += rotNum;
        } else {
            b -= rotNum;
        }
        return b;
    }
    
    private byte[] crypt(byte[] bytes, boolean forEncryption) {
        byte[] newBytes = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            newBytes[i] = rot(bytes[i], forEncryption);
        }
        return newBytes;
    }

    @Override
    public byte[] encrypt(byte[] plainbytes) {
        return crypt(plainbytes, true);
    }

    @Override
    public byte[] decrypt(byte[] cipherbytes) {
        return crypt(cipherbytes, false);
    }

}
