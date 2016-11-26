package sen.khyber.crypto.ciphers.transposition;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import sen.khyber.crypto.ciphers.BlockCipher;
import sen.khyber.crypto.modes.ElectronicCodebookMode;
import sen.khyber.crypto.modes.Mode;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class PermutationCipher extends BlockCipher implements TranspositionCipher {
    
    private static final Charset charset = StandardCharsets.UTF_8;
    
    private final byte[] key;
    private final List<Integer> order = new ArrayList<>();
    
    @SuppressWarnings("unused")
    private final int numCols;
    
    private void setOrder() {
        final Map<Byte, Integer> keyMap = new TreeMap<>();
        for (int i = 0; i < key.length; i++) {
            keyMap.put(key[i], i);
        }
        final Collection<Integer> tempOrder = keyMap.values();
        for (final Object i : tempOrder) {
            order.add((Integer) i);
        }
    }
    
    public PermutationCipher(final byte[] key, final Mode mode) {
        super(mode);
        this.key = key;
        setOrder();
        numCols = key.length;
    }
    
    public PermutationCipher(final String key, final Mode mode) {
        this(key.getBytes(charset), mode);
    }
    
    public PermutationCipher(final byte[] key) {
        this(key, new ElectronicCodebookMode());
    }
    
    public PermutationCipher(final String key) {
        this(key.getBytes(charset));
    }
    
    @Override
    public int getBlockSize() {
        return key.length;
    }
    
    @Override
    public byte[] encryptBlock(final byte[] plainBlock) {
        final byte[] cipherBlock = new byte[plainBlock.length];
        for (int i = 0; i < cipherBlock.length; i++) {
            cipherBlock[i] = plainBlock[order.get(i)];
        }
        return cipherBlock;
    }
    
    @Override
    public byte[] decryptBlock(final byte[] cipherBlock) {
        final byte[] plainBlock = new byte[cipherBlock.length];
        for (int i = 0; i < plainBlock.length; i++) {
            plainBlock[i] = cipherBlock[order.indexOf(i)];
        }
        return plainBlock;
    }
    
}
