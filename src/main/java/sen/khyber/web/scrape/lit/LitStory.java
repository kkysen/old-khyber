package sen.khyber.web.scrape.lit;

import sen.khyber.io.MyFiles;
import sen.khyber.util.ByteBufferUtils;
import sen.khyber.web.Internet;
import sen.khyber.web.scrape.lit.Lit.Category;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@ExtensionMethod(ByteBufferUtils.class)
public class LitStory implements Iterable<Document> {
    
    private static final String URL = Lit.Url.STORY.getUrl();
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("M/d/yy");
    
    public static final LitStory DUMMY = new LitStory(null, "authorName", "title", "href",
            "description", null, null, -1);
    
    private final @Getter LitAuthor author;
    private final @Getter String authorName;
    private final @Getter String title;
    private final String url;
    private final @Getter String description;
    private final @Getter Category category;
    private final @Getter LocalDate date;
    private final @Getter double rating;
    
    public String getHref() {
        return URL + url;
    }
    
    public String getDateString() {
        return date.format(DATE_FORMATTER);
    }
    
    private final List<Document> pages = new ArrayList<>();
    private int numPages;
    private String story;
    
    LitStory(final Element fromAuthorElement, final LitAuthor author) {
        this.author = author;
        authorName = author.getName();
        
        final Elements rowData = fromAuthorElement.children();
        
        final Element titleAndRatingElem = rowData.get(0);
        final Element titleElem = titleAndRatingElem.child(0);
        title = titleElem.text();
        final String fullHref = titleElem.attr("href");
        final int startIndex = fullHref.lastIndexOf('/') + 1;
        url = fullHref.substring(startIndex, fullHref.length());
        
        final String ratingEtc = titleAndRatingElem.ownText();
        final String ratingString = ratingEtc.substring(ratingEtc.length() - 5,
                ratingEtc.length() - 1);
        rating = ratingString.contains("x") ? 0 : Double.parseDouble(ratingString);
        
        final Element descriptionElem = rowData.get(1);
        final String tempDescription = descriptionElem.ownText().trim();
        description = tempDescription == null ? "" : tempDescription;
        
        final Element categoryElem = rowData.get(2).child(0);
        final String categoryHref = categoryElem.attr("href");
        System.out.println(categoryHref);
        category = Category.fromUrl(categoryHref);
        if (category == null) {
            System.out.println(this);
        }
        
        final Element dateElem = rowData.get(3);
        date = LocalDate.parse(dateElem.ownText(), DATE_FORMATTER);
    }
    
    public LitStory(final ByteBuffer in, final LitAuthor author) {
        this.author = author;
        authorName = author.getName();
        
        category = Category.fromId(in.get());
        
        final int year = 2000 + in.get();
        final int month = in.get();
        final int day = in.get();
        date = LocalDate.of(year, month, day);
        
        rating = in.getFloat();
        
        url = in.getShortString();
        title = in.getShortString();
        description = in.getShortString();
    }
    
    private static final int BASE_SERIALIZED_LENGTH = 0
            + Float.BYTES // rating
            + Byte.BYTES // category
            + Short.BYTES // year
            + Byte.BYTES // month
            + Byte.BYTES // day
            + Short.BYTES // title length
            + Short.BYTES // href length
            + Short.BYTES; // description length
    
    private byte[] titleBytes;
    private byte[] descriptionBytes;
    
    public int serializedLength() {
        titleBytes = title.getBytes();
        descriptionBytes = description.getBytes();
        return BASE_SERIALIZED_LENGTH + titleBytes.length + url.length() + descriptionBytes.length;
    }
    
    public void serialize(final ByteBuffer out) {
        final int start = out.position();
        
        out.putFloat((float) rating);
        
        System.out.println(category);
        out.put((byte) category.getId());
        
        out.putShort((short) date.getYear());
        out.put((byte) date.getMonthValue());
        out.put((byte) date.getDayOfMonth());
        
        out.putShortBytes(titleBytes);
        out.putShortString(url);
        out.putShortBytes(descriptionBytes);
        
        final int titleLength = title.length();
        final int hrefLength = url.length();
        final int descriptionLength = description.length();
        final int end = out.position();
        final int length = serializedLength();
        if (end - start != length) {
            System.out.println(this);
        }
    }
    
    private static interface LitComparator<T> extends UsingLitProperty<T>, Comparator<LitStory> {}
    
    static class TitleComparator implements LitComparator<String> {
        
        @Override
        public int compare(final LitStory story1, final LitStory story2) {
            return story1.title.compareTo(story2.title);
        }
        
        @Override
        public LitProperty<String> getProperty() {
            return LitProperty.TITLE;
        }
        
    }
    
    static class AuthorComparator implements LitComparator<String> {
        
        @Override
        public int compare(final LitStory story1, final LitStory story2) {
            return story1.author.compareTo(story2.author);
        }
        
        @Override
        public LitProperty<String> getProperty() {
            return LitProperty.AUTHOR_NAME;
        }
        
    }
    
    static class RatingComparator implements LitComparator<Double> {
        
        @Override
        public int compare(final LitStory story1, final LitStory story2) {
            return Double.compare(story1.rating, story2.rating);
        }
        
        @Override
        public LitProperty<Double> getProperty() {
            return LitProperty.RATING;
        }
        
    }
    
    static class DateComparator implements LitComparator<LocalDate> {
        
        @Override
        public int compare(final LitStory story1, final LitStory story2) {
            return story1.date.compareTo(story2.date);
        }
        
        @Override
        public LitProperty<LocalDate> getProperty() {
            return LitProperty.DATE;
        }
        
    }
    
    static class LengthComparator implements LitComparator<Integer> {
        
        @Override
        public int compare(final LitStory story1, final LitStory story2) {
            try {
                return story1.numPages() - story2.numPages();
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        @Override
        public LitProperty<Integer> getProperty() {
            return LitProperty.LENGTH;
        }
        
    }
    
    static class CategoryComparator implements LitComparator<Category> {
        
        @Override
        public int compare(final LitStory story1, final LitStory story2) {
            return story1.category.compareTo(story2.category);
        }
        
        @Override
        public LitProperty<Category> getProperty() {
            return LitProperty.CATEGORY;
        }
        
    }
    
    private Document getFirstPage() throws IOException {
        return Internet.getDocument(url);
    }
    
    private int calcNumPages() throws IOException {
        if (pages.size() > 0) {
            return pages.size();
        }
        final Document storyDoc = getFirstPage();
        final Element numPagesElem = storyDoc.getElementsByClass("b-pager-pagesText").get(0);
        final String numPagesEtc = numPagesElem.text();
        return Integer.parseInt(numPagesEtc.substring(0, numPagesEtc.indexOf(' ')));
    }
    
    public int numPages() throws IOException {
        if (numPages == 0) {
            numPages = calcNumPages();
        }
        return numPages;
    }
    
    public int safeNumPages() {
        try {
            return numPages();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
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
        download(Lit.DOWNLOAD_DIR.resolve("stories"));
    }
    
    @Override
    public int hashCode() {
        return url.hashCode();
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
        return url.equals(other.url);
    }
    
    @Override
    public String toString() {
        String ret = "LitStory [title=" + title + ", href=" + url + ", authorName=" + authorName
                + ", description=" + description + ", category=" + category + ", date=" + date
                + ", rating=" + rating;
        if (numPages != 0) {
            ret += ", numPages=" + numPages;
        }
        return ret + "]";
    }
    
}
