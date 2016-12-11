package sen.khyber.web.scrape.lit;

import sen.khyber.io.MyFiles;
import sen.khyber.web.Internet;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lombok.Getter;

public class LitStory implements Iterable<Document> {
    
    // TODO
    /*private static final Map<String, Integer> CATEGORY_RANKING = new HashMap<>();
    static {
        
    }*/
    
    public static final Comparator<LitStory> TITLE_ORDER = new TitleComparator();
    public static final Comparator<LitStory> AUTHOR_ORDER = new AuthorComparator();
    public static final Comparator<LitStory> RATING_ORDER = new RatingComparator().reversed();
    public static final Comparator<LitStory> DATE_ORDER = new DateComparator().reversed();
    public static final Comparator<LitStory> LENGTH_ORDER = new LengthComparator().reversed();
    public static final Comparator<LitStory> CATEGORY_ORDER = new CategoryComparator();
    
    private final @Getter LitAuthor author;
    private final @Getter String authorName;
    private final @Getter String title;
    private final @Getter String href;
    private final @Getter String description;
    private final @Getter String category;
    private final @Getter String categoryHref;
    private final @Getter String date;
    private final @Getter double rating;
    
    private final List<Document> pages = new ArrayList<>();
    private String story;
    
    LitStory(final Element fromAuthorElement, final LitAuthor author) {
        this.author = author;
        authorName = author.getName();
        
        final Elements rowData = fromAuthorElement.children();
        
        final Element titleAndRatingElem = rowData.get(0);
        final Element titleElem = titleAndRatingElem.child(0);
        title = titleElem.text();
        href = titleElem.attr("href");
        
        final String ratingEtc = titleAndRatingElem.ownText();
        final String ratingString = ratingEtc.substring(ratingEtc.length() - 5,
                ratingEtc.length() - 1);
        rating = ratingString.contains("x") ? 0 : Double.parseDouble(ratingString);
        
        final Element descriptionElem = rowData.get(1);
        description = descriptionElem.ownText().trim();
        
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
    
    private static class CategoryComparator implements Comparator<LitStory> {
        
        @Override
        public int compare(final LitStory story1, final LitStory story2) {
            return story1.category.compareTo(story2.category);
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
        final Element numPagesElem = storyDoc.getElementsByClass("b-pager-pagesText").get(0);
        final String numPagesEtc = numPagesElem.text();
        return Integer.parseInt(numPagesEtc.substring(0, numPagesEtc.indexOf(' ')));
    }
    
    public class PageIterator implements Iterator<Document> {
        
        private Document storyDoc;
        private Elements nextPageLinks;
        private String nextPageHref;
        private int pageNum;
        
        public PageIterator(final Document storyDoc, final int pageNum) {
            this.storyDoc = storyDoc;
            this.pageNum = pageNum;
            for (int i = 0; i < pageNum; i++) {
                pages.add(null);
            }
        }
        
        public PageIterator() throws IOException {
            this(getFirstPage(), 0);
            pages.add(storyDoc);
        }
        
        private boolean hasNextPage() {
            nextPageLinks = storyDoc.getElementsByClass("b-pager-next");
            return nextPageLinks.size() > 0;
        }
        
        @Override
        public boolean hasNext() {
            if (pages.size() > pageNum) {
                return true;
            }
            return hasNextPage();
        }
        
        @Override
        public Document next() {
            if (pages.size() > pageNum) {
                final Document page = pages.get(pageNum);
                if (page != null) {
                    pageNum++;
                    return page;
                }
            }
            if (!hasNextPage()) {
                throw new NoSuchElementException();
            }
            final Element nextPageLink = nextPageLinks.get(0);
            final String nextPageHref = nextPageLink.attr("href");
            try {
                storyDoc = Internet.getDocument(nextPageHref);
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
            pages.add(storyDoc);
            pageNum++;
            return storyDoc;
        }
        
    }
    
    @Override
    public Iterator<Document> iterator() {
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
        for (@SuppressWarnings("unused")
        final Document page : this) {}
    }
    
    public void clearPages() {
        pages.clear();
    }
    
    public List<Document> getPages() {
        loadPages();
        return new ArrayList<>(pages);
    }
    
    public String getStory() {
        if (story == null) {
            loadPages();
            story = pages
                    .parallelStream()
                    .map(Document::toString)
                    .collect(Collectors.joining("\n"));
        }
        return story;
    }
    
    public void clearStory() {
        story = null;
    }
    
    public void download(final Path parentDir) throws IOException {
        final String storyName = MyFiles.fixFileName(title + " by " + authorName);
        System.out.println("downloading " + storyName);
        final Path dir = Paths.get(parentDir.toString(), storyName);
        MyFiles.createDirectoryOverwriting(dir);
        MyFiles.write(Paths.get(dir.toString(), storyName + ".txt"), toString());
        try {
            loadPages();
        } catch (final RuntimeException e) {
            final Throwable cause = ExceptionUtils.getRootCause(e);
            if (cause instanceof SocketTimeoutException) {
                try {
                    System.out.println("pausing for 5 seconds");
                    TimeUnit.SECONDS.sleep(5);
                } catch (final InterruptedException e1) {
                    e1.printStackTrace();
                }
                throw (SocketTimeoutException) cause;
            }
        }
        for (int pageNum = 0; pageNum < pages.size(); pageNum++) {
            final Path path = Paths.get(dir.toString(),
                    "Page " + (pageNum + 1) + " - " + storyName + ".html");
            final String html = pages.get(pageNum).html();
            MyFiles.write(path, html);
        }
        clearPages(); // free up memory
        // FIXME encrypt directory
    }
    
    public void download() throws IOException {
        download(Paths.get(Lit.DOWNLOAD_DIR + "/stories"));
    }
    
    @Override
    public int hashCode() {
        return 31 * href.hashCode();
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
        final LitStory other = (LitStory) obj;
        return href.equals(other.href);
    }
    
    @Override
    public String toString() {
        return "LitStory [title=" + title + ", href=" + href + ", authorName=" + authorName
                + ", description=" + description + ", category=" + category + ", date=" + date
                + ", rating=" + rating + "]";
    }
    
}
