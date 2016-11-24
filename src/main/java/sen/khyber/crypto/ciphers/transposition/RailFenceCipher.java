package sen.khyber.crypto.ciphers.transposition;

import java.util.LinkedHashSet;
import java.util.Set;

public class RailFenceCipher implements TranspositionCipher {
    
    private int numRows;
    
    public RailFenceCipher(int numRows) {
        this.numRows = numRows;
    }
    
    private Integer[] getIndices(int length) {
        Set<Integer> indices = new LinkedHashSet<>();
        int period = 2 * (numRows - 1);
        
        @SuppressWarnings("unused")
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
    public byte[] encrypt(byte[] plainbytes) {
        byte[] cipherbytes = new byte[plainbytes.length];
        Integer[] indices = getIndices(plainbytes.length);
        for (int i = 0; i < cipherbytes.length; i++) {
            cipherbytes[i] = plainbytes[indices[i]];
        }
        return cipherbytes;
    }

    @Override
    public byte[] decrypt(byte[] cipherbytes) {
        byte[] plainbytes = new byte[cipherbytes.length];
        Integer[] indices = getIndices(cipherbytes.length);
        for (int i = 0; i < plainbytes.length; i++) {
            plainbytes[indices[i]] = cipherbytes[i];
        }
        return plainbytes;
    }

}
