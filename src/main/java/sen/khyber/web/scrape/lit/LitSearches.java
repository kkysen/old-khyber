package sen.khyber.web.scrape.lit;

import sen.khyber.web.Internet;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jsoup.nodes.Document;

public class LitSearches {
    
    static final String URL = Lit.SEARCH_URL;
    
    private final Document doc;
    private Set<LitSearch> searches;
    
    public LitSearches() throws IOException {
        doc = Internet.getDocument(URL);
    }
    
    private void loadSearches() {
        if (searches != null) {
            return;
        }
        searches = doc.getElementsByTag("a")
                .parallelStream()
                .filter(elem -> elem.attr("href").contains("search.php?q="))
                .map(LitSearch::new)
                .collect(Collectors.toSet());
    }
    
    public Set<LitSearch> getSearchQueries() {
        loadSearches();
        return searches;
    }
    
    public Stream<LitSearch> getSearchResults() {
        loadSearches();
        return searches.parallelStream()
                .peek(search -> {
                    try {
                        search.loadResults();
                    } catch (final IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
    
    public static Set<LitSearch> searchForQueries(final int numPages) {
        final Set<LitSearch> searchQueries = ConcurrentHashMap.newKeySet();
        Stream.generate(() -> {
            try {
                return new LitSearches();
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        })
                .limit(numPages)
                .map(LitSearches::getSearchQueries)
                .forEach(searchQueries::addAll);
        return searchQueries;
    }
    
    public static String searchForQueriesHtml(final int numPages) {
        final Set<LitSearch> searches = searchForQueries(numPages);
        return searches.parallelStream()
                .sorted()
                .map(LitSearch::toHtml)
                .collect(Collectors.joining("<br>"));
    }
    
}
