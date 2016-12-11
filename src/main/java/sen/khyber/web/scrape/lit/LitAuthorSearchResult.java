package sen.khyber.web.scrape.lit;

import java.io.IOException;

import org.jsoup.nodes.Element;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class LitAuthorSearchResult extends LitSearch {
    
    public LitAuthorSearchResult(final Element link) {
        super(link);
    }
    
    public LitAuthorSearchResult(final String csv) {
        super(csv);
    }
    
    @Override
    protected String getType() {
        return "author";
    }
    
    @Override
    protected String getWholeUrl(final String url) {
        return "https:" + url + "&page=submissions";
    }
    
    public LitAuthor getAuthor() throws IOException {
        return new LitAuthor(url);
    }
    
}

