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

public class LitAuthor implements Iterable<LitStory> {
    
    private final Document doc;
    
    private final @Getter String author;
    private final @Getter String href;
    private final @Getter int uid;
    
    private final @Getter List<LitStory> stories;
    
    public LitAuthor(final String url) throws IOException {
        this.href = url;
        
        final int uidStartIndex = href.indexOf('=') + 1;
        final int uidEndIndex = href.indexOf('&');
        uid = Integer.parseInt(href.substring(uidStartIndex, uidEndIndex));
        
        doc = Internet.getDocument(url);
        
        final Element authorLink = doc.getElementsByClass("contactheader").get(0);
        author = authorLink.ownText();
        
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
        for (final Element storyElem : storyElems) {
            stories.add(new LitStory(storyElem, author, href, uid));
        }
    }
    
    @Override
    public Iterator<LitStory> iterator() {
        return stories.iterator();
    }
    
    public void readStories() {
        stories.forEach(LitStory::getStory);
    }
    
}