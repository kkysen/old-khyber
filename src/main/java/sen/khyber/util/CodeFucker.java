package sen.khyber.util;

import sen.khyber.crypto.ByteArrays;
import sen.khyber.io.MyFiles;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class CodeFucker {
    
    private static final Charset charset = StandardCharsets.UTF_8;
    private static final Map<Character, Character> lookalikes = new HashMap<>();
    private static final Map<Character, Character> lookalikesFlipped = new HashMap<>();
    
    private static final char[][] lookalikesArr = {
        {';', '\u037E'},
        {'a', '\u0430'},
        {'c', '\u0441'},
        {'p', '\u0440'},
        {'O', '0'},
        {'l', '1'},
    };
    
    static {
        for (final char[] pair : lookalikesArr) {
            lookalikes.put(pair[0], pair[1]);
            lookalikesFlipped.put(pair[1], pair[0]);
        }
    }
    
    private static int countChar(final String s, final char c) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (c == s.charAt(i)) {
                count++;
            }
        }
        return count;
    }
    
    private static int replace(final byte[] bytes, final char oldChar, final boolean forFucking) {
        final char newChar = forFucking ? lookalikes.get(oldChar) : lookalikesFlipped.get(oldChar);
        final String fucked = new String(bytes, charset).replace(oldChar, newChar);
        final int count = countChar(fucked, newChar);
        ByteArrays.overwrite(bytes, fucked.getBytes(charset));
        return count;
    }
    
    // replace all
    private static int replace(final byte[] bytes, final boolean forFucking) {
        String fucked = new String(bytes, charset);
        int count = 0;
        final Map<Character, Character> map = forFucking ? lookalikes : lookalikesFlipped;
        for (final char oldChar : map.keySet()) {
            final char newChar = map.get(oldChar);
            fucked = fucked.replace(oldChar, newChar);
            count += countChar(fucked, newChar);
        }
        ByteArrays.overwrite(bytes, fucked.getBytes(charset));
        return count;
    }
    
    private static int replace(final Path path, final char oldChar, final boolean forFucking)
            throws IOException {
        return MyFiles.apply(path, bytes -> replace(bytes, oldChar, forFucking));
    }
    
    // replace all
    private static int replace(final Path path, final boolean forFucking) throws IOException {
        return MyFiles.apply(path, bytes -> replace(bytes, forFucking));
    }
    
    public static int fuck(final Path path, char toFuckUpChar) throws IOException {
        if (!lookalikes.containsKey(toFuckUpChar)) {
            if (!lookalikesFlipped.containsKey(toFuckUpChar)) {
                throw new IllegalArgumentException("char not supported");
            } else {
                toFuckUpChar = lookalikesFlipped.get(toFuckUpChar);
            }
        }
        return replace(path, toFuckUpChar, true);
    }
    
    // fuck all
    public static int fuck(final Path path) throws IOException {
        return replace(path, true);
    }
    
    public static int unfuck(final Path path, char toUnfuckChar) throws IOException {
        if (!lookalikesFlipped.containsKey(toUnfuckChar)) {
            if (!lookalikes.containsKey(toUnfuckChar)) {
                throw new IllegalArgumentException("char not supported");
            } else {
                toUnfuckChar = lookalikes.get(toUnfuckChar);
            }
        }
        return replace(path, toUnfuckChar, false);
    }
    
    // unfuck all
    public static int unfuck(final Path path) throws IOException {
        return replace(path, false);
    }
    
    public static int fuck(final String path, final char toFuckUpChar) throws IOException {
        return fuck(Paths.get(path), toFuckUpChar);
    }
    
    public static int fuck(final String path) throws IOException {
        return fuck(Paths.get(path));
    }
    
    public static int unfuck(final String path, final char toUnfuckChar) throws IOException {
        return unfuck(Paths.get(path), toUnfuckChar);
    }
    
    public static int unfuck(final String path) throws IOException {
        return unfuck(Paths.get(path));
    }
    
    public static void main(final String[] args) throws Exception {
        System.out.println(CodeFucker.fuck("src/sen.khyber.io/enums/Day.java", 'a'));
        System.out.println('\u037E');
    }
    
}
