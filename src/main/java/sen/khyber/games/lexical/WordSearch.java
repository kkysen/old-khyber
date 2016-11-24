package sen.khyber.games.lexical;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class WordSearch {
    
    // alphabet
    private static final char[] UPPERCASE_ENGLISH_ALPHABET = //
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    
    private static final char FILL_CHAR = '-';
    
    // directions [vertStep, horizStep]
    
    // easy
    private static final int[] RIGHT = new int[] {0, 1};
    private static final int[] DOWN = new int[] {1, 0};
    
    // medium
    private static final int[] DOWN_RIGHT = new int[] {1, 1};
    private static final int[] UP_RIGHT = new int[] {-1, 1};
    
    // hard
    private static final int[] LEFT = new int[] {0, -1};
    private static final int[] UP = new int[] {-1, 0};
    private static final int[] DOWN_LEFT = new int[] {1, -1};
    private static final int[] UP_LEFT = new int[] {-1, -1};
    
    private static final int[][] DIRECTIONS = new int[][] {
        RIGHT, DOWN, DOWN_RIGHT, UP_RIGHT, LEFT, UP, DOWN_LEFT, UP_LEFT
    };
    
    private static final Comparator<String> REVERSE_LENGTH_AND_NATURAL_ORDER = //
            new ReverseLengthAndNaturalComparator();
    
    private static final class ReverseLengthAndNaturalComparator implements Comparator<String> {
        
        @Override
        public int compare(final String s1, final String s2) {
            final int len1 = s1.length();
            final int len2 = s2.length();
            if (len1 == len2) {
                // if same length, use natural order
                return s1.compareTo(s2);
            }
            return len2 - len1;
        }
        
    }
    
    private static final int MAX_ATTEMPTS = 1000;
    
    private static final Random seedGenerator = new Random();
    
    private final Random random;
    
    private final char[] alphabet;
    private final List<String> words;
    private final List<String> addedWords;
    private final Map<String, List<int[]>> wordLocations;
    
    private final int height;
    private final int width;
    private final char[][] board;
    private final char[][] boardKey;
    
    public WordSearch(final int height, final int width, final Collection<String> wordsColl,
            final long seed) {
        this.height = height;
        this.width = width;
        board = new char[height][width];
        boardKey = new char[height][];
        words = filterWords(wordsColl);
        addedWords = new ArrayList<>();
        wordLocations = new TreeMap<>(REVERSE_LENGTH_AND_NATURAL_ORDER);
        this.random = new Random(seed);
        alphabet = UPPERCASE_ENGLISH_ALPHABET;
        insertWords();
    }
    
    public WordSearch(final int height, final int width, final Path path, final long seed)
            throws IOException {
        this(height, width, Files.readAllLines(path), seed);
    }
    
    private List<String> filterWords(final Collection<String> words) {
        final int maxLength = Math.max(height, width);
        return words.parallelStream()
                .filter(s -> s.length() <= maxLength)
                .map(String::toUpperCase)
                .filter(s -> {
                    for (int i = 0; i < s.length(); i++) {
                        final char c = s.charAt(i);
                        if (c < 'A' || c > 'Z') {
                            return false;
                        }
                    }
                    return true;
                }).collect(Collectors.toList());
    }
    
    private int randomI() {
        return random.nextInt(height);
    }
    
    private int randomJ() {
        return random.nextInt(width);
    }
    
    private char randomChar() {
        return alphabet[random.nextInt(alphabet.length)];
    }
    
    private int[] randomDirection() {
        return DIRECTIONS[random.nextInt(8)];
    }
    
    private boolean inBounds(final int startIndex, int length, final int step,
            final int bound) {
        // decrement length b/c need to check index right before length, which is out of bounds
        final int lastIndex = startIndex + --length * step;
        return lastIndex >= 0 && lastIndex < bound;
    }
    
    private boolean isValidInsertion(int i, int j, final char[] wordChars,
            final int vertStep, final int horizStep) {
        final int length = wordChars.length;
        
        // height check
        if (!inBounds(i, length, vertStep, height)) {
            return false;
        }
        
        // width check
        if (!inBounds(j, length, horizStep, width)) {
            return false;
        }
        
        // check to see if any intersections with previous words
        // if same letter as new word, still okay, it just overlaps
        for (int k = 0; k < length; k++) {
            if (board[i][j] != 0 && board[i][j] != wordChars[k]) {
                return false;
            }
            i += vertStep;
            j += horizStep;
        }
        
        return true;
    }
    
    private boolean insertWord(int i, int j, final String word, final int[] direction) {
        final int vertStep = direction[0];
        final int horizStep = direction[1];
        
        //System.out.println(i + " " + j + " " + word.length() + " " + vertStep + " " + horizStep);
        
        final char[] wordChars = word.toCharArray();
        if (!isValidInsertion(i, j, wordChars, vertStep, horizStep)) {
            return false;
        }
        
        // add word and now empty list of coordinates to wordLocations
        final List<int[]> coordinates = new ArrayList<>(wordChars.length);
        wordLocations.put(word, coordinates);
        
        // actually insert the word
        for (final char c : wordChars) {
            board[i][j] = c;
            // add the coordinate
            coordinates.add(new int[] {i, j});
            i += vertStep;
            j += horizStep;
        }
        
        // to signify insertion succeeded
        return true;
    }
    
    private void insertWord(final String word) {
        // if it cannot insert a word in MAX_ATTEMPTS, it skips it
        for (int i = 0; !insertWord(randomI(), randomJ(), word, randomDirection())
                && i < MAX_ATTEMPTS; i++) {}
    }
    
    // fills main board
    private void fillRemainingInBoardRandomly() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (board[i][j] == 0) {
                    board[i][j] = randomChar();
                }
            }
        }
    }
    
    // fills boardKey
    private void fillRemainingInKeyWith(final char fillChar) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (boardKey[i][j] == 0) {
                    boardKey[i][j] = fillChar;
                }
            }
        }
    }
    
    private void insertWords() {
        // sort the words so that longer ones come first
        // this will probably make it easier to insert all the words
        words.sort(REVERSE_LENGTH_AND_NATURAL_ORDER);
        
        // fill board with words
        words.forEach(this::insertWord);
        
        // copy to boardKey
        for (int i = 0; i < height; i++) {
            boardKey[i] = board[i].clone();
        }
        
        fillRemainingInBoardRandomly();
        
        // set addedWords to words from wordLocations, because some might have been skipped
        addedWords.addAll(wordLocations.keySet());
    }
    
    private String charBoardToString(final char[][] board) {
        // for better performance
        final StringBuilder sb = new StringBuilder(height * width * 2);
        for (int i = 0; i < height; i++) {
            final char[] joinedRow = new char[width * 2];
            for (int j = 0; j < width; j++) {
                int index = j * 2;
                joinedRow[index] = board[i][j];
                // fill every other with ' '
                joinedRow[++index] = ' ';
            }
            // change extra ' ' to '\n'
            joinedRow[joinedRow.length - 1] = '\n';
            sb.append(joinedRow);
        }
        // delete extra '\n' at end
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return charBoardToString(board);
    }
    
    public String keyToString() {
        fillRemainingInKeyWith(FILL_CHAR);
        return charBoardToString(boardKey);
    }
    
    public int numWords() {
        return addedWords.size();
    }
    
    public List<String> getOriginalWords() {
        return new ArrayList<>(words);
    }
    
    public List<String> getAddedWords() {
        return new ArrayList<>(addedWords);
    }
    
    public Map<String, List<int[]>> findWords() {
        return new TreeMap<>(wordLocations);
    }
    
    public List<String> findPrintableWords() {
        final List<String> printableWordLocations = new ArrayList<>(wordLocations.size());
        for (final String word : wordLocations.keySet()) {
            final StringJoiner sj = new StringJoiner(", ", word + ": ", "");
            wordLocations.get(word)
                    .forEach(coords -> sj.add("(" + coords[0] + ", " + coords[1] + ")"));
            printableWordLocations.add(sj.toString());
        }
        return printableWordLocations;
    }
    
    private static void printSeed(final long seed) {
        System.out.println("Seed: " + seed);
    }
    
    private void print() {
        System.out.println("\nWordSearch:\n" + this);
        System.out.println("\nWords (" + numWords() + "):");
        getAddedWords().forEach(System.out::println);
    }
    
    private void printKey() {
        System.out.println("\nWord Search Key:\n" + keyToString());
        System.out.println("\nExact Locations (" + numWords() + " words):");
        findPrintableWords().forEach(System.out::println);
    }
    
    private static void runWordSearch(final int height, final int width, final Path path,
            final long seed, final boolean shouldPrintKey) {
        WordSearch wordSearch = null;
        try {
            wordSearch = new WordSearch(height, width, path, seed);
        } catch (final IOException e) {
            throwError(e);
        }
        printSeed(seed);
        wordSearch.print();
        if (shouldPrintKey) {
            wordSearch.printKey();
        }
    }
    
    private static void throwInstructions() {
        final String instructions = "Usage: java WordSearch height width words_file_path [random_seed [key]]";
        System.out.println(instructions);
        System.exit(1);
    }
    
    private static void throwError(final Throwable e) {
        System.out.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(1);
    }
    
    private static void throwError(final String name, final Object obj, final String expected) {
        System.out.println(name + " Error: given: " + obj.toString() + "; expected: " + expected);
        System.exit(1);
    }
    
    private static void throwMissingWidthError() {
        throwError("Missing Width", "nothing", "a non-negative integer");
    }
    
    private static void throwMissingPathError() {
        throwError("Missing Path", "nothing", "the path of the words file");
    }
    
    private static void throwExtraArgsError(final String[] args, final int maxArgs) {
        final String[] extraArgs = Arrays.copyOfRange(args, maxArgs, args.length);
        final String joinedArgs = String.join(" ", extraArgs);
        throwError("Extra Args", joinedArgs, "nothing");
    }
    
    private static void checkArgsLength(final String[] args) {
        switch (args.length) {
            case 0:
                throwInstructions();
            case 1:
                throwMissingWidthError();
            case 2:
                throwMissingPathError();
        }
        if (args.length > 5) {
            throwExtraArgsError(args, 5);
        }
    }
    
    private static void throwDimensionError(final String name, final Object arg) {
        throwError(name, arg, "a non-negative integer");
    }
    
    private static int checkDimension(final String name, final String arg) {
        int length = 0;
        try {
            length = Integer.parseInt(arg);
        } catch (final NumberFormatException e) {
            throwDimensionError(name, arg);
        }
        if (length < 0) {
            throwDimensionError(name, length);
        }
        return length;
    }
    
    private static Path checkPath(final String arg) {
        try {
            return Paths.get(arg);
        } catch (final InvalidPathException e) {
            throwError(e);
            return null; // won't happen
        }
    }
    
    private static void checkKeyAndSeed(final boolean correctSeed, final boolean correctKey, final String[] args) {
        if (correctKey) {
            return;
        }
        if (args.length == 4) {
            throwError("Seed or Key", args[3], "a long, \"key\", or nothing");
        }
        if (args.length == 5) {
            if (correctSeed && !correctKey) {
                throwError("Key", args[4], "\"key\" or nothing");
            } else {
                throwError("Seed", args[3], "a long or nothing");
            }
        }
    }
    
    // cli args: 100 100 src/sen.khyber.language/lexicons/english.txt 7378022787950606786 key
    public static void main(final String[] args) throws Exception {
        checkArgsLength(args);
        
        final Scanner argsScanner = new Scanner(String.join(" ", args));
        
        final int height = checkDimension("Height", argsScanner.next());
        final int width = checkDimension("Width", argsScanner.next());
        final Path path = checkPath(argsScanner.next());
        
        final long seed;
        boolean receivedSeed = false;
        if (argsScanner.hasNextLong()) {
            seed = argsScanner.nextLong();
            receivedSeed = true;
        } else {
            seed = seedGenerator.nextLong();
        }
        
        final boolean shouldPrintKey = argsScanner.hasNext("key");
        
        checkKeyAndSeed(receivedSeed, shouldPrintKey, args);
        
        argsScanner.close();
        runWordSearch(height, width, path, seed, shouldPrintKey);
    }
    
}
