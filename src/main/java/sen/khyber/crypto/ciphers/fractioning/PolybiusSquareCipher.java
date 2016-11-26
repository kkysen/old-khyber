package sen.khyber.crypto.ciphers.fractioning;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.Set;

import sen.khyber.crypto.ByteArrays;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class PolybiusSquareCipher implements FractioningCipher {
    
    private static final Charset charset = StandardCharsets.UTF_8;
    
    private final byte[][] key;
    
    public PolybiusSquareCipher(final byte[] initKey) {
        final Set<Byte> keySet = new LinkedHashSet<>();
        for (final byte b : initKey) {
            keySet.add(b);
        }
        final byte[] tempKey = new byte[256];
        int i = 0;
        for (final byte b : keySet) {
            tempKey[i++] = b;
        }
        for (byte b = -128; b < 128; b++) {
            if (!keySet.contains(b)) {
                tempKey[i++] = b;
            }
        }
        key = ByteArrays.splitByLength(tempKey, 16);
    }
    
    public PolybiusSquareCipher(final String key) {
        this(key.getBytes(charset));
    }
    
    private byte hexToByte(final byte[] hexes) {
        return key[hexes[0]][hexes[1]];
        //return (byte) (hexes[0] * 16 + hexes[1]);
    }
    
    private byte[] byteToHex(final byte b) {
        for (int i = 0; i < key.length; i++) {
            for (int j = 0; j < key[i].length; j++) {
                if (b == key[i][j]) {
                    return new byte[] {(byte) i, (byte) j};
                }
            }
        }
        // Eclipse giving me trouble
        // but the above loop will always return a value
        // because it checks b against all possible bytes
        return new byte[2];
        //int i = ((int) b) + 128;
        //return new byte[] {(byte) (i / 16), (byte) (i % 16)};
    }
    
    @Override
    public byte[] encrypt(final byte[] plainbytes) {
        final byte[] cipherbytes = new byte[plainbytes.length * 2];
        for (int i = 0; i < cipherbytes.length; i += 2) {
            final byte[] hexes = byteToHex(plainbytes[i / 2]);
            cipherbytes[i] = hexes[0];
            cipherbytes[i + 1] = hexes[1];
        }
        return cipherbytes;
    }
    
    @Override
    public byte[] decrypt(final byte[] cipherbytes) {
        final byte[] plainbytes = new byte[cipherbytes.length / 2];
        for (int i = 0; i < cipherbytes.length; i += 2) {
            final byte[] hexes = new byte[] {cipherbytes[i], cipherbytes[i + 1]};
            plainbytes[i / 2] = hexToByte(hexes);
        }
        return plainbytes;
    }
    
}
