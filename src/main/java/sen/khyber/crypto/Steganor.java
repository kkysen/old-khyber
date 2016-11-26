package sen.khyber.crypto;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;
import java.util.StringJoiner;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class Steganor {
    
    private static final Charset charset = StandardCharsets.UTF_8;
    
    // & 1 gets last bit
    // & 0xFE clears last bit
    // | 1 or | 0 sets last bit to 1 or 0
    
    public static void print(final byte[] bytes) {
        System.out.println(Arrays.toString(bytes));
    }
    
    private static final Random random = new Random();
    
    private final byte[] text;
    
    public Steganor(final byte[] text) {
        this.text = text;
    }
    
    public Steganor(final String text) {
        this(text.getBytes(charset));
    }
    
    @Override
    public String toString() {
        //return Arrays.toString(text);
        final byte[] bytes = text;
        final StringJoiner js = new StringJoiner(", ", "{", "}");
        for (int i = 0; i < bytes.length; i++) {
            js.add(i + "=" + bytes[i]);
        }
        return js.toString();
    }
    
    private int genMessageOffset(final int length) {
        return random.nextInt(text.length - length * 8);
    }
    
    private int genIntOffset() {
        return genMessageOffset(4);
    }
    
    private static byte[] intToBytes(final int i) {
        return ByteBuffer.allocate(4).putInt(i).array();
    }
    
    private static int bytesToInt(final byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }
    
    public void flipBits(final byte[] message, int offset) {
        for (int i = 0; i < message.length; i++) {
            final byte byteToAdd = message[i];
            for (int bitNum = 7; bitNum >= 0; bitNum--, offset++) {
                final int bitToAdd = byteToAdd >>> bitNum & 1;
                message[i] = (byte) (message[i] << 1 | text[offset] & 1);
                text[offset] = (byte) (text[offset] & 0xFE | bitToAdd);
            }
        }
    }
    
    public int hideInt(final byte[] iBytes) {
        System.out.println(Arrays.toString(iBytes));
        final int offset = genIntOffset();
        flipBits(iBytes, offset);
        return offset;
    }
    
    /*
    hideIntOnce
        generate random offset
        flipBits at offset
        return offset
    hideIntMany
        convert int to iBytes
        repeatedly hideIntOnce with iBytes
        return iBytes converted to int
    findIntOnce
        offset is iBytes converted to int
        flipBits of iBytes at offset
    findIntMany
        iBytes is offset converted
        repeatedly findIntOnce with iBytes
        return iBytes converted to int
    */
    
    public void findInt(final byte[] iBytes) {
        System.out.println(Arrays.toString(iBytes));
        final int offset = bytesToInt(iBytes);
        flipBits(iBytes, offset);
    }
    
    public int hideInt(int offset, final int numIters) {
        final byte[] iBytes = intToBytes(offset);
        for (int i = 0; i < numIters; i++) {
            offset = hideInt(iBytes);
        }
        return bytesToInt(iBytes);
    }
    
    public int findInt(final int offset, final int numIters) {
        final byte[] iBytes = intToBytes(offset);
        for (int i = 0; i < numIters; i++) {
            findInt(iBytes);
        }
        return bytesToInt(iBytes);
    }
    
    /*public int hideInt(int i) {
        int offset = genIntOffset();
        byte[] iBytes = intToBytes(i);
        flipBits(iBytes, offset);
        
    }
    
    private void hideBytes(byte[] message, int offset) {
        for (byte byteToAdd : message) {
            for (int bitNum = 7; bitNum >= 0; bitNum--, offset++) {
                int bitToAdd = (byteToAdd >>> bitNum) & 1;
                text[offset] = (byte) ((text[offset] & 0xFE) | bitToAdd);
            }
        }
    }
    
    private byte[] findBytes(int length, int offset) {
        byte[] message = new byte[length];
        for (int b = 0; b < length; b++) {
            for (int i = 0; i < 8; i++, offset++) {
                // error
                // text must return to pre hid state
                // in order to have overlapping hidden messages
                // and be able to find the original ones
                // but this is impossible
                // because the old bits were destroyed
                message[b] = (byte) ((message[b] << 1) | (text[offset] & 1));
                text[offset] = (byte) ((text[offset] << 1) | message[b] & 1);
            }
        }
        return message;
    }
    
    public int hideInt(int i) {
        int offset = genIntOffset();
        hideBytes(intToBytes(i), offset);
        return offset;
    }
    
    public int findInt(int offset) {
        return bytesToInt(findBytes(4, offset));
    }
    
    public int hideInt(int offset, int numIters) {
        System.out.println(offset);
        for (int i = 0; i < numIters; i++) {
            offset = hideInt(offset);
            System.out.println(offset);
        }
        return offset;
    }
    
    public int findInt(int offset, int numIters) {
        System.out.println(offset);
        for (int i = 0; i < numIters; i++) {
            offset = findInt(offset);
            System.out.println(offset);
        }
        return offset;
    }
    
    public int[] hideMessage(byte[] message) {
        int messageOffset = genMessageOffset(message.length);
        hideBytes(message, messageOffset);
        return new int[] {message.length, messageOffset};
    }
    
    public byte[] findMessage(int length, int offset) {
        return findBytes(length, offset);
    }
    
    private int[] hideToIntArray(byte[] message, int numIters) {
        int[] originalOffsets = hideMessage(message);
        int[] finalOffsets = new int[originalOffsets.length + 1];
        for (int i = 0; i < originalOffsets.length; i++) {
            finalOffsets[i] = hideInt(originalOffsets[i], numIters);
        }
        finalOffsets[finalOffsets.length - 1] = numIters;
        return finalOffsets;
    }
    
    public String hide(byte[] message, int numIters) {
        int[] offsets = hideToIntArray(message, numIters);
        StringJoiner sj = new StringJoiner(",");
        for (int offset : offsets) {
            sj.add(String.valueOf(offset));
        }
        return sj.toString();
    }
    
    public String hide(byte[] message) {
        return hide(message, 100);
    }
    
    private byte[] find(int lengthOffset, int messageOffset, int numIters) {
        int length = findInt(lengthOffset, numIters);
        int offset = findInt(messageOffset, numIters);
        return findMessage(length, offset);
    }
    
    public byte[] find(String key) {
        String[] offsetsAsStrings = key.split(",");
        int[] offsets = new int[offsetsAsStrings.length];
        for (int i = 0; i < offsets.length; i++) {
            offsets[i] = Integer.valueOf(offsetsAsStrings[i]);
        }
        return find(offsets[0], offsets[1], offsets[2]);
    }*/
    
    public static void main(final String[] args) {
        
    }
    
}
