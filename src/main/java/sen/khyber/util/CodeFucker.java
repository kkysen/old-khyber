package sen.khyber.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import sen.khyber.crypto.ByteArrays;
import sen.khyber.io.MyFiles;

public class CodeFucker {
    
    private static final Charset charset = StandardCharsets.UTF_8;
    private static final Map<Character, Character> lookalikes = new HashMap<>(),
                                                   lookalikesFlipped = new HashMap<>();
    
    private static final char[][] lookalikesArr = {
        {';', '\u037E'},
        {'a', '\u0430'},
        {'c', '\u0441'},
        {'p', '\u0440'},
        {'O', '0'},
        {'l', '1'},
    };
    
    static {
        for (char[] pair : lookalikesArr) {
            lookalikes.put(pair[0], pair[1]);
            lookalikesFlipped.put(pair[1], pair[0]);
        }
    }
    
    private static int countChar(String s, char c) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (c == s.charAt(i)) {
                count++;
            }
        }
        return count;
    }
    
    private static int replace(byte[] bytes, char oldChar, boolean forFucking) {
        char newChar = (forFucking) ? lookalikes.get(oldChar) : lookalikesFlipped.get(oldChar);
        String fucked = new String(bytes, charset).replace(oldChar, newChar);
        int count = countChar(fucked, newChar);
        ByteArrays.overwrite(bytes, fucked.getBytes(charset));
        return count;
    }
    
    // replace all
    private static int replace(byte[] bytes, boolean forFucking) {
        String fucked = new String(bytes, charset);
        int count = 0;
        Map<Character, Character> map = (forFucking) ? lookalikes : lookalikesFlipped;
        for (char oldChar : map.keySet()) {
            char newChar = map.get(oldChar);
            fucked = fucked.replace(oldChar, newChar);
            count += countChar(fucked, newChar);
        }
        ByteArrays.overwrite(bytes, fucked.getBytes(charset));
        return count;
    }
    
    private static int replace(Path path, char oldChar, boolean forFucking) throws IOException {
        return MyFiles.apply(path, bytes -> replace(bytes, oldChar, forFucking));
    }
    
    // replace all
    private static int replace(Path path, boolean forFucking) throws IOException {
        return MyFiles.apply(path, bytes -> replace(bytes, forFucking));
    }
    
    public static int fuck(Path path, char toFuckUpChar) throws IOException {
        if (! lookalikes.containsKey(toFuckUpChar)) {
            if (! lookalikesFlipped.containsKey(toFuckUpChar)) {
                throw new IllegalArgumentException("char not supported");
            } else {
                toFuckUpChar = lookalikesFlipped.get(toFuckUpChar);
            }
        }
        return replace(path, toFuckUpChar, true);
    }
    
    // fuck all
    public static int fuck(Path path) throws IOException {
        return replace(path, true);
    }
    
    public static int unfuck(Path path, char toUnfuckChar) throws IOException {
        if (! lookalikesFlipped.containsKey(toUnfuckChar)) {
            if (! lookalikes.containsKey(toUnfuckChar)) {
                throw new IllegalArgumentException("char not supported");
            } else {
                toUnfuckChar = lookalikes.get(toUnfuckChar);
            }
        }
        return replace(path, toUnfuckChar, false);
    }
    
    // unfuck all
    public static int unfuck(Path path) throws IOException {
        return replace(path, false);
    }
    
    public static int fuck(String path, char toFuckUpChar) throws IOException {
        return fuck(Paths.get(path), toFuckUpChar);
    }
    
    public static int fuck(String path) throws IOException {
        return fuck(Paths.get(path));
    }
    
    public static int unfuck(String path, char toUnfuckChar) throws IOException {
        return unfuck(Paths.get(path), toUnfuckChar);
    }
    
    public static int unfuck(String path) throws IOException {
        return unfuck(Paths.get(path));
    }
    
    public static void main(String[] args) throws Exception {
        System.out.println(CodeFucker.fuck("src/sen.khyber.io/enums/Day.java", 'a'));
        System.out.println('\u037E');
    }
    
}
