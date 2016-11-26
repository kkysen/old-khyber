package sen.khyber.crypto.ciphers.substitution.polyalphabetic;

import sen.khyber.crypto.ByteArrays;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class AutokeyCipher extends VigenereCipher {
    
    public AutokeyCipher(final byte[] key) {
        super(key);
    }
    
    public AutokeyCipher(final String key) {
        super(key);
    }
    
    @Override
    public byte[] encrypt(final byte[] plainbytes) {
        final byte[] keyStream = ByteArrays.concat(key, plainbytes);
        final byte[] cipherbytes = new byte[plainbytes.length];
        for (int i = 0; i < cipherbytes.length; i++) {
            cipherbytes[i] = rot(plainbytes[i], keyStream[i], true);
        }
        return cipherbytes;
    }
    
    @Override
    public byte[] decrypt(final byte[] cipherbytes) {
        final byte[] keyStream = ByteArrays.concat(key, cipherbytes);
        final byte[] plainbytes = new byte[cipherbytes.length];
        for (int i = 0; i < plainbytes.length; i++) {
            plainbytes[i] = rot(cipherbytes[i], keyStream[i], false);
            keyStream[i + key.length] = plainbytes[i];
        }
        return plainbytes;
    }
    
}
