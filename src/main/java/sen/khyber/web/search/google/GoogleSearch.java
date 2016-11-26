package sen.khyber.web.search.google;

import sen.khyber.io.MyFiles;
import sen.khyber.web.Internet;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import lombok.Setter;

/**
 * a Google search
 * If you use this too much, Google will block you.
 * 
 * @author Khyber Sen
 */
public class GoogleSearch {
    
    /**
     * base URL for a Google search
     */
    private static final String GOOGLE_SEARCH_URL = "https://www.google.com/search?";
    
    /**
     * HTML element class that contains all the search results
     */
    private static final String SEARCH_RESULTS_CSS_CLASS = "rc";
    
    /**
     * search query
     */
    private final String search;
    
    /**
     * number of search results returned by Google
     */
    private final int numResults;
    
    private static final int DEFAULT_NUM_RESULTS = 10;
    
    /**
     * memoized Document of the Google search HTML page
     */
    private @Setter Document doc;
    
    /**
     * List of elements containing the actual search results
     */
    private List<Element> resultElements;
    
    /**
     * List of search results as GoogleSearchResults
     */
    private List<GoogleSearchResult> results;
    
    /**
     * creates a new GoogleSearch without doing the actual searching yet
     * 
     * @param search Google search query
     * @param numResults number of search results returned by Google
     */
    public GoogleSearch(final String search, final int numResults) {
        this.search = search;
        this.numResults = numResults;
    }
    
    /**
     * creates a new GoogleSearch with the default (10) numResults
     * 
     * @param search Google search query
     */
    public GoogleSearch(final String search) {
        this(search, DEFAULT_NUM_RESULTS);
    }
    
    /**
     * @param path the Path of the HTML file that should be of a Google search
     * @param numResults number of search results to return
     * @return a new GoogleSearch using a file instead of a URL
     * @throws IOException an IOException
     */
    public static GoogleSearch of(final Path path, final int numResults)
            throws IOException {
        final GoogleSearch search = new GoogleSearch(path.toString(), numResults);
        search.setDoc(Jsoup.parse(MyFiles.read(path)));
        return search;
    }
    
    /**
     * @see GoogleSearch#of(Path, int)
     * 
     * @param path the Path of the HTML file that should be of a Google search
     * @return a new GoogleSearch using a file instead of a URL
     * @throws IOException IOException an IOException
     */
    public static GoogleSearch of(final Path path) throws IOException {
        return of(path, DEFAULT_NUM_RESULTS);
    }
    
    private void memoizeDocument() throws IOException {
        doc = Internet.getRenderedDocument(GOOGLE_SEARCH_URL +
                "q=" + search + "&num=" + numResults);
    }
    
    public Document getDocument() throws IOException {
        if (doc == null) {
            memoizeDocument();
        }
        return doc;
    }
    
    private void memoizeResultElements() throws IOException {
        getDocument();
        resultElements = doc.getElementsByClass(SEARCH_RESULTS_CSS_CLASS);
    }
    
    public List<Element> getResultElements() throws IOException {
        if (resultElements == null) {
            memoizeResultElements();
        }
        return resultElements;
    }
    
    private void memoizeResults() throws IOException {
        getResultElements();
        results = new ArrayList<>(numResults);
        for (int i = 0; i < numResults; i++) {
            results.add(new GoogleSearchResult(resultElements.get(i)));
        }
    }
    
    /**
     * returns memoized results as a List of GoogleSearchResults
     * 
     * @return search results as a List of GoogleSearchResults
     * @throws IOException an IOException
     */
    public List<GoogleSearchResult> getResults() throws IOException {
        if (results == null) {
            memoizeResults();
        }
        return results;
    }
    
    /**
     * returns a Stream<Document> from the URLs
     * of all the results that pass the filter.
     * Implemented through Streams because fetching the Documents
     * requires waiting for other servers,
     * so it is well-suited for multithreading.
     * 
     * @see Internet#getDocuments(Stream)
     * @see Jsoup#connect(String)
     * 
     * @param filter a filter for the GoogleSearchResults
     * @return the Documents of the pages linked in the results
     *         that pass the filter
     * @throws IOException an IOException
     */
    public Stream<Document> getLinkedDocuments(final Predicate<GoogleSearchResult> filter)
            throws IOException {
        final Stream<String> urls = getResults().parallelStream()
                .filter(filter)
                .map(GoogleSearchResult::getUrl);
        return Internet.getDocuments(urls);
    }
    
    /**
     * @see #getLinkedDocuments(Predicate)
     * 
     * @return the Documents of the pages linked in the results
     * @throws IOException an IOException
     */
    public Stream<Document> getLinkedDocuments() throws IOException {
        return getLinkedDocuments(result -> true);
    }
    
    /**
     * @param filter
     * @return
     * @throws IOException
     */
    public Stream<Document> getRenderedLinkedDocuments(final Predicate<GoogleSearchResult> filter)
            throws IOException {
        final Stream<String> urls = getResults().parallelStream()
                .filter(filter)
                .map(GoogleSearchResult::getUrl);
        return Internet.getRenderedDocuments(urls);
    }
    
    /**
     * @return
     * @throws IOException
     */
    public Stream<Document> getRenderedLinkedDocuments() throws IOException {
        return getRenderedLinkedDocuments(result -> true);
    }
    
}
