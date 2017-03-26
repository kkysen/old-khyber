package sen.khyber.web.scrape.lit;

import sen.khyber.web.Internet;

import java.io.IOException;
import java.util.stream.Stream;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class LitStorySearch extends LitSearch {
    
    private static final String BASE_URL = Lit.Url.SEARCH.getUrl();
    private static final String URL = BASE_URL + "type=story";
    
    private Document doc;
    private Stream<LitStorySearchResult> results;
    
    public LitStorySearch(final Element link) {
        super(link);
    }
    
    public LitStorySearch(final String csv) {
        super(csv);
    }
    
    @Override
    protected String getType() {
        return "story";
    }
    
    @Override
    protected String getWholeUrl(final String url) {
        return BASE_URL + url;
    }
    
    public void loadDocument() throws IOException {
        doc = Internet.getDocument(url);
    }
    
    public void loadResults() throws IOException {
        loadDocument();
        results = doc.getElementsByClass("r-56t")
                .parallelStream()
                .map(LitStorySearchResult::new);
    }
    
}