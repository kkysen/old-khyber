package sen.khyber.language.gutenberg;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import sen.khyber.io.MyFiles;

/**
 * creates a .csv index for all the Gutenberg text files
 * 
 * @author Khyber Sen
 */
public class GutenbergIndexer {
    
    private static final String GUTENBERG_DIRECTORY = "";
    
    private static final String[] FIELD_NAMES = {
        "title",
        "author",
        "postingDate",
        "releaseDate",
        "lastUpdatedDate",
        "sen.khyber.language",
        "charset",
        "path",
    };
    
    public GutenbergIndexer() {}
    
    public void indexEbooks(Path indexPath) throws IOException {
        Stream<Path> ebookFiles = Files.walk(Paths.get(GUTENBERG_DIRECTORY));
        List<String> lines = 
                ebookFiles.map(path -> new GutenbergEbook(path).getFields())
                          .collect(Collectors.toList());
        ebookFiles.close(); // not sure why I have to close it
        lines.add(String.join(",", FIELD_NAMES));
        MyFiles.write(indexPath, lines);
    }
    
    public void indexEbooks() throws IOException {
        indexEbooks(Paths.get("gutenbergIndex.csv"));
    }
    
}
