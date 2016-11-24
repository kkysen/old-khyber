package sen.khyber.language.gutenberg;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import sen.khyber.io.MyFiles;
import sen.khyber.language.Language;
import sen.khyber.language.Lexicon;
import sen.khyber.stats.counter.CharCounter;
import sen.khyber.stats.counter.IOCapableCounter;
import sen.khyber.stats.counter.WordCounter;

public class GutenbergCounters {
    
    private static final String GUTENBERG_DIRECTORY = "C:/Users/kkyse/OneDrive/CS/www.gutenberg.lib.md.us";
    
    private static Stream<Path> gutenbergStream() throws IOException {
        return Files.walk(Paths.get(GUTENBERG_DIRECTORY), 1)
                .parallel()
                .filter(path -> path.toString().endsWith(".txt"));
    }
    
    public static void addFiles(final IOCapableCounter counter, final Stream<Path> paths) {
        final long startTime = System.currentTimeMillis();
        final AtomicInteger i = new AtomicInteger();
        paths.forEach(path -> {
            System.out.println(i.incrementAndGet() + ": now counting... " + path);
            try {
                counter.addFile(path);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
        System.out.println("time: " + (System.currentTimeMillis() - startTime));
    }
    
    public static void runCounter(final IOCapableCounter counter, final Path csv) throws IOException {
        addFiles(counter, gutenbergStream());
        counter.saveSorted(csv);
    }
    
    public static void runWordCounter() throws IOException {
        final WordCounter wordCounter = new WordCounter();
        runCounter(wordCounter, Paths.get("gutenbergWordCounts.csv"));
        final Lexicon english = new Lexicon(Language.ENGLISH);
        wordCounter.retainIf(english::isWord);
        wordCounter.saveSorted(Paths.get("gutenbergEnglishWordCounts.csv"));
    }
    
    public static void runCharCounter() throws IOException {
        final CharCounter charCounter = new CharCounter();
        runCounter(charCounter, Paths.get("gutenbergCharCounts.csv"));
        charCounter.retainIf(Character::isLetter);
        charCounter.saveSorted(Paths.get("gutenbergLetterCounts.csv"));
    }
    
    public static void runEnglishWordCounter() throws IOException {
        final WordCounter wordCounter = WordCounter.fromCSV(Paths.get("gutenbergEnglishWordCounts.csv"));
        System.out.println("size: " + wordCounter.size());
        System.out.println("length: " + wordCounter.length());
        //wordCounter.mostCommonPercentages().forEach(System.out::println);
        //Lexicon english = Lexicon.ENGLISH;
        //wordCounter.retainIf(english::isWord);
        //wordCounter.saveSorted(Paths.get("gutenbergEnglishWordCounts.csv"));
        //wordCounter.mostCommonCounts(100).forEach(System.out::println);
        final List<String> lines = new ArrayList<>();
        final AtomicInteger i = new AtomicInteger();
        wordCounter.sortedPercentages().forEach(count -> {
            lines.add(i.incrementAndGet() + "," + count.getKey() + "," + count.getValue() * 100);
        });
        MyFiles.write(Paths.get("gutenbergEnglishWordFrequencies.csv"), lines);
    }
    
    public static void main(final String[] args) throws Exception {
        final String s = MyFiles.read(Paths.get("gutenbergCharCounts.csv"));
        final CharCounter charCounter = CharCounter.fromCSV(Paths.get("gutenbergCharCounts.csv"));
        System.out.println("size: " + charCounter.size());
        System.out.println("length: " + charCounter.length());
        final List<String> lines = new ArrayList<>();
        final AtomicInteger i = new AtomicInteger();
        charCounter.sortedPercentages().forEach(count -> {
            lines.add(i.incrementAndGet() + "," + count.getKey() + "," + count.getValue() * 100);
        });
        MyFiles.write(Paths.get("gutenbergCharFrequencies.csv"), lines);
    }
    
}
