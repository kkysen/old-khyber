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
    
    public static String read(Path path) {
        try {
            return MyFiles.read(path);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void write(Path path, String contents) {
        try {
            MyFiles.write(path, contents);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static <T> void write(Path path, Iterable<T> iter) {
        try {
            MyFiles.write(path, iter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static <T> void write(Path path, T t) {
        try {
            MyFiles.write(path, t);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void append(Path path, String contents) {
        try {
            MyFiles.append(path, contents);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static <T> void append(Path path, T t) {
        try {
            MyFiles.append(path, t);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void modify(Path readPath, Path writePath, Consumer<byte[]> modifier) {
        try {
            MyFiles.modify(readPath, writePath, modifier);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void modify(Path path, Consumer<byte[]> modifier) {
        try {
            MyFiles.modify(path, modifier);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void modifyLines(Path readPath, Path writePath, Function<String, String> modifier) {
        try {
            MyFiles.modifyLines(readPath, writePath, modifier);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void modifyLines(Path path, Function<String, String> modifier) {
        try {
            MyFiles.modifyLines(path, modifier);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static <R> R apply(Path path, Function<byte[], R> function) {
        try {
            return MyFiles.apply(path, function);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static List<String> readLines(Path path) {
        try {
            return MyFiles.readLines(path);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static List<Path> walkDirectory(Path dir, int maxDepths) {
        try {
            return MyFiles.walkDirectory(dir, maxDepths);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
