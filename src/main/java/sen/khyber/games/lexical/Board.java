package sen.khyber.games.lexical;

import sen.khyber.language.Lexicon;
import sen.khyber.util.linkedMatrix.LinkedMatrix;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import lombok.Getter;

/*
 * to do:
 * 
 * add mutation methods
 */

/**
 * 
 * 
 * @author Khyber Sen
 */
public class Board implements Cloneable {
    
    @SuppressWarnings("unchecked")
    private static final Class<List<List<String>>> RANDOM_MATRIX_TYPE = //
            (Class<List<List<String>>>) (Class<?>) ArrayList.class;
    
    private static final float DEFAULT_LOAD_FACTOR = 0.75f; // for ConcurrentHashMap
    
    private static final Deque<LinkedMatrix<String>.Node> EMPTY_PATH = new ArrayDeque<>();
    
    private int height;
    private int width;
    private LinkedMatrix<String> board;
    
    private PointValueMap pointValueMap;
    private int minLength;
    private int maxLength;
    
    private String letters;
    private String letterPattern;
    
    private Set<String> unfilteredLexicon;
    private Set<String> lexicon;
    private Set<String> prefixes;
    
    private Map<String, FoundWord> foundWords;
    
    private int numWords = 0;
    private int score = 0;
    private int difficulty = 0; // total distances
    
    private boolean solved = false;
    private boolean concurrent;
    
    private Class<? extends LinkedMatrix<String>> linkedMatrixType;
    private Supplier<String> randomLetterSupplier;
    
    private void setDimensions(final int height, final int width) {
        this.height = height;
        this.width = width;
    }
    
    private void setBoard(final LinkedMatrix<String> board) {
        this.board = board;
        setDimensions(board.getHeight(), board.getWidth());
    }
    
    private void setPointValueMap(final PointValueMap pointValueMap) {
        this.pointValueMap = pointValueMap;
        minLength = pointValueMap.getMinLength();
        maxLength = height * width;
    }
    
    private void setWords(final Lexicon language, final boolean isConcurrent) {
        unfilteredLexicon = language.getRawLexicon();
        concurrent = isConcurrent;
        if (concurrent) {
            foundWords = //
                    new ConcurrentHashMap<>(estimateNumWords(), DEFAULT_LOAD_FACTOR, maxLength);
            lexicon = ConcurrentHashMap.newKeySet(unfilteredLexicon.size() / 5); // estimate
            prefixes = ConcurrentHashMap.newKeySet(unfilteredLexicon.size()); // estimate
        } else {
            foundWords = new HashMap<>(estimateNumWords());
            lexicon = new HashSet<>(unfilteredLexicon.size() / 5); // estimate
            prefixes = new HashSet<>(unfilteredLexicon.size()); // estimate
        }
    }
    
    public Board(final LinkedMatrix<String> board, final PointValueMap pointValueMap,
            final Lexicon language, final boolean isConcurrent) {
        setBoard(board);
        setPointValueMap(pointValueMap);
        setWords(language, isConcurrent);
    }
    
    private int estimateNumWords() {
        return (int) Math.pow(maxLength, 2.5);
    }
    
    private Board() {} // for cloning
    
    @Override
    public Board clone() {
        final Board copy = new Board();
        copy.height = height;
        copy.width = width;
        copy.board = board.clone();
        copy.pointValueMap = pointValueMap;
        copy.minLength = minLength;
        copy.maxLength = maxLength;
        copy.letters = letters;
        copy.letterPattern = letterPattern;
        copy.unfilteredLexicon = unfilteredLexicon;
        copy.lexicon = lexicon;
        copy.prefixes = prefixes;
        copy.foundWords = foundWords;
        copy.numWords = numWords;
        copy.score = score;
        copy.difficulty = difficulty;
        copy.solved = solved;
        return copy;
    }
    
    public void reset() {
        if (concurrent) {
            foundWords = new ConcurrentHashMap<>(estimateNumWords(), DEFAULT_LOAD_FACTOR,
                    maxLength);
        } else {
            foundWords = new HashMap<>(estimateNumWords());
        }
        numWords = 0;
        score = 0;
        difficulty = 0;
        solved = false;
    }
    
