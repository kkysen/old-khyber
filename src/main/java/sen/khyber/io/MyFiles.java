package sen.khyber.io;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.StreamSupport;

public class MyFiles {
    
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    
    public static String read(Path path) throws IOException {
        return new String(Files.readAllBytes(path), CHARSET);
    }
    
    public static void write(Path path, String contents) throws IOException {
        Files.write(path, contents.getBytes(CHARSET));
    }
    
    public static <T> void write(Path path, Iterable<T> iter) throws IOException {
        Iterable<String> lines = StreamSupport.stream(iter.spliterator(), false).map(T::toString)::iterator;
        Files.write(path, lines, CHARSET);
    }
    
    public static <T> void write(Path path, T t) throws IOException {
        write(path, t.toString());
    }
    
    public static void append(Path path, String contents) throws IOException {
        write(path, read(path) + contents);
    }
    
    public static <T> void append(Path path, T t) throws IOException {
        append(path, t.toString());
    }
    
    public static void modify(Path readPath, Path writePath, Consumer<byte[]> modifier) throws IOException {
        byte[] bytes = Files.readAllBytes(readPath);
        modifier.accept(bytes);
        Files.write(writePath, bytes);
    }
    
    public static void modify(Path path, Consumer<byte[]> modifier) throws IOException {
        modify(path, path, modifier);
    }
    
    public static void modifyLines(Path readPath, Path writePath, Function<String, String> modifier) throws IOException {
        List<String> lines = readLines(readPath);
        for (int i = 0; i < lines.size(); i++) {
            lines.set(i, modifier.apply(lines.get(i)));
        }
        Files.write(writePath, lines, CHARSET);
    }
    
    public static void modifyLines(Path path, Function<String, String> modifier) throws IOException {
        modifyLines(path, path, modifier);
    }
    
    public static <R> R apply(Path path, Function<byte[], R> function) throws IOException {
        byte[] bytes = Files.readAllBytes(path);
        R r = function.apply(bytes);
        Files.write(path, bytes);
        return r;
    }
    
    public static List<String> readLines(Path path) throws IOException {
        return Files.readAllLines(path, CHARSET);
    }
    
    public static FileLines readLinesAsIterable(Path path) {
        return new FileLines(path, CHARSET);
    }
    
    private static void walkDirectory(List<Path> paths, Path dir, int maxDepth) throws IOException {
        if (maxDepth > 0) {
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(dir);
            for (Path file : directoryStream) {
                if (Files.isDirectory(file)) {
                    walkDirectory(paths, file, maxDepth - 1);
                } else {
                    paths.add(file);
                }
            }
        }
    }
    
    public static List<Path> walkDirectory(Path dir, int maxDepths) throws IOException {
        List<Path> paths = new ArrayList<>();
        walkDirectory(paths, dir, maxDepths);
        return paths;
    }
    
}
