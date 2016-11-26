package sen.khyber.ai.ml.nlp.ner.persons;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import sen.khyber.io.MyFiles;
import sen.khyber.io.MyFilesNoExceptions;
import sen.khyber.regex.RegexUtils;
import sen.khyber.stats.counter.WordCounter;

/**
 * finds the human (only capitalized Latin) nameCandidates in text
 * 
 * @author Khyber Sen
 */
public class PersonNameFinder {
    
    private static final String CAPITALIZED_WORD_PATTERN = "[A-Z][a-z]*";
    
    private static final String NAME_DATABASE_DIRECTORY = "personNameDatabases/";
    
    /**
     * lookup dictionary for real names
     */
    private final Set<String> realNames = new HashSet<>();
    
    /**
     * creates a new PersonNameFinder based on the specified name database
     * 
     * @param nameDatabases
     *            file paths of text files containing real human names
     */
    public PersonNameFinder(final Path... nameDatabases) {
        for (final Path nameDatabase : nameDatabases) {
            realNames.addAll(MyFilesNoExceptions.readLines(nameDatabase));
        }
    }
    
    private static Path[] getNameDatabasePaths() {
        final File nameDatabaseDirectory = new File(NAME_DATABASE_DIRECTORY);
        final File[] nameDatabaseFiles = nameDatabaseDirectory.listFiles();
        final Path[] nameDatabasePaths = new Path[nameDatabaseFiles.length];
        for (int i = 0; i < nameDatabaseFiles.length; i++) {
            nameDatabasePaths[i] = Paths.get(nameDatabaseFiles[i].toURI());
        }
        return nameDatabasePaths;
    }
    
    public PersonNameFinder() {
        this(getNameDatabasePaths());
    }
    
    public boolean isName(final String nameCandidate) {
        return realNames.contains(nameCandidate);
    }
    
    public List<String> findNames(final CharSequence text) {
        final List<String> nameCandidates = RegexUtils.findMatches(text, CAPITALIZED_WORD_PATTERN);
        final List<String> verifiedNames = new ArrayList<>(nameCandidates.size() / 4);
        for (final String nameCandidate : nameCandidates) {
            if (realNames.contains(nameCandidate)) {
                verifiedNames.add(nameCandidate);
            }
        }
        return verifiedNames;
    }
    
    public void filter(final Iterable<String> nameCandidates) {
        final Iterator<String> iter = nameCandidates.iterator();
        while (iter.hasNext()) {
            if (!realNames.contains(iter.next())) {
                iter.remove();
            }
        }
    }
    
    public static void lesMiserablesTest() throws IOException {
        final Path path = Paths.get("C:/Users/kkyse/OneDrive/CS/Eclipse/git/Khyber/"
                + "personNameDatabases/CSV_Database_of_First_Names.csv");
        final PersonNameFinder nameFinder = new PersonNameFinder(path);
        final String text = MyFiles.read(Paths.get("Les Miserables.txt"));
        //List<String> names = nameFinder.findNames(text);
        //WordCounter counter = new WordCounter(names);
        //counter.sortedCounts().forEach(System.out::println);
        final WordCounter counter = new WordCounter();
        //counter.setAllLowerCase(false);
        counter.addWords(text);
        nameFinder.filter(counter);
        counter.mostCommonCounts().forEach(System.out::println);
        System.out.println(counter.mostCommonCounts(100));
        System.out.println();
        final WordCounter lesMiserables = new WordCounter();
        lesMiserables.add(text);
        //lesMiserables.retainIf(s -> s.length() > 10);
        System.out.println(lesMiserables.mostCommonCounts(100));
        //System.out.println(lesMiserables.toBarGraph(100));
        lesMiserables.mostCommonPercentages(10).forEach(System.out::println);
    }
    
    public static void main(final String[] args) throws IOException {
        lesMiserablesTest();
    }
    
}