    @Override
    public String toString() {
        return board.toString();
    }
    
    private void setRandomBoard(final Supplier<String> randomLetterSupplier,
            final Class<? extends LinkedMatrix<String>> linkedMatrixType)
            throws NoSuchMethodException, SecurityException, InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        this.randomLetterSupplier = randomLetterSupplier;
        this.linkedMatrixType = linkedMatrixType;
        final Constructor<? extends LinkedMatrix<String>> constructor = linkedMatrixType
                .getDeclaredConstructor(Supplier.class, int.class, int.class);
        final LinkedMatrix<String> randomLinkedMatrix = //
                constructor.newInstance(randomLetterSupplier, height, width);
        setBoard(randomLinkedMatrix);
    }
    
    public Board spawnRandom() {
        final Board child = clone();
        child.reset();
        child.board.randomizeElements(randomLetterSupplier);
        return child;
    }
    
    // random based on random supplier
    public Board(final int height, final int width,
            final Supplier<String> randomLetterSupplier,
            final Class<? extends LinkedMatrix<String>> linkedMatrixType,
            final PointValueMap pointValueMap,
            final Lexicon language, final boolean isConcurrent)
            throws InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {
        setDimensions(height, width);
        setPointValueMap(pointValueMap);
        setWords(language, isConcurrent);
        setRandomBoard(randomLetterSupplier, linkedMatrixType);
    }
    
    // random based off letter frequency
    public Board(final int height, final int width,
            final List<Pair<String, Double>> letterFrequencies,
            final Class<? extends LinkedMatrix<String>> linkedMatrixType,
            final PointValueMap pointValueMap,
            final Lexicon language, final boolean isConcurrent)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        this(height, width, new EnumeratedDistribution<>(letterFrequencies)::sample,
                linkedMatrixType, pointValueMap, language, isConcurrent);
    }
    
    private static <T, U> List<Pair<T, U>> mapToPairList(final Map<T, U> map) {
        final List<Pair<T, U>> list = new ArrayList<>(map.size());
        for (final T t : map.keySet()) {
            list.add(new Pair<T, U>(t, map.get(t)));
        }
        return list;
    }
    
    public Board(final int height, final int width,
            final Class<? extends LinkedMatrix<String>> linkedMatrixType,
            final PointValueMap pointValueMap,
            final Lexicon language, final boolean isConcurrent)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        this(height, width, mapToPairList(language.getLetterFrequencies()), linkedMatrixType,
                pointValueMap, language, isConcurrent);
    }
    
    protected String lexiconFilter(final String word) {
        if (word.matches(letterPattern)) {
            return word; // can possibly change word in filter
        }
        return null; // return null if does not pass filter
    }
    
    private void parseLetterPattern() {
        final StringBuilder sb = new StringBuilder(maxLength);
        for (final LinkedMatrix<String>.Node letter : board.toList()) {
            sb.append(letter);
        }
        letters = sb.toString();
        letterPattern = "[" + letters + "]*";
    }
    
    private void parsePrefix(final String word) {
        for (int i = 1; i < word.length()/* - 1 */; i++) { // CHECK
            prefixes.add(word.substring(0, i));
        }
    }
    
    private void parseLexicon() {
        parseLetterPattern();
        // if unfilteredLexicon is concurrent, should be faster
        unfilteredLexicon.forEach(word -> {
            word = lexiconFilter(word);
            if (word != null) {
                lexicon.add(word);
                parsePrefix(word);
            }
        });
    }
    
    public class FoundWord implements Comparable<FoundWord> {
        
        private final @Getter String word;
        private @Getter List<LinkedMatrix<String>.Node> path;
        
        private @Getter int length;
        private @Getter int distance;
        private @Getter int pointValue;
        
        private int calcDistance() {
            int distance = 0;
            for (int i = 1; i < path.size(); i++) {
                distance += path.get(i).distanceTo(path.get(i - 1));
            }
            return distance;
        }
        
        private void calcData() {
            length = word.length();
            pointValue = pointValueMap.getPointValue(length);
            calcDistance();
        }
        
        private void updateCounts() {
            numWords++;
            score += pointValue;
            difficulty += distance;
        }
        
        // TODO needed?
        public FoundWord(final String word) {
            this.word = word;
        }
        
        public FoundWord(final String word, final Deque<LinkedMatrix<String>.Node> oldPath,
                final LinkedMatrix<String>.Node lastLetter) {
            this.word = word;
            this.path = new ArrayList<>(oldPath);
            path.add(lastLetter);
            calcData();
            updateCounts();
        }
        
        @Override
        public int compareTo(final FoundWord other) {
            // reverse order
            final int diff = other.pointValue - pointValue;
            return diff == 0 ? other.length - length : diff;
        }
        
        @Override
        public String toString() {
            return "FoundWord [word=" + word + ", length=" + length
                    + ", distance=" + distance + ", pointValue=" + pointValue + ", path=" + path
                    + "]";
        }
        
        @Override
        public int hashCode() {
            return word.hashCode();
        }
        
        @Override
        public boolean equals(final Object obj) {
            return word.equals(obj);
        }
        
    }
    
    private void extendPrefix(final String prefix,
            final Deque<LinkedMatrix<String>.Node> oldPath,
            final LinkedMatrix<String>.Node lastLetter) {
        if (lexicon.contains(prefix)) {
            foundWords.put(prefix, new FoundWord(prefix, oldPath, lastLetter));
        }
        final Set<LinkedMatrix<String>.Node> neighbors = lastLetter.getNeighbors();
        for (final LinkedMatrix<String>.Node nextLetter : neighbors) {
            if (!oldPath.contains(nextLetter)) {
                final String extendedPrefix = prefix + nextLetter.getElement();
                if (prefixes.contains(extendedPrefix)) {
                    // Deque doesn't have a clone method for some reason
                    final ArrayDeque<LinkedMatrix<String>.Node> cloneablePath = //
                            (ArrayDeque<LinkedMatrix<String>.Node>) oldPath;
                    final Deque<LinkedMatrix<String>.Node> path = cloneablePath.clone();
                    path.add(lastLetter);
                    extendPrefix(extendedPrefix, path, nextLetter);
                }
            }
        }
    }
    
    private void extendLetter(final LinkedMatrix<String>.Node letter) {
        final String prefix = letter.getElement().toString();
        extendPrefix(prefix, EMPTY_PATH, letter);
    }
    
    private void extendAllLetters() {
        if (concurrent) {
            board.parallelStream().forEach(this::extendLetter);
        } else {
            for (final LinkedMatrix<String>.Node letter : board.toList()) {
                extendLetter(letter);
            }
        }
    }
    
    public void solve() {
        parseLexicon();
        extendAllLetters();
        solved = true;
    }
    
    public Collection<FoundWord> getFoundWords() {
        return foundWords.values();
    }
    
    public boolean containsWord(final String word) {
        return foundWords.containsKey(word);
    }
    
    // returns null if word doesn't exist
    public FoundWord extendWord(final String word) {
        // if already solved, then just return foundWord already calculated
        if (solved) {
            return foundWords.get(word);
        }
        // if not solved yet, then try to extend only this word
        if (!lexicon.contains(word)) {
            return null; // return null if this word is not a real word
        }
        
        // set lexicon, prefixes, and foundWords to only this word
        unfilteredLexicon = new HashSet<>(1); // used in lexicon parsing
        unfilteredLexicon.add(word);
        parseLexicon(); // also parses prefixes
        foundWords = new HashMap<>(); // concurrency not needed since only 1 word
        
        // call extendLetter to find words
        final List<LinkedMatrix<String>.Node> firstLetters = board.matchesOf(word.substring(0, 1));
        for (final LinkedMatrix<String>.Node letter : firstLetters) {
            extendLetter(letter);
        }
        final FoundWord result = foundWords.get(word);
        
        return result;
    }
    
    // TODO needed?
    /*public class UserWord extends FoundWord {
        
    }*/
    
    // TODO finish
    public void checkUserWord(String word) {
        word = word.toLowerCase();
        if (word.length() < minLength) {
            // TODO
        } else if (word.length() > letters.length()) {
            // TODO
        } else if (!lexicon.contains(word)) {
            // TODO
        } else if (!containsWord(word)) {
            // TODO
        }
    }
    
}
