package sen.khyber.misc;

import sen.khyber.stats.counter.WordCounter;
import sen.khyber.web.Internet;

import java.nio.file.Paths;

public class Misc {
    
    public static void main(final String[] args) throws Exception {
        final WordCounter words = WordCounter.fromCSV(Paths.get("C:/Users/kkyse/OneDrive/CS/Eclipse/git/Khyber/gutenbergEnglishWordCounts.csv"));
        System.out.println(words.size());
        System.out.println(words.length());
    }
    
}
