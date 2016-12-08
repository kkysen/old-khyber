package sen.khyber.apcs.barcode;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class Barcode implements Comparable<Barcode>, Cloneable {
    
    private static final Map<Integer, String> ENCODING = new HashMap<>(10);
    static {
        ENCODING.put(0, "||:::");
        ENCODING.put(1, ":::||");
        ENCODING.put(2, "::|:|");
        ENCODING.put(3, "::||:");
        ENCODING.put(4, ":|::|");
        ENCODING.put(5, ":|:|:");
        ENCODING.put(6, ":||::");
        ENCODING.put(7, "|:::|");
        ENCODING.put(8, "|::|:");
        ENCODING.put(9, "|:|::");
    }
    
    private final String zip;
    private final int zipCode;
    private final int checkDigit;
    private final String toString;
    
    public Barcode(final String zip) {
        final int length = zip.length();
        if (length != 5) {
            throw new IllegalArgumentException("length must be 5; given: " + length);
        }
        this.zip = zip;
        int digitSum = 0;
        final StringBuilder sb = new StringBuilder(6 * (length + 1) + 3);
        sb.append(zip + "  |");
        for (int i = 0; i < length; i++) {
            final char digit = zip.charAt(i);
            if (digit < '0' || digit > '9') {
                throw new IllegalArgumentException(
                        "zip must be all digits; found " + digit + " at index " + i);
            }
            digitSum += digit;
            sb.append(ENCODING.get(digit - '0'));
        }
        digitSum -= '0' * length;
        checkDigit = digitSum % 10;
        zipCode = Integer.parseInt(zip);
        sb.append(ENCODING.get(checkDigit));
        sb.append("|");
        sb.setCharAt(length, (char) (checkDigit + '0'));
        toString = sb.toString();
    }
    
    // for cloning
    private Barcode(final Barcode other) {
        zip = other.zip;
        zipCode = other.zipCode;
        checkDigit = other.checkDigit;
        toString = other.toString;
    }
    
    @Override
    public Barcode clone() {
        return new Barcode(this);
    }
    
    public int checkSum() {
        return checkDigit;
    }
    
    @Override
    public String toString() {
        return toString;
    }
    
    // doesn't need to compare checkDigit 
    // because for equal zips checkDigit will also be equal
    @Override
    public int compareTo(final Barcode other) {
        return Integer.compare(zipCode, other.zipCode);
    }
    
}
