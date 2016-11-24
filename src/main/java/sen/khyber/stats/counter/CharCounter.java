package sen.khyber.stats.counter;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import sen.khyber.io.MyFiles;
import sen.khyber.web.Internet;

public class CharCounter extends Counter<Character> implements IOCapableCounter {
    
    public CharCounter() {}
    
    public static CharCounter fromFile(final Path path) throws IOException {
        final CharCounter charCounter = new CharCounter();
        charCounter.addFile(path);
        return charCounter;
    }
    
    private static String escapeLineTerminators(final Character c) {
        if (c == '\n') {
            return "\\n";
        }
        if (c == '\r') {
            return "\\r";
        }
        return String.valueOf(c);
    }
    
    @Override
    protected String toCSVLine(final Character word, final AtomicLong count) {
        return escapeLineTerminators(word) + "," + count;
    }
    
    @Override
    protected String toCSVLine(final Map.Entry<Character, Long> count) {
        return escapeLineTerminators(count.getKey()) + "," + count.getValue();
    }
    
    public static CharCounter fromCSV(final Path path) throws IOException {
        final CharCounter charCounter = new CharCounter();
        charCounter.addCSV(path, s -> {
            System.out.println(s);
            if (s.equals("\\n")) {
                return '\n';
            }
            if (s.equals("\\r")) {
                return '\r';
            }
            return s.charAt(0);
        });
        return charCounter;
    }
    
    public static CharCounter fromWebsite(final URL url) throws IOException {
        final CharCounter charCounter = new CharCounter();
        charCounter.addWebsite(url);
        return charCounter;
    }
    
    private void addAll(final char[] chars) {
        for (final char c : chars) {
            add(c);
        }
    }
    
    public void addChars(final Iterable<Character> chars) {
        addAll(chars);
    }
    
    public void addChars(final String... strings) {
        for (final String s : strings) {
            addAll(s.toCharArray());
        }
    }
    
    // same erasure as Collection<Character>
    /*public int addChars(Collection<String> strings) {
        int i = 0;
        for (String s : strings) {
            i += addChars(s);
        }
        return i;
    }*/
    
    @Override
    public void addFile(final Path path) throws IOException {
        addChars(MyFiles.read(path));
    }
    
    public void addWebsite(final URL url) throws IOException {
        addChars(Internet.read(url));
    }
    
    public BarGraph<Character> toBarGraph() {
        return toBarGraph(Character.class);
    }
    
    public BarGraph<Character> toBarGraph(final int numMostCommon) {
        return toBarGraph(Character.class, numMostCommon);
    }
    
}