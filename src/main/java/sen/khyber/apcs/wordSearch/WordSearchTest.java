package sen.khyber.apcs.wordSearch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class WordSearchTest {
    
    public static void allEnglishWordsTest() throws IOException {
        final Path path = Paths.get("src/sen.khyber.language/lexicons/english.txt");
        final List<String> words = Files.lines(path).filter(s -> s.matches("[a-z]*"))
                .collect(Collectors.toList());
        final WordSearch wordSearch = new WordSearch(5000, 5000, words, 1234567890987654321L);
        final List<String> outLines = wordSearch.findPrintableWords();
        outLines.add(0, "\n\n");
        outLines.addAll(0, Arrays.asList(wordSearch.toString().split("\n")));
        Files.write(Paths.get("AllEnglishWordsWordSearch.txt"), outLines);
    }
    
    public static void normalTest() throws IOException {
        final Path path = Paths.get("src/sen.khyber.apcs/wordSearch/words.txt");
        final WordSearch wordSearch = new WordSearch(7, 7, path, 1234567890987654321L);
        System.out.println(wordSearch.getAddedWords() + "\n");
        System.out.println(wordSearch + "\n");
        wordSearch.findPrintableWords().forEach(System.out::println);
    }
    
    public static void main(final String[] args) throws Exception {
        //allEnglishWordsTest();
        normalTest();
    }
    
}
