package sen.khyber.stats.counter;

import java.io.IOException;
import java.nio.file.Path;

/**
 * 
 * 
 * @author Khyber Sen
 */
public interface IOCapableCounter {
    
    public void addFile(Path path) throws IOException;
    
    public void save(Path path) throws IOException;
    
    public void saveSorted(Path path) throws IOException;
    
}
