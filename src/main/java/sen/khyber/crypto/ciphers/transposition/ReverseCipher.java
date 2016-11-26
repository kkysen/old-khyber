package sen.khyber.crypto.ciphers.transposition;

import sen.khyber.crypto.ByteArrays;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class ReverseCipher implements TranspositionCipher {
    
    public ReverseCipher() {}
    
    @Override
    public byte[] encrypt(final byte[] plainbytes) {
        return ByteArrays.reverse(plainbytes);
    }
    
    @Override
    public byte[] decrypt(final byte[] cipherbytes) {
        return ByteArrays.reverse(cipherbytes);
    }
    
    public static void main(final String[] args) {
        
        final ReverseCipher reverser = new ReverseCipher();
        final byte[] plainIn = "Hello, world".getBytes();
        final byte[] cipherbytes = reverser.encrypt(plainIn);
        final byte[] plainOut = reverser.decrypt(cipherbytes);
        
        System.out.println(new String(plainIn));
        System.out.println();
        
        System.out.println(new String(cipherbytes));
        System.out.println();
        
        System.out.println(new String(plainOut));
        System.out.println();
    }
    
}
