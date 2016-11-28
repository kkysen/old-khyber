package sen.khyber.web.scrape.lit;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lombok.Getter;

public class LitSearchResult {
    
    private final Element searchResult;
    private final @Getter String title;
    private final @Getter String storyHref;
    private final @Getter String description;
    private final @Getter String author;
    private final @Getter String authorHref;
    private final @Getter String category;
    private final @Getter String categoryHref;
    private final @Getter String date;
    
    public LitSearchResult(final Element searchResult) {
        this.searchResult = searchResult;
        
        final Elements links = searchResult.getElementsByTag("a");
        
        final Element titleLink = links.get(0);
        title = titleLink.attr("title");
        storyHref = titleLink.attr("href");
        description = titleLink.ownText();
        
        final Element authorLink = links.get(1);
        author = authorLink.attr("title");
        authorHref = authorLink.attr("href");
        
        final Element categoryLink = links.get(2);
        category = categoryLink.attr("title");
        categoryHref = categoryLink.attr("href");
        
        final Element paragraph = searchResult.getElementsByTag("p").get(0);
        final String textWithDate = paragraph.ownText();
        date = textWithDate.substring(textWithDate.length() - 8);
    }

    @Override
    public String toString() {
        return "LitSearchResult [title=" + title + ", storyHref=" + storyHref + ", description="
                + description + ", author=" + author + ", authorHref=" + authorHref + ", category="
                + category + ", categoryHref=" + categoryHref + ", date=" + date + "]";
    }
    
    public String toHtml() {
        // TODO generate own html
        return searchResult.html();
    }
    
}
