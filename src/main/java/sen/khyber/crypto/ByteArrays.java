package sen.khyber.crypto;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class ByteArrays {
    
    public static byte[] concat(final byte[]... byteArrays) {
        int capacity = 0;
        for (final byte[] arr : byteArrays) {
            capacity += arr.length;
        }
        final ByteBuffer buffer = ByteBuffer.allocate(capacity);
        for (final byte[] arr : byteArrays) {
            buffer.put(arr);
        }
        return buffer.array();
    }
    
    public static byte[] concat(final Iterable<byte[]> byteArrays) {
        int capacity = 0;
        for (final byte[] arr : byteArrays) {
            capacity += arr.length;
        }
        final ByteBuffer buffer = ByteBuffer.allocate(capacity);
        for (final byte[] arr : byteArrays) {
            buffer.put(arr);
        }
        return buffer.array();
    }
    
    public static byte[] append(final byte[] bytes, final byte b) {
        return concat(bytes, new byte[] {b});
    }
    
    public static byte[] append(final byte[] bytes, final byte b, final int i) {
        if (i == 0) {
            return concat(new byte[] {b}, bytes);
        }
        return concat(Arrays.copyOfRange(bytes, 0, i),
                new byte[] {b},
                Arrays.copyOfRange(bytes, i, bytes.length));
    }
    
    public static byte[] fill(final byte b, final int length) {
        final byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = b;
        }
        return bytes;
    }
    
    public static byte[] fill(final int i, final int length) {
        return fill((byte) i, length);
    }
    
    /*
     * copies the specified bytes between fromIndex to toIndex
     * from copiedBytes to targetBytes
     */
    public static void copyTo(final byte[] targetBytes, final byte[] copiedBytes, final int fromIndex, final int toIndex) {
        for (int i = fromIndex; i < toIndex; i++) {
            targetBytes[i] = copiedBytes[i];
        }
    }
    
    /*
     * copies the range of copiedBytes specified by fromIndex and toIndex
     * into a new byte[] of the specified length
     * this means that the ends of the returned byte[] may be set to 0
     * or truncated if length is shorted than copiedBytes.length
     */
    public static byte[] copyTo(final int length, final byte[] copiedBytes, final int fromIndex, final int toIndex) {
        final byte[] targetBytes = new byte[length];
        copyTo(targetBytes, copiedBytes, fromIndex, toIndex);
        return targetBytes;
    }
    
    /*
     * copies bytes from copiedBytes to targetBytes starting from fromIndex
     * stops when copiedBytes.length or targetBytes.length is reached
     */
    public static void copyTo(final byte[] targetBytes, final byte[] copiedBytes, final int fromIndex) {
        for (int i = fromIndex; i < copiedBytes.length && i < targetBytes.length; i++) {
            targetBytes[i] = copiedBytes[i];
        }
    }
    
    public static void overwrite(final byte[] targetBytes, final byte[] copiedBytes) {
        final int length = Math.min(targetBytes.length, copiedBytes.length);
        for (int i = 0; i < length; i++) {
            targetBytes[i] = copiedBytes[i];
        }
    }
    
    /*
     * copies all the bytes from copiedBytes to targetBytes
     * stopping at then end of targetBytes or copiedBytes
     * whichever comes first
     */
    public static void copyTo(final byte[] targetBytes, final byte[] copiedBytes) {
        copyTo(targetBytes, copiedBytes, 0);
    }
    
    /*
     * copies the bytes from copiedBytes starting at fromIndex
     * copies length bytes, unless copiedBytes is shorting
     * in which case the leftover elements in the returned byte[] are set to 0
     */
    public static byte[] copyTo(final int length, final byte[] copiedBytes, final int fromIndex) {
        final byte[] targetBytes = new byte[length];
        copyTo(targetBytes, copiedBytes, fromIndex);
        return targetBytes;
    }
    
    /*
     * copies the bytes from copiedBytes starting at the beginning
     * ends when length or copiedBytes.length is reached
     */
    public static byte[] copyTo(final int length, final byte[] copiedBytes) {
        return copyTo(length, copiedBytes, 0);
    }
    
    /*
     * copies all the bytes of copiedBytes
     * to the end of a new byte[length]
     * as long as length >= copiedBytes.length
     */
    public static byte[] copyToEnd(final int length, final byte[] copiedBytes) {
        return copyTo(length, copiedBytes, length - copiedBytes.length);
    }
    
    public static byte[] xor(final byte[] targetBytes, final byte[] addedBytes) {
        for (int i = 0; i < targetBytes.length; i++) {
            targetBytes[i] ^= addedBytes[i];
        }
        return targetBytes;
    }
    
    private static int convertIndex(final byte[] bytes, final int index) {
        if (index >= 0) {
            return index;
        } else {
            return bytes.length + index;
        }
    }
    
    public static byte get(final byte[] bytes, int index) {
        index = convertIndex(bytes, index);
        return bytes[index];
    }
    
    public static byte[] slice(final byte[] bytes, int fromIndex, int toIndex, int step) {
        fromIndex = convertIndex(bytes, fromIndex);
        toIndex = convertIndex(bytes, toIndex);
        final byte[] slicedBytes = new byte[Math.abs(toIndex - fromIndex)];
        if (step > 0) {
            for (int i = 0; i < slicedBytes.length; i += step) {
                slicedBytes[i] = bytes[i + fromIndex];
            }
        } else {
            step = -step;
            for (int i = slicedBytes.length - 1; i >= 0; i -= step) {
                slicedBytes[i] = bytes[fromIndex - i - 1];
            }
        }
        return slicedBytes;
    }
    
    public static byte[] slice(final byte[] bytes, final int fromIndex, final int toIndex) {
        return slice(bytes, fromIndex, toIndex, 1);
    }
    
    public static byte[] slice(final byte[] bytes, final int fromIndex) {
        return slice(bytes, fromIndex, bytes.length);
    }
    
    public static byte[] reverse(final byte[] bytes) {
        return slice(bytes, bytes.length, 0, -1);
    }
    
    public static byte[][] splitAtIndices(final byte[] bytes, final int... indices) {
        final int[] allIndices = new int[indices.length + 2];
        allIndices[0] = 0;
        allIndices[allIndices.length - 1] = bytes.length;
        for (int i = 0; i < indices.length; i++) {
            allIndices[i + 1] = indices[i];
        }
        final byte[][] splitArrays = new byte[indices.length + 1][];
        for (int i = 0; i + 1 < indices.length; i++) {
            splitArrays[i] = Arrays.copyOfRange(bytes, indices[i], indices[i + 1]);
        }
        return splitArrays;
    }
    
    public static byte[][] splitByLength(final byte[] bytes, final int rowLength) {
        final int numFullRows = bytes.length / rowLength;
        final int remainderRowLength = bytes.length % rowLength;
        final boolean addRemainderRow = remainderRowLength != 0;
        final int numRows = addRemainderRow ? numFullRows + 1 : numFullRows;
        final byte[][] rows = new byte[numRows][];
        for (int i = 0; i < numFullRows; i++) {
            rows[i] = slice(bytes, i * rowLength, (i + 1) * rowLength);
        }
        if (addRemainderRow) {
            rows[rows.length - 1] = slice(bytes, rowLength * numFullRows);
        }
        return rows;
    }
    
    public static List<byte[]> toBlocks(final byte[] bytes, final int blockSize) {
        final int length = bytes.length;
        final List<byte[]> blocks = new ArrayList<>(length / blockSize);
        for (int i = 0; i < length; i += blockSize) {
            blocks.add(slice(bytes, i, i + blockSize));
        }
        return blocks;
    }
    
    public static byte[][] snake(final byte[] bytes, final int rowLength) {
        final byte[][] snake = splitByLength(bytes, rowLength);
        for (int i = 1; i < snake.length; i += 2) {
            snake[i] = reverse(snake[i]);
        }
        return snake;
    }
    
    public static byte[][] transpose(final byte[][] bytes) {
        final int height = bytes.length;
        final int width = bytes[0].length;
        final byte[][] newBytes = new byte[width][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                newBytes[j][i] = bytes[i][j];
            }
        }
        return newBytes;
    }
    
    public static byte[] convert(final Byte[] Bytes) {
        final byte[] bytes = new byte[Bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = Bytes[i];
        }
        return bytes;
    }
    
    public static byte[] trim(final Byte[] bytes) {
        final List<Byte> trimmedBytes = new ArrayList<>();
        for (final Byte b : bytes) {
            if (b != null) {
                trimmedBytes.add(b);
            }
        }
        return convert(trimmedBytes.toArray(new Byte[trimmedBytes.size()]));
    }
    
    public static void main(final String[] args) {
        System.out.println(new String(reverse("hello, world".getBytes())));
        System.out.println(new String(slice("hello, world".getBytes(), 0, -5)));
    }
    
}