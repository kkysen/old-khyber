package sen.khyber.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

public class FileLineIterator implements Iterator<String> {
    
    private BufferedReader reader;
    private String line;
    
    public FileLineIterator(Path path, Charset charset) {
        try {
            reader = Files.newBufferedReader(path, charset);
        } catch (IOException e) {
            e.printStackTrace();
            throw new NullPointerException("BufferedReader is null");
        }
    }
    
    @Override
    public boolean hasNext() {
        try {
            line = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line != null;
    }
    
    @Override
    public String next() {
        return line;
    }
    
}
