package sen.khyber.util;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class StringUtils {
    
    private static char unicodeQuoteToAscii(final char c) {
        if (c == 8217) {
            System.out.println(c);
        }
        switch (c) {
            case 8217:
                return '\'';
            case 160:
                return ' ';
            case 8211:
                return '-';
            case '\u2018':
                return '\'';
            case '\u2017':
                return '\'';
            case '\u201c':
                return '"';
            case '\u201d':
                return '"';
            default:
                return c;
        }
    }
    
    public static String unicodeQuotesToAscii(final String s) {
        final char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] = unicodeQuoteToAscii(chars[i]);
        }
        return new String(chars);
    }
    
}
