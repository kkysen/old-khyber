package sen.khyber.apcs.other;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * 
 * 
 * @author Khyber Sen
 * @author Daniel Roz
 */
public class DanielRozWordSearch {
    
    private final char[][] data;
    private final Random randgen;
    private static ArrayList<String> wordsToAdd = new ArrayList<String>();
    private static ArrayList<String> wordsAdded = new ArrayList<String>();
    
    private DanielRozWordSearch(final int rows, final int cols, final String file,
            final Random random) {
        data = new char[rows][cols];
        clear();
        randgen = random;
        loadWords(file);
        fillWithWords();
    }
    
    public DanielRozWordSearch(final int rows, final int cols, final String file) {
        this(rows, cols, file, new Random());
    }
    
    public DanielRozWordSearch(final int rows, final int cols, final String file,
            final int randomSeed,
            final boolean showKey) {
        this(rows, cols, file, new Random((long) Math.random() * 1000000));
        if (!showKey) {
            fillWithChars();
        }
    }
    
    private void clear() {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                set('_', i, j);
            }
        }
    }
    
    public char get(final int row, final int col) {
        return data[row][col];
    }
    
    public boolean set(final char newChar, final int row, final int col) {
        data[row][col] = newChar;
        return true;
    }
    
    @Override
    public String toString() {
        String ans = "";
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                ans += get(i, j) + " ";
            }
            ans += '\n';
        }
        return ans;
    }
    
    public boolean addWord(final String word, final int row, final int col, final int deltac,
            final int deltar) {
        int c = 0;
        if (deltar == 0 && deltac == 0) {
            return false;
        }
        for (int i = 0; i < word.length(); i++) {
            try {
                if (get(row - deltar * i, col + deltac * i) == '_'
                        || get(row - deltar * i, col + deltac * i) == word.charAt(i)) {
                    c++;
                }
            } catch (final IndexOutOfBoundsException e) {
                return false;
            }
        }
        if (c != word.length()) {
            return false;
        } else {
            for (int j = 0; j < word.length(); j++) {
                set(word.charAt(j), row - deltar * j, col + deltac * j);
            }
            return true;
        }
    }
    
    public static void loadWords(final String fileName) {
        try {
            final Scanner in = new Scanner(new File(fileName));
            String word;
            final int i = 0;
            while (in.hasNext()) {
                word = in.next();
                //System.out.println(word);
                wordsToAdd.add(i, word);
                //System.out.println(wordsToAdd);
            }
            in.close();
        } catch (final FileNotFoundException e) {
            System.out.println("Invalid file name or path");
            System.exit(1);
        }
    }
    
    public static void printWordList() {
        System.out.println("Words To Add: " + wordsToAdd);
        //System.out.println("Words Added: " + wordsAdded);
    }
    
    public void fillWithWords() {
        //loadWords("words.txt");
        int a, b, c, d, e;
        for (int i = 0; i < wordsToAdd.size(); i++) {
            e = 0;
            a = randgen.nextInt(data.length);
            b = randgen.nextInt(data[a].length);
            c = randgen.nextInt(3) - 1;
            d = randgen.nextInt(3) - 1;
            while (e < 100) {
                if (addWord(wordsToAdd.get(0), a, b, c, d)) {
                    addWord(wordsToAdd.get(0), a, b, c, d);
                    wordsAdded.add(wordsToAdd.get(0));
                    wordsToAdd.remove(0);
                    i--;
                    break;
                } else {
                    e++;
                    a = randgen.nextInt(data.length);
                    b = randgen.nextInt(data[0].length);
                    c = randgen.nextInt(3) - 1;
                    d = randgen.nextInt(3) - 1;
                }
                if (e == 100) {
                    System.out.println("Error: Words cannot fit in board. Please resize!");
                    System.exit(1);
                }
            }
        }
    }
    
    public void fillWithChars() {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if (get(i, j) == '_') {
                    set((char) (randgen.nextInt(26) + 97), i, j);
                }
            }
        }
    }
    
    public static void main(final String[] args) {
        //loadWords("words.txt");
        //printWordList();
        final DanielRozWordSearch w = new DanielRozWordSearch(20, 20, "words.txt", 1000, false);
        w.fillWithWords();
        System.out.println(w);
    }
    
}