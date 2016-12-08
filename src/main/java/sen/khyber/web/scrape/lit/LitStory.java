package sen.khyber.web.scrape.lit;

import sen.khyber.web.Internet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lombok.Getter;

public class LitStory implements Iterable<String> {
    
    public static final Comparator<LitStory> TITLE_ORDER = new TitleComparator();
    public static final Comparator<LitStory> AUTHOR_ORDER = new AuthorComparator();
    public static final Comparator<LitStory> RATING_ORDER = new RatingComparator().reversed();
    public static final Comparator<LitStory> DATE_ORDER = new DateComparator().reversed();
    public static final Comparator<LitStory> LENGTH_ORDER = new LengthComparator().reversed();
    
    private final @Getter String author;
    private final @Getter String authorHref;
    private final @Getter int authorUid;
    private final @Getter String title;
    private final @Getter String href;
    private final @Getter String description;
    private final @Getter String category;
    private final @Getter String categoryHref;
    private final @Getter String date;
    private final @Getter double rating;
    
    private final List<String> pages = new ArrayList<>();
    private String story;
    
    public LitStory(final Element fromAuthorElement, final String author, final String authorHref,
            final int authorUid) {
        this.author = author;
        this.authorHref = authorHref;
        this.authorUid = authorUid;
        
        final Elements rowData = fromAuthorElement.children();
        
        final Element titleAndRatingElem = rowData.get(0);
        final Element titleElem = titleAndRatingElem.child(0);
        title = titleElem.text();
        href = titleElem.attr("href");
        
        final String ratingEtc = titleAndRatingElem.ownText();
        final String ratingString = ratingEtc.substring(ratingEtc.length() - 5,
                ratingEtc.length() - 1);
        rating = Double.parseDouble(ratingString);
        
        final Element descriptionElem = rowData.get(1);
        description = descriptionElem.ownText();
        
        final Element categoryElem = rowData.get(2).child(0);
        category = categoryElem.text();
        categoryHref = categoryElem.attr("href");
        
        final Element dateElem = rowData.get(3);
        date = dateElem.ownText();
    }
    
    private static class TitleComparator implements Comparator<LitStory> {
        
        @Override
        public int compare(final LitStory story1, final LitStory story2) {
            return story1.title.compareTo(story2.title);
        }
        
    }
    
    private static class AuthorComparator implements Comparator<LitStory> {
        
        @Override
        public int compare(final LitStory story1, final LitStory story2) {
            return story1.author.compareTo(story2.author);
        }
        
    }
    
    private static class RatingComparator implements Comparator<LitStory> {
        
        @Override
        public int compare(final LitStory story1, final LitStory story2) {
            return Double.compare(story1.rating, story2.rating);
        }
        
    }
    
    private static class DateComparator implements Comparator<LitStory> {
        
        @Override
        public int compare(final LitStory story1, final LitStory story2) {
            // FIXME
            return 0;
        }
        
    }
    
    private static class LengthComparator implements Comparator<LitStory> {
        
        @Override
        public int compare(final LitStory story1, final LitStory story2) {
            try {
                return story1.numPages() - story2.numPages();
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }
        
    }
    
    private Document getFirstPage() throws IOException {
        return Internet.getDocument(href);
    }
    
    public int numPages() throws IOException {
        if (pages.size() > 0) {
            return pages.size();
        }
        final Document storyDoc = getFirstPage();
        final Element numPagesElem = storyDoc.getElementsByClass("b-pager-pages").get(0);
        final String numPagesEtc = numPagesElem.text();
        return Integer.parseInt(numPagesEtc.substring(0, numPagesEtc.indexOf(' ')));
    }
    
    public class PageIterator implements Iterator<String> {
        
        private Document storyDoc;
        private final int pageNum;
        
        public PageIterator(final Document storyDoc, final int pageNum) {
            this.storyDoc = storyDoc;
            this.pageNum = pageNum;
        }
        
        public PageIterator() throws IOException {
            this(getFirstPage(), 1);
        }
        
        @Override
        public boolean hasNext() {
            final Elements nextPageLinks = storyDoc.getElementsByClass("b-pager-next");
            if (nextPageLinks.size() > 0) {
                final Element nextPageLink = nextPageLinks.get(0);
                final String nextPageHref = nextPageLink.attr("href");
                try {
                    storyDoc = Internet.getDocument(nextPageHref);
                } catch (final IOException e) {
                    throw new RuntimeException(e);
                }
                return true;
            }
            return false;
        }
        
        @Override
        public String next() {
            final String page = storyDoc.getElementsByClass("b-story-body-x x-r15").get(0).text();
            if (pageNum - 1 == pages.size()) {
                pages.add(page);
            }
            return page;
        }
        
    }
    
    @Override
    public Iterator<String> iterator() {
        if (pages.size() == 0) {
            try {
                return new PageIterator();
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return pages.iterator();
        }
    }
    
    private void loadPages() {
        if (pages.size() == 0) {
            for (final String page : this) {}
        }
    }
    
    public List<String> getPages() {
        loadPages();
        return new ArrayList<>(pages);
    }
    
    public String getStory() {
        if (story == null) {
            loadPages();
            story = String.join("\n", pages);
        }
        return story;
    }
    
    @Override
    public String toString() {
        return "LitStory [title=" + title + ", href=" + href + ", description="
                + description + ", rating=" + rating + ", author=" + author + ", authorHref="
                + authorHref + ", authorUid=" + authorUid + ", category=" + category
                + ", categoryHref=" + categoryHref + ", date=" + date + "]";
    }
    
}
