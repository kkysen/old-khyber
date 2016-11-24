package sen.khyber.crypto.ciphers.transposition;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ColumnCipher implements TranspositionCipher {
    
    private static final Charset charset = StandardCharsets.UTF_8;
    
    private byte[] key;
    private List<Integer> order = new ArrayList<>();
    private int numCols;
    private int numRounds;
    
    private void setOrder() {
        Map<Byte, Integer> keyMap = new TreeMap<>();
        for (int i = 0, index = 0; i < key.length; i++, index++) {
            if (keyMap.containsKey(key[i])) {
                index--;
            } else {
                keyMap.put(key[i], index);
            }
        }
        for (Integer i : keyMap.values()) {
            order.add(i);
        }
        numCols = order.size();
    }
    
    public ColumnCipher(byte[] key, int numRounds) {
        this.key = key;
        setOrder();
        this.numRounds = numRounds;
    }
    
    public ColumnCipher(byte[] key) {
        this(key, 1);
    }
    
    public ColumnCipher(String key, int numRounds) {
        this(key.getBytes(charset), numRounds);
    }
    
    public ColumnCipher(String key) {
        this(key.getBytes(charset));
    }

    @Override
    public int getBlockSize() {
        return numCols;
    }
    
    private byte[] encryptOnce(byte[] plainbytes) {
        int numRows = plainbytes.length / numCols;
        byte[] cipherbytes = new byte[plainbytes.length];
        for (int j = 0; j < numCols; j++) {
            int colNum = order.get(j);
            for (int i = 0; i < numRows; i++) {
                cipherbytes[(j * numRows) + i] = plainbytes[(i * numCols) + colNum];
            }
        }
        return cipherbytes;
    }
    
    private byte[] decryptOnce(byte[] cipherbytes) {
        int numRows = cipherbytes.length / numCols;
        byte[] plainbytes = new byte[cipherbytes.length];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                int rowNum = order.indexOf(j);
                plainbytes[(i * numCols) + j] = cipherbytes[(rowNum * numRows) + i];
            }
        }
        return plainbytes;
    }
    
    private byte[] crypt(byte[] bytes, boolean forEncryption) {
        for (int i = 0; i < numRounds; i++) {
            if (forEncryption) {
                bytes = encryptOnce(bytes);
            } else {
                bytes = decryptOnce(bytes);
            }
        }
        return bytes;
    }

    @Override
    public byte[] encrypt(byte[] plainbytes) {
        return crypt(plainbytes, true);
    }

    @Override
    public byte[] decrypt(byte[] cipherbytes) {
        return crypt(cipherbytes, false);
    }
    
    public static void main(String[] args) {
        
        ColumnCipher reverser = new ColumnCipher("passwordsabc");
        byte[] plainIn = "super hello world and again super hello world".getBytes(charset);
        byte[] cipherbytes = reverser.encrypt(plainIn);
        byte[] plainOut = reverser.decrypt(cipherbytes);
        
        System.out.println(plainIn.length + "\n");
        
        System.out.println(new String(plainIn));
        System.out.println();
        
        System.out.println(new String(cipherbytes));
        System.out.println();
        
        System.out.println(new String(plainOut));
        System.out.println();
        
    }

}
