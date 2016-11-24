package sen.khyber.web.search.google.tests;

import java.io.IOException;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jsoup.nodes.Document;

import sen.khyber.ai.ml.nlp.ner.persons.PersonNameFinder;
import sen.khyber.regex.RegexUtils;
import sen.khyber.web.search.google.GoogleSearch;
import sen.khyber.web.search.google.GoogleSearchResult;

/**
 * test for finding all the Disney Princesses with red hair
 * 
 * @author Khyber Sen
 */
public class RedHairedDisneyPrincessGoogleSearchTest {
    
    private GoogleSearch search;
    private List<GoogleSearchResult> results;
    private TreeSet<String> princesses;
    private Stream<Document> linkedPages;
    private Stream<String> linkedUrls;
    
    public RedHairedDisneyPrincessGoogleSearchTest() {
        search = new GoogleSearch("disney+princess+red+hair", 100);
    }
    
    public List<GoogleSearchResult> getResults() throws IOException {
        if (results == null) {
            results = search.getResults();
        }
        return results;
    }
    
    private static List<String> findPrincesses(String s) {
        return RegexUtils.findMatches(s, "Princess [A-Z][a-z]*");
    }
    
    public TreeSet<String> getPrincesses() throws IOException {
        if (princesses == null) {
            String resultsAsString = getResults().toString();
            princesses = new TreeSet<>(findPrincesses(resultsAsString));
        }
        return princesses;
    }
    
    public Stream<Document> getLinkedPages() throws IOException {
        if (linkedPages == null) {
            linkedPages = search.getRenderedLinkedDocuments(result -> {
                System.out.println("now rendering..." + result.getName());
                return result.toString().matches("[^P]*Princess [A-Z][a-z]*.*");
            });
        }
        return linkedPages;  
    }
    
    private static Stream<String> findPrincesses(Document doc) {
        return findPrincesses(doc.toString()).parallelStream();
    }
    
    public TreeSet<String> getLinkedPrincesses() throws IOException {
        return getLinkedPages().flatMap(doc -> findPrincesses(doc))
                               .collect(Collectors.toCollection(TreeSet::new));
    }
    
    private static Stream<String> findAllUrls(Stream<Document> docs) {
        return docs.flatMap(doc -> RegexUtils.findUrls(doc.html()).parallelStream());
    }
    
    public Stream<String> getLinkedUrls() throws IOException {
        if (linkedUrls == null) {
            linkedUrls = findAllUrls(getLinkedPages());
        }
        return linkedUrls;
    }
    
    public static void main(String[] args) throws Exception {
        RedHairedDisneyPrincessGoogleSearchTest search = 
                new RedHairedDisneyPrincessGoogleSearchTest();
        //search.getPrincesses().forEach(System.out::println);
        String princessesString = String.join(" ", search.getPrincesses())
                .replaceAll("Princess ", "");
        //NERs.identifyNers(princessesString).entrySet().forEach(System.out::println);
        
        PersonNameFinder nameFinder = new PersonNameFinder();
        new TreeSet<String>(nameFinder.findNames(princessesString)).forEach(System.out::println);
        
        //search.getLinkedPrincesses().forEach(System.out::println);
        //search.getResults().forEach(System.out::println);
        //search.getLinkedPages().forEach(System.out::println);
        //search.getLinkedUrls().forEach(System.out::println);
    }
    
}
