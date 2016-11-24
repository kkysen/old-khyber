package sen.khyber.io;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSV {
    
    private static List<String> splitUnquotedLine(String line) {
        return new ArrayList<>(Arrays.asList(line.split(",")));
    }
    
    private static String stripQuotes(String s) {
        if ((s.charAt(0) == '"') && (s.charAt(s.length() - 1) == '"')) {
            return s.substring(1, s.length() - 1);
        } else {
            return s;
        }
    }
    
    public static List<String> splitQuotedLine(String line) {
        // http://stackoverflow.com/questions/1757065/java-splitting-a-comma-separated-string-but-ignoring-commas-in-quotes
        String[] splitLine = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        List<String> list = new ArrayList<>(splitLine.length);
        for (String s : splitLine) {
            list.add(stripQuotes(s));
        }
        return list;
    }
    
    private static List<String> splitLine(String line, boolean isQuoted) {
        if (isQuoted) {
            return splitQuotedLine(line);
        } else {
            return splitUnquotedLine(line);
        }
    }
    
    public static List<List<String>> read(Path path, boolean isQuoted) throws IOException {
        List<String> lines = MyFiles.readLines(path);
        List<List<String>> csv = new ArrayList<>(lines.size());
        for (String line : lines) {
            csv.add(splitLine(line, isQuoted));
        }
        return csv;
    }
    
    public static List<List<String>> read(Path path) throws IOException {
        return read(path, false);
    }
    
    public static List<List<String>> readQuoted(Path path) throws IOException {
        return read(path, true);
    }
    
    public static void main(String[] args) {
        String line = "\"hello, world\",hello,\"world\"";
        System.out.println(splitQuotedLine(line).size());
    }
    
}
