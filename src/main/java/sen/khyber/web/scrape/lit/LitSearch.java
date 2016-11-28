package sen.khyber.web.scrape.lit;

import sen.khyber.web.Internet;

import java.io.IOException;
import java.util.StringJoiner;
import java.util.stream.Stream;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import lombok.Getter;

public class LitSearch implements Comparable<LitSearch> {
    
    private final Element hyperlink;
    private Document doc;
    private final @Getter String query;
    private final @Getter String url;
    
    private @Getter Stream<LitSearchResult> results;
    
    public LitSearch(final Element hyperlink) {
        this.hyperlink = hyperlink;
        query = hyperlink.attr("title");
        url = hyperlink.attr("href");
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (query == null ? 0 : query.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LitSearch other = (LitSearch) obj;
        if (query == null) {
            if (other.query != null) {
                return false;
            }
        } else if (!query.equals(other.query)) {
            return false;
        }
        return true;
    }
    
    @Override
    public int compareTo(final LitSearch otherSearch) {
        return query.compareTo(otherSearch.query);
    }
    
    public void loadDocument() throws IOException {
        doc = Internet.getDocument(LitSearches.URL + "/" + hyperlink.attr("href"));
    }
    
    public void loadResults() throws IOException {
        loadDocument();
        results = doc.getElementsByClass("r-56t")
                .parallelStream()
                .map(LitSearchResult::new);
    }
    
    public String resultsToHtml() {
        final StringJoiner sj = new StringJoiner("\n");
        getResults().map(LitSearchResult::toString).forEach(sj::add);
        return sj.toString();
    }
    
    public String toHtml() {
        return "<a href=\"" + LitSearches.URL + "/" + url + "\">" + query + "</a>";
    }
    
    @Override
    public String toString() {
        return "LitSearch [query=" + query + ", url=" + url + "]";
    }
    
}
