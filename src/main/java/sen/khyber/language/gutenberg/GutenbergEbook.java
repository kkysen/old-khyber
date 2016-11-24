package sen.khyber.language.gutenberg;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

import sen.khyber.io.FileLineIterator;
import sen.khyber.io.MyFiles;

/**
 * represents a Gutenberg Ebook's metadata
 * 
 * @author Khyber Sen
 */
public class GutenbergEbook {
    
    private static final String[] FIELD_NAMES = {
        "Title",
        "Author",
        "Posting Date",
        "Release Date",
        "Last updated",
        "Lexicon",
        "Character set encoding",
    };
    
    private static final String TEXT_START_PATTERN = 
            "\\*\\*\\* START OF THIS PROJECT GUTENBERG EBOOK [^\\*]*\\*\\*\\*\\s*";
    
    private final FileLineIterator lines;
    private final String path;
    
    private Set<String> fieldNames = new HashSet<>(Arrays.asList(FIELD_NAMES));
    private String fields;
    private String text;
    private boolean startedText = false;
    
    public GutenbergEbook(Path path) {
        lines = MyFiles.readLinesAsIterable(path).iterator();
        this.path = path.toAbsolutePath().toString();
    }
    
    private static String getFieldFromLine(String line, String fieldName) {
        if (line.contains("[")) {
            line = line.replace("[", "").replace("]", "");
        }
        String fieldValue = line.substring(fieldName.length() + 2);
        if (fieldValue.contains("EBook")) {
            int endIndex = fieldValue.lastIndexOf("EBook") - 1; // for space
            fieldValue = fieldValue.substring(0, endIndex);
        }
        return fieldValue;
    }
    
    //private static 
    
    private String getFieldFromLines(String fieldName) {
        String line;
        while (lines.hasNext()) {
            line = lines.next();
            System.out.println(line);
            if (line.contains(": ")) {
                if (line.contains(fieldName)) {
                    //System.out.println(line);
                    return getFieldFromLine(line, fieldName);
                }
            }
            if (line.matches(TEXT_START_PATTERN)) {
                startedText = true;
                break;
            }
        }
        return "";
    }
    
    private void memoizeFields() {
        StringJoiner sj = new StringJoiner(",");
        for (String fieldName : FIELD_NAMES) {
            sj.add(getFieldFromLines(fieldName));
        }
        sj.add(path);
        fields = sj.toString();
    }
    
    public String getFields() {
        if (fields == null) {
            memoizeFields();
        }
        return fields;
    }
    
    private void memoizeText() {
        if (fields == null) {
            memoizeFields();
        }
        if (!startedText) {
            String line;
            while (lines.hasNext()) {
                line = lines.next();
                if (line.matches(TEXT_START_PATTERN)) {
                    break;
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        while (lines.hasNext()) {
            sb.append(lines.next());
        }
        text = sb.toString();
    }
    
    public String getText() {
        if (text == null) {
            memoizeText();
        }
        return text;
    }
    
    public static void main(String[] args) {
        //System.out.println("*** START OF THIS PROJECT GUTENBERG EBOOK MARTIN LUTHER'S 95 THESES ***\r\n".matches(TEXT_START_PATTERN));
        Path path = Paths.get("C:/Users/kkyse/OneDrive/CS/274.txt");
        GutenbergEbook book = new GutenbergEbook(path);
        System.out.println(book.getFields());
    }
    
}
