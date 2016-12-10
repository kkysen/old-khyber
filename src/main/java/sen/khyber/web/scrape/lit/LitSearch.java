package sen.khyber.web.scrape.lit;

import org.jsoup.nodes.Element;

import lombok.Getter;

/**
 * 
 * 
 * @author Khyber Sen
 */
public abstract class LitSearch implements Comparable<LitSearch> {
    
    private final Element link;
    private final @Getter String query;
    protected final @Getter String url;
    private final String type;
    
    public LitSearch(final Element link) {
        this.link = link;
        query = link.attr("title");
        url = getWholeUrl(link.attr("href"));
        type = getType();
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + query.hashCode();
        result = prime * result + getType().hashCode();
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
        return query.equals(other.query);
    }
    
    @Override
    public int compareTo(final LitSearch otherSearch) {
        int cmp = query.compareTo(otherSearch.query);
        if (cmp == 0) {
            cmp = url.compareTo(otherSearch.url);
        }
        return cmp;
    }
    
    protected abstract String getType();
    
    protected abstract String getWholeUrl(String url);
    
    public String linkToHtml() {
        return "<a href=\"" + getUrl() + "\">" + query + "</a>";
    }

    @Override
    public String toString() {
        return "LitSearch [query=" + query + ", url=" + url + "]";
    }
    
}
