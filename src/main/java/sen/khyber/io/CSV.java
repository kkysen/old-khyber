package sen.khyber.io;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class CSV {
    
    private static List<String> splitUnquotedLine(final String line) {
        return new ArrayList<>(Arrays.asList(line.split(",")));
    }
    
    private static String stripQuotes(final String s) {
        if (s.charAt(0) == '"' && s.charAt(s.length() - 1) == '"') {
            return s.substring(1, s.length() - 1);
        } else {
            return s;
        }
    }
    
    public static List<String> splitQuotedLine(final String line) {
        // http://stackoverflow.com/questions/1757065/java-splitting-a-comma-separated-string-but-ignoring-commas-in-quotes
        final String[] splitLine = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        final List<String> list = new ArrayList<>(splitLine.length);
        for (final String s : splitLine) {
            list.add(stripQuotes(s));
        }
        return list;
    }
    
    private static List<String> splitLine(final String line, final boolean isQuoted) {
        if (isQuoted) {
            return splitQuotedLine(line);
        } else {
            return splitUnquotedLine(line);
        }
    }
    
    public static List<List<String>> read(final Path path, final boolean isQuoted)
            throws IOException {
        final List<String> lines = MyFiles.readLines(path);
        final List<List<String>> csv = new ArrayList<>(lines.size());
        for (final String line : lines) {
            csv.add(splitLine(line, isQuoted));
        }
        return csv;
    }
    
    public static List<List<String>> read(final Path path) throws IOException {
        return read(path, false);
    }
    
    public static List<List<String>> readQuoted(final Path path) throws IOException {
        return read(path, true);
    }
    
    public static void main(final String[] args) {
        final String line = "\"hello, world\",hello,\"world\"";
        System.out.println(splitQuotedLine(line).size());
    }
    
}
