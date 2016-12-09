package sen.khyber.apcs.barcode;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class Barcode implements Comparable<Barcode> {
    
    private static final int ZIPCODE_LENGTH = 5;
    private static final int BAR_LENGTH = 5;
    private static final int BARCODE_LENGTH = BAR_LENGTH * (ZIPCODE_LENGTH + 1) + 2;
    private static final int TO_STRING_LENGTH = BARCODE_LENGTH + ZIPCODE_LENGTH + 2;
    
    private static final Map<Character, String> ENCODING = new HashMap<>(10);
    static {
        ENCODING.put('0', "||:::");
        ENCODING.put('1', ":::||");
        ENCODING.put('2', "::|:|");
        ENCODING.put('3', "::||:");
        ENCODING.put('4', ":|::|");
        ENCODING.put('5', ":|:|:");
        ENCODING.put('6', ":||::");
        ENCODING.put('7', "|:::|");
        ENCODING.put('8', "|::|:");
        ENCODING.put('9', "|:|::");
    }
    
    private static final Map<String, Character> DECODING = new HashMap<>(10);
    static {
        DECODING.put("||:::", '0');
        DECODING.put(":::||", '1');
        DECODING.put("::|:|", '2');
        DECODING.put("::||:", '3');
        DECODING.put(":|::|", '4');
        DECODING.put(":|:|:", '5');
        DECODING.put(":||::", '6');
        DECODING.put("|:::|", '7');
        DECODING.put("|::|:", '8');
        DECODING.put("|:|::", '9');
    }
    
    private String zipCode;
    private int zipInt;
    private int checkSum;
    private String barcode;
    private String toString;
    
    private void initZipCode(final String zipCode) {
        this.zipCode = zipCode;
        int digitSum = 0;
        final StringBuilder sb = new StringBuilder(TO_STRING_LENGTH);
        sb.append(zipCode + "  |");
        for (int i = 0; i < ZIPCODE_LENGTH; i++) {
            final char digit = zipCode.charAt(i);
            if (digit < '0' || digit > '9') {
                throw new IllegalArgumentException(
                        "zipCode must be all digits; found " + digit + " at index " + i);
            }
            digitSum += digit;
            sb.append(ENCODING.get(digit));
        }
        digitSum -= '0' * ZIPCODE_LENGTH;
        checkSum = digitSum % 10;
        zipInt = Integer.parseInt(zipCode);
        sb.append(ENCODING.get((char) (checkSum + '0')));
        sb.append("|");
        sb.setCharAt(ZIPCODE_LENGTH, (char) (checkSum + '0'));
        barcode = sb.substring(TO_STRING_LENGTH - BARCODE_LENGTH).toString();
        toString = sb.toString();
    }
    
    private void checkEdgeBars(final String barcode) {
        char edge;
        if ((edge = barcode.charAt(0)) != '|') {
            throw new IllegalArgumentException("barcode must start with '|'; given: " + edge);
        }
        if ((edge = barcode.charAt(BARCODE_LENGTH - 1)) != '|') {
            throw new IllegalArgumentException("barcode must end with '|'; given: " + edge);
        }
    }
    
    private void checkInvalidChars(final String barcode) {
        for (int i = 1; i < BARCODE_LENGTH - 1; i++) {
            final char c = barcode.charAt(i);
            if (!(c == '|' || c == ':')) {
                throw new IllegalArgumentException(
                        "barcode must contain only '|' and ':'; found " + c + " at index " + i);
            }
        }
    }
    
    private char decodeBar(final String bar) {
        final Character digit = DECODING.get(bar);
        if (digit == null) {
            throw new IllegalArgumentException(bar + " is not a valid barcode character");
        }
        return digit;
    }
    
    private void initBarcode(final String barcode) {
        checkEdgeBars(barcode);
        checkInvalidChars(barcode);
        this.barcode = barcode;
        int digitSum = 0;
        final StringBuilder sb = new StringBuilder(ZIPCODE_LENGTH);
        for (int i = 0; i < ZIPCODE_LENGTH; i++) {
            final int startIndex = i * BAR_LENGTH + 1;
            final String bar = barcode.substring(startIndex, startIndex + BAR_LENGTH);
            final char digit = decodeBar(bar);
            sb.append(digit);
            digitSum += digit - '0';
        }
        final int endIndex = BARCODE_LENGTH - 1;
        final String checkSumBar = barcode.substring(endIndex - BAR_LENGTH, endIndex);
        checkSum = decodeBar(checkSumBar) - '0';
        if (checkSum != digitSum % 10) {
            throw new IllegalArgumentException(
                    "found checkSum=" + checkSum + " when checkSum should be " + digitSum % 10);
        }
        zipCode = sb.toString();
        zipInt = Integer.parseInt(zipCode);
        toString = zipCode + (char) (checkSum + '0') + " " + barcode;
    }
    
    public Barcode(final String zipOrBarcode) {
        final int length = zipOrBarcode.length();
        if (length == ZIPCODE_LENGTH) {
            initZipCode(zipOrBarcode);
        } else if (length == BARCODE_LENGTH) {
            initBarcode(zipOrBarcode);
        } else {
            throw new IllegalArgumentException("must be of length " + ZIPCODE_LENGTH
                    + " (zipCode) or " + BARCODE_LENGTH + " (barcode); given: " + length);
        }
    }
    
    public int checkSum() {
        return checkSum;
    }
    
    @Override
    public String toString() {
        return toString;
    }
    
    public String toCode() {
        return barcode;
    }
    
    public String toZip() {
        return zipCode;
    }
    
    // doesn't need to compare checkSum 
    // because for equal zips checkSum will also be equal
    @Override
    public int compareTo(final Barcode other) {
        return Integer.compare(zipInt, other.zipInt);
    }
    
    public static String toCode(final String zip) {
        return new Barcode(zip).barcode;
    }
    
    public static String toZip(final String code) {
        return new Barcode(code).zipCode;
    }
    
}
