package sen.khyber.crypto.ciphers.substitution.polyalphabetic;

import sen.khyber.crypto.ByteArrays;

public class AutokeyCipher extends VigenereCipher {
    
    public AutokeyCipher(byte[] key) {
        super(key);
    }
    
    public AutokeyCipher(String key) {
        super(key);
    }
    
    @Override
    public byte[] encrypt(byte[] plainbytes) {
        byte[] keyStream = ByteArrays.concat(key, plainbytes);
        byte[] cipherbytes = new byte[plainbytes.length];
        for (int i = 0; i < cipherbytes.length; i++) {
            cipherbytes[i] = rot(plainbytes[i], keyStream[i], true);
        }
        return cipherbytes;
    }
    
    @Override
    public byte[] decrypt(byte[] cipherbytes) {
        byte[] keyStream = ByteArrays.concat(key, cipherbytes);
        byte[] plainbytes = new byte[cipherbytes.length];
        for (int i = 0; i < plainbytes.length; i++) {
            plainbytes[i] = rot(cipherbytes[i], keyStream[i], false);
            keyStream[i + key.length] = plainbytes[i];
        }
        return plainbytes;
    }
    
}
