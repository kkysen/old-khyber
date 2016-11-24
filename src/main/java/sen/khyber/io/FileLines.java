package sen.khyber.io;

import java.nio.charset.Charset;
import java.nio.file.Path;

public class FileLines implements Iterable<String> {
    
    private Path path;
    private Charset charset;
    
    public FileLines(Path path, Charset charset) {
        this.path = path;
        this.charset = charset;
    }

    @Override
    public FileLineIterator iterator() {
        return new FileLineIterator(path, charset);
    }
    
}
