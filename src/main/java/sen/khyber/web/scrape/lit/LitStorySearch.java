package sen.khyber.web.scrape.lit;

import sen.khyber.web.Internet;

import java.io.IOException;
import java.util.stream.Stream;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class LitStorySearch extends LitSearch {
    
    private static final String URL = Lit.SEARCH_URL + "type=story";
    private Document doc;
    private Stream<LitStorySearchResult> results;
    
    public LitStorySearch(final Element link) {
        super(link);
    }
    
    @Override
    protected String getType() {
        return "story";
    }
    
    @Override
    protected String getWholeUrl(final String url) {
        return Lit.SEARCH_URL + url;
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