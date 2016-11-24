package sen.khyber.crypto.ciphers.transposition;

import java.util.Collection;
import java.util.List;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import sen.khyber.crypto.ciphers.BlockCipher;
import sen.khyber.crypto.modes.ElectronicCodebookMode;
import sen.khyber.crypto.modes.Mode;

public class PermutationCipher extends BlockCipher implements TranspositionCipher {
    
    private static final Charset charset = StandardCharsets.UTF_8;
    
    private byte[] key;
    private List<Integer> order = new ArrayList<>();
    
    @SuppressWarnings("unused")
    private int numCols;
    
    private void setOrder() {
        Map<Byte, Integer> keyMap = new TreeMap<>();
        for (int i = 0; i < key.length; i++) {
            keyMap.put(key[i], i);
        }
        Collection<Integer> tempOrder = keyMap.values();
        for (Object i : tempOrder) {
            order.add((Integer) i);
        }
    }
    
    public PermutationCipher(byte[] key, Mode mode) {
        super(mode);
        this.key = key;
        setOrder();
        numCols = key.length;
    }
    
    public PermutationCipher(String key, Mode mode) {
        this(key.getBytes(charset), mode);
    }
    
    public PermutationCipher(byte[] key) {
        this(key, new ElectronicCodebookMode());
    }
    
    public PermutationCipher(String key) {
        this(key.getBytes(charset));
    }
    
    @Override
    public int getBlockSize() {
        return key.length;
    }

    @Override
    public byte[] encryptBlock(byte[] plainBlock) {
        byte[] cipherBlock = new byte[plainBlock.length];
        for (int i = 0; i < cipherBlock.length; i++) {
            cipherBlock[i] = plainBlock[order.get(i)];
        }
        return cipherBlock;
    }

    @Override
    public byte[] decryptBlock(byte[] cipherBlock) {
        byte[] plainBlock = new byte[cipherBlock.length];
        for (int i = 0; i < plainBlock.length; i++) {
            plainBlock[i] = cipherBlock[order.indexOf(i)];
        }
        return plainBlock;
    }

}
