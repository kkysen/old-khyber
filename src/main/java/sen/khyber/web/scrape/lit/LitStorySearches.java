package sen.khyber.web.scrape.lit;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jsoup.nodes.Element;

public class LitStorySearches extends LitSearches<LitStorySearch> {
    
    private static final String URL = Lit.SEARCH_QUERY_URL + "type=story";
    
    public LitStorySearches() throws IOException {}
    
    @Override
    protected String getUrl() {
        return URL;
    }

    @Override
    protected String getFilterString() {
        return "search.php?q=";
    }

    @Override
    protected Function<Element, LitStorySearch> getLitSearchConstructorFunction() {
        return LitStorySearch::new;
    }
    
    public Set<LitStorySearch> getSearchQueries() {
        loadSearches();
        return searches;
    }
    
    public Stream<LitStorySearch> getSearchResults() {
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
    
    public static Set<LitStorySearch> searchForQueries(final int numPages) {
        final Set<LitStorySearch> searchQueries = ConcurrentHashMap.newKeySet();
        Stream.generate(() -> {
            try {
                return new LitStorySearches();
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        })
                .limit(numPages)
                .map(LitStorySearches::getSearchQueries)
                .forEach(searchQueries::addAll);
        return searchQueries;
    }
    
    public static String searchForQueriesHtml(final int numPages) {
        final Set<LitStorySearch> searches = searchForQueries(numPages);
        return searches.parallelStream()
                .sorted()
                .map(LitStorySearch::linkToHtml)
                .collect(Collectors.joining("<br>"));
    }
    
}
