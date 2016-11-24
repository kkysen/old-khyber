package sen.khyber.stats.counter;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import sen.khyber.io.MyFiles;
import sen.khyber.web.Internet;

public class WordCounter extends Counter<String> implements IOCapableCounter {
    
    //private @Setter boolean isAllLowerCase = true;
    
    public WordCounter() {}
    
    public static WordCounter fromFile(final Path path) throws IOException {
        final WordCounter wordCounter = new WordCounter();
        wordCounter.addFile(path);
        return wordCounter;
    }
    
    @Override
    protected String toCSVLine(final String word, final AtomicLong count) {
        return word + "," + count;
    }
    
    @Override
    protected String toCSVLine(final Map.Entry<String, Long> count) {
        return count.getKey() + "," + count.getValue();
    }
    
    @Override
    protected String[] parseCSVLine(final String line) {
        return line.split(",");
    }
    
    public static WordCounter fromCSV(final Path path) throws IOException {
        final WordCounter wordCounter = new WordCounter();
        wordCounter.addCSV(path, s -> s);
        return wordCounter;
    }
    
    public static WordCounter fromWebsite(final URL url) throws IOException {
        final WordCounter wordCounter = new WordCounter();
        wordCounter.addWebsite(url);
        return wordCounter;
    }
    
    
    /*public static List<String> splitIntoWords(String unsplitWords) {
        List<String> words = new ArrayList<>();
        Pattern wordPattern = Pattern.compile("\\W+");
        Matcher wordMatcher = wordPattern.matcher(unsplitWords);
        while (wordMatcher.find()) {
            words.add(wordMatcher.group());
        }
        return words;
    }*/
    
    public static String[] splitIntoWords(final String unsplitWords) {
        return unsplitWords.split("\\W+");
    }
    
    /*@Override
    public boolean add(String s) {
        if (isAllLowerCase) {
            s = s.toLowerCase();
        
        return super.add(s);
    }*/
    
    public void addWords(final Iterable<String> words) {
        //words.forEach(s -> s.toLowerCase());
        addAll(words);
    }
    
    public void addWords(final String unsplitWords) {
        /*if (isAllLowerCase) {
            unsplitWords = unsplitWords.toLowerCase();
        }*/
        addAll(splitIntoWords(unsplitWords.toLowerCase()));
    }
    
    @Override
    public void addFile(final Path path) throws IOException {
        addWords(MyFiles.read(path));
    }
    
    public void addWebsite(final URL url) throws IOException {
        addWords(Internet.read(url));
    }
    
    public BarGraph<String> toBarGraph() {
        return toBarGraph(String.class);
    }
    
    public BarGraph<String> toBarGraph(final int numMostCommon) {
        return toBarGraph(String.class, numMostCommon);
    }
    
}
