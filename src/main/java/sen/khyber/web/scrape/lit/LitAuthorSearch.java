package sen.khyber.web.scrape.lit;

import sen.khyber.util.Timer;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jsoup.nodes.Element;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class LitAuthorSearch extends LitSearches<LitAuthorSearchResult> {
    
    private static final String URL = Lit.SEARCH_QUERY_URL + "type=member";
    
    public LitAuthorSearch() throws IOException {}
    
    private boolean isValid() {
        // estimate for html error message
        return doc.html().length() > 200;
    }
    
    public static LitAuthorSearch create() {
        LitAuthorSearch search;
        int tries = 0;
        do {
            tries++;
            try {
                search = new LitAuthorSearch();
            } catch (final IOException e) {
                search = null;
            }
        } while (search == null || !search.isValid());
        System.out.println(Timer.timeString() + ", tries: " + tries);
        return search;
    }
    
    public static Stream<LitAuthorSearch> generate(final int maxSize) {
        return Stream.generate(LitAuthorSearch::create)
                .limit(maxSize)
                .parallel();
    }
    
    @Override
    protected String getUrl() {
        return URL;
    }
    
    @Override
    protected String getFilterString() {
        return "memberpage.php";
    }
    
    @Override
    protected Function<Element, LitAuthorSearchResult> getLitSearchConstructorFunction() {
        return LitAuthorSearchResult::new;
    }
    
    public Stream<LitAuthor> getAuthors() {
        loadSearches();
        return searches.parallelStream().map(authorSearchResult -> {
            try {
                return authorSearchResult.getAuthor();
            } catch (final IOException e) {
                return null;
            }
        }).filter(author -> author != null);
    }
    
    public static Set<LitAuthorSearchResult> findResults(final int numSearches) throws IOException {
        System.out.println("starting...");
        // put it all in a set to remove duplicates
        // no Author constructor overhead cost
        final Supplier<Set<LitAuthorSearchResult>> concurrentSetSupplier = //
                () -> ConcurrentHashMap.newKeySet(numSearches * 20);
        Timer.time();
        final Set<LitAuthorSearchResult> authorSearchResults = generate(numSearches)
                .limit(numSearches)
                .flatMap(LitAuthorSearch::streamSearches)
                .collect(Collectors.toCollection(concurrentSetSupplier));
        System.out.println("found: " + authorSearchResults.size());
        return authorSearchResults;
    }
    
    public static Stream<LitAuthor> resultsToAuthors(final Set<LitAuthorSearchResult> results) {
        return results.parallelStream()
                .map(searchResult -> {
                    try {
                        return searchResult.getAuthor();
                    } catch (final IOException e) {
                        return null;
                    }
                })
                .filter(author -> author != null);
    }
    
    public static Stream<LitAuthor> findAuthors(final int numSearches) throws IOException {
        return resultsToAuthors(findResults(numSearches));
    }
    
}
