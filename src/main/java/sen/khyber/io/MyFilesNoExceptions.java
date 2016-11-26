package sen.khyber.io;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * wrapper for MyFiles static methods that catch all checked exceptions
 * 
 * @author Khyber Sen
 */
public class MyFilesNoExceptions {
    
    public static String read(final Path path) {
        try {
            return MyFiles.read(path);
        } catch (final IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void write(final Path path, final String contents) {
        try {
            MyFiles.write(path, contents);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
    
    public static <T> void write(final Path path, final Iterable<T> iter) {
        try {
            MyFiles.write(path, iter);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
    
    public static <T> void write(final Path path, final T t) {
        try {
            MyFiles.write(path, t);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void append(final Path path, final String contents) {
        try {
            MyFiles.append(path, contents);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
    
    public static <T> void append(final Path path, final T t) {
        try {
            MyFiles.append(path, t);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void modify(final Path readPath, final Path writePath,
            final Consumer<byte[]> modifier) {
        try {
            MyFiles.modify(readPath, writePath, modifier);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void modify(final Path path, final Consumer<byte[]> modifier) {
        try {
            MyFiles.modify(path, modifier);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void modifyLines(final Path readPath, final Path writePath,
            final Function<String, String> modifier) {
        try {
            MyFiles.modifyLines(readPath, writePath, modifier);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void modifyLines(final Path path, final Function<String, String> modifier) {
        try {
            MyFiles.modifyLines(path, modifier);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
    
    public static <R> R apply(final Path path, final Function<byte[], R> function) {
        try {
            return MyFiles.apply(path, function);
        } catch (final IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static List<String> readLines(final Path path) {
        try {
            return MyFiles.readLines(path);
        } catch (final IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static List<Path> walkDirectory(final Path dir, final int maxDepths) {
        try {
            return MyFiles.walkDirectory(dir, maxDepths);
        } catch (final IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
