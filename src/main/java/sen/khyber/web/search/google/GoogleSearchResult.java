package sen.khyber.web.search.google;

import java.io.IOException;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import lombok.Getter;

import sen.khyber.web.Internet;

/**
 * a single Google search result
 * 
 * @author Khyber Sen
 */
public class GoogleSearchResult {
    
    private @Getter String name;
    private @Getter String url;
    private @Getter String description;
    
    protected GoogleSearchResult(Element element) {
        
        // get href
        Element h3 = element.getElementsByTag("h3").first();
        Element href = h3.getElementsByTag("a").first();
        
        // get name
        name = href.text();
        
        // get url
        url = href.attr("href");
        if (url.startsWith("/")) {
            url = href.attr("data-href");
        }
        
        // get description
        description = element.getElementsByClass("st").text();
        
    }
    
    public Document getDocument() throws IOException {
        return Internet.getDocument(url);
    }

    @Override
    public String toString() {
        return "GoogleSearchResult [name=" + name + ", url=" + url
            + ", description=" + description + "]";
    }
    
}
