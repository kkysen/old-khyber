package sen.khyber.web.scrape.lit;

import sen.khyber.util.ByteBufferUtils;
import sen.khyber.web.Internet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lombok.Getter;
import lombok.experimental.ExtensionMethod;

/**
 * 
 * 
 * @author Khyber Sen
 */
@ExtensionMethod(ByteBufferUtils.class)
public class LitAuthor implements Iterable<LitStory>, Comparable<LitAuthor> {
    
    private static final String BASE_URL = Lit.Url.AUTHOR.getUrl();
    
    private final @Getter String name;
    private final @Getter String url;
    private final @Getter int uid;
    private final @Getter int numStories;
    private final @Getter double averageRating;
    
    private final @Getter List<LitStory> stories;
    
    public Stream<LitStory> storyStream() {
        return stories.stream();
    }
    
    private LitAuthor(final int uid, final String url) throws IOException {
        this.uid = uid;
        this.url = url;
        
        final Document doc = Internet.getDocument(url);
        
        final Element authorLink;
        try {
            authorLink = doc.getElementsByClass("contactheader").get(0);
        } catch (final IndexOutOfBoundsException e) {
            throw new RuntimeException(url, e);
        }
        name = authorLink.ownText();
        
        final Elements seriesStories = doc.getElementsByClass("sl");
        final Elements loneStories = //doc.getElementsByClass("root-story r-ott");
                // the above is not working for some reason, so I'm using this for now
                doc.getElementsByTag("tr")
                        .parallelStream()
                        .filter(e -> e.className().equals("root-story r-ott"))
                        .collect(Collectors.toCollection(Elements::new));
        final List<Element> storyElems = new ArrayList<>();
        storyElems.addAll(seriesStories);
        storyElems.addAll(loneStories);
        stories = new ArrayList<>(storyElems.size());
        int totalRating = 0;
        for (final Element storyElem : storyElems) {
            final LitStory story = new LitStory(storyElem, this);
            totalRating += story.getRating();
            stories.add(story);
        }
        numStories = stories.size();
        if (numStories == 0) { // div by 0
            averageRating = 0;
        } else {
            averageRating = totalRating / numStories;
        }
    }
    
    private static String uidToUrl(final int uid) {
        return BASE_URL + uid + "&page=submissions";
    }
    
    private static int urlToUid(final String url) {
        final int uidStartIndex = url.indexOf('=') + 1;
        final int uidEndIndex = url.indexOf('&');
        return Integer.parseInt(url.substring(uidStartIndex, uidEndIndex));
    }
    
    public LitAuthor(final int uid) throws IOException {
        this(uid, uidToUrl(uid));
    }
    
    public LitAuthor(final String url) throws IOException {
        this(urlToUid(url), url);
    }
    
    public LitAuthor(final ByteBuffer in) {
        uid = in.getInt();
        url = uidToUrl(uid);
        averageRating = in.getFloat();
        name = in.getShortString();
        numStories = in.getShort();
        stories = new ArrayList<>(numStories);
        for (int i = 0; i < numStories; i++) {
            stories.add(new LitStory(in, this));
        }
    }
    
    private static final int BASE_SERIALIZED_LENGTH = 0
            + Integer.BYTES // uid
            + Float.BYTES // averageRating
            + Short.BYTES // name length
            + Short.BYTES; // numStories
    
    private byte[] nameBytes;
    
    public int serializedLength() {
        nameBytes = name.getBytes();
        int length = BASE_SERIALIZED_LENGTH + nameBytes.length;
        for (final LitStory story : stories) {
            length += story.serializedLength();
        }
        return length;
    }
    
    public void serialize(final ByteBuffer out) {
        out.putInt(uid);
        out.putFloat((float) averageRating);
        out.putShortBytes(nameBytes);
        out.putShort((short) numStories); // or maybe stories.size()
        for (final LitStory story : stories) {
            story.serialize(out);
        }
    }
    
    @Override
    public Iterator<LitStory> iterator() {
        return stories.iterator();
    }
    
    public void readStories() {
        stories.forEach(LitStory::getStory);
    }
    
    @Override
    public String toString() {
        return "LitAuthor [name=" + name + ", href=" + url + "]";
    }
    
    @Override
    public int hashCode() {
        return uid;
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
        final LitAuthor other = (LitAuthor) obj;
        return uid == other.uid;
    }
    
    @Override
    public int compareTo(final LitAuthor other) {
        return uid - other.uid;
    }
    
    public static final Comparator<LitAuthor> NAME_ORDER = new NameComparator();
    
    private static class NameComparator implements Comparator<LitAuthor> {
        
        @Override
        public int compare(final LitAuthor author1, final LitAuthor author2) {
            return author1.name.compareTo(author2.name);
        }
        
    }
    
}