package sen.khyber.web.scrape.lit;

import sen.khyber.web.Internet;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * 
 * 
 * @author Khyber Sen
 */
public abstract class LitSearches<S extends LitSearch> {
    
    protected final Document doc;
    protected Set<S> searches;
    
    public LitSearches() throws IOException {
        doc = Internet.getDocument(getUrl());
    }
    
    protected abstract String getUrl();
    
    protected abstract String getFilterString();
    
    protected abstract Function<Element, S> getLitSearchConstructorFunction();
    
    protected void loadSearches() {
        if (searches != null) {
            return;
        }
        searches = doc.getElementsByTag("a")
                .parallelStream()
                .filter(elem -> elem.attr("href").contains(getFilterString()))
                .map(getLitSearchConstructorFunction())
                .collect(Collectors.toSet());
    }
    
    protected Stream<S> streamSearches() {
        return doc.getElementsByTag("a")
                .parallelStream()
                .filter(elem -> elem.attr("href").contains(getFilterString()))
                .map(getLitSearchConstructorFunction());
    }
    
    public Set<S> getSearches() {
        loadSearches();
        return new HashSet<>(searches);
    }
    
}
