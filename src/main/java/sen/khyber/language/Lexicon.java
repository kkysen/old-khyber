package sen.khyber.language;

import sen.khyber.io.MyFiles;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import lombok.Getter;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class Lexicon {
    
    private final @Getter String name;
    private final Set<String> lexicon;
    private final @Getter Map<String, Double> letterFrequencies;
    
    private static Set<String> loadLexicon(final String lexiconPath, final boolean isConcurrent)
            throws IOException {
        final List<String> lines = MyFiles.readLines(Paths.get(lexiconPath));
        Set<String> lexicon;
        if (isConcurrent) {
            lexicon = ConcurrentHashMap.newKeySet(lines.size());
        } else {
            lexicon = new HashSet<>(lines.size());
        }
        lexicon.addAll(lines);
        return lexicon;
    }
    
    private static Map<String, Double> loadLetterFrequencies(final String letterFrequenciesPath)
            throws IOException {
        final Map<String, Double> letterFrequencies = new HashMap<>();
        final List<String> lines = MyFiles.readLines(Paths.get(letterFrequenciesPath));
        for (final String line : lines) {
            final String[] letterAndFrequency = line.split(",");
            final String letter = letterAndFrequency[0];
            final double frequency = Double.parseDouble(letterAndFrequency[1]);
            letterFrequencies.put(letter, frequency);
        }
        return letterFrequencies;
    }
    
    public Lexicon(final String name, final String lexiconPath,
            final String letterFrequenciesPath, final boolean isConcurrent) throws IOException {
        this.name = name;
        this.lexicon = loadLexicon(lexiconPath, isConcurrent);
        this.letterFrequencies = loadLetterFrequencies(letterFrequenciesPath);
    }
    
    public Lexicon(final Language language, final boolean isConcurrent) throws IOException {
        this(language.getName(), language.getLexiconPath(), language.getLetterFrequenciesPath(),
                isConcurrent);
    }
    
    public Lexicon(final Language language) throws IOException {
        this(language, false);
    }
    
    public static Lexicon safe(final String name, final String lexiconPath,
            final String letterFrequenciesPath, final boolean isConcurrent) {
        try {
            return new Lexicon(name, lexiconPath, letterFrequenciesPath, isConcurrent);
        } catch (final IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    public static Lexicon safe(final Language language, final boolean isConcurrent) {
        try {
            return new Lexicon(language, isConcurrent);
        } catch (final IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    public static Lexicon safe(final Language language) {
        try {
            return new Lexicon(language);
        } catch (final IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    public Set<String> getLexicon() {
        return new HashSet<>(lexicon);
    }
    
    // for use when time is constrained
    public Set<String> getRawLexicon() {
        return lexicon;
    }
    
    public boolean isWord(final String word) {
        return lexicon.contains(word);
    }
    
    public boolean isWordIgnoreCase(final String word) {
        return isWord(word.toLowerCase());
    }
    
    public List<String> matches(final String pattern) {
        return lexicon.parallelStream()
                .filter(s -> s.matches(pattern))
                .collect(Collectors.toList());
    }
    
}
