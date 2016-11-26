package sen.khyber.crypto.ciphers.transposition;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class RailFenceCipher implements TranspositionCipher {
    
    private final int numRows;
    
    public RailFenceCipher(final int numRows) {
        this.numRows = numRows;
    }
    
    private Integer[] getIndices(final int length) {
        final Set<Integer> indices = new LinkedHashSet<>();
        final int period = 2 * (numRows - 1);
        
        @SuppressWarnings("unused")
        final
        int stepTwo = 0;
        
        for (int i = 0, step = period; i < numRows; i++) {
            int index = i;
            while (index < length) {
                indices.add(index);
                index += step;
                if (index < length) {
                    indices.add(index);
                    index += period - step;
                }
            }
            step -= 2;
        }
        return indices.toArray(new Integer[indices.size()]);
    }
    
    @Override
    public byte[] encrypt(final byte[] plainbytes) {
        final byte[] cipherbytes = new byte[plainbytes.length];
        final Integer[] indices = getIndices(plainbytes.length);
        for (int i = 0; i < cipherbytes.length; i++) {
            cipherbytes[i] = plainbytes[indices[i]];
        }
        return cipherbytes;
    }
    
    @Override
    public byte[] decrypt(final byte[] cipherbytes) {
        final byte[] plainbytes = new byte[cipherbytes.length];
        final Integer[] indices = getIndices(cipherbytes.length);
        for (int i = 0; i < plainbytes.length; i++) {
            plainbytes[indices[i]] = cipherbytes[i];
        }
        return plainbytes;
    }
    
}
