package sen.khyber.web.scrape.lit;

import sen.khyber.web.Internet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lombok.Getter;

public class LitAuthor implements Iterable<LitStory>, Comparable<LitAuthor> {
    
    private final Document doc;
    
    private final @Getter String name;
    private final @Getter String href;
    private final @Getter int uid;
    private final @Getter int numStories;
    private final @Getter double averageRating;
    
    private final @Getter List<LitStory> stories;
    
    public LitAuthor(final String url) throws IOException {
        this.href = url;
        
        final int uidStartIndex = href.indexOf('=') + 1;
        final int uidEndIndex = href.indexOf('&');
        uid = Integer.parseInt(href.substring(uidStartIndex, uidEndIndex));
        
        doc = Internet.getDocument(url);
        
        final Element authorLink = doc.getElementsByClass("contactheader").get(0);
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
        int numSkipped = 0; // skipped if some error with the story
        int totalRating = 0;
        for (final Element storyElem : storyElems) {
            try {
                final LitStory story = new LitStory(storyElem, this);
                totalRating += story.getRating();
                stories.add(story);
            } catch (final IndexOutOfBoundsException e) {
                numSkipped++;
            }
        }
        numStories = stories.size() + numSkipped;
        if (numStories == 0) { // div by 0
            averageRating = 0;
        } else {
            averageRating = totalRating / numStories;
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
        return "LitAuthor [name=" + name + ", href=" + href + "]";
    }
    
    @Override
    public int hashCode() {
        return 31 + uid;
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
        return name.compareTo(other.name);
    }
    
}