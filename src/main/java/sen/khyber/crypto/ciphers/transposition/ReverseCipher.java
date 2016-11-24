package sen.khyber.crypto.ciphers.transposition;

import sen.khyber.crypto.ByteArrays;

public class ReverseCipher implements TranspositionCipher {

    public ReverseCipher() {}

    @Override
    public byte[] encrypt(byte[] plainbytes) {
        return ByteArrays.reverse(plainbytes);
    }

    @Override
    public byte[] decrypt(byte[] cipherbytes) {
        return ByteArrays.reverse(cipherbytes);
    }
    
    public static void main(String[] args) {
        
        ReverseCipher reverser = new ReverseCipher();
        byte[] plainIn = "Hello, world".getBytes();
        byte[] cipherbytes = reverser.encrypt(plainIn);
        byte[] plainOut = reverser.decrypt(cipherbytes);
        
        System.out.println(new String(plainIn));
        System.out.println();
        
        System.out.println(new String(cipherbytes));
        System.out.println();
        
        System.out.println(new String(plainOut));
        System.out.println();
    }

}
