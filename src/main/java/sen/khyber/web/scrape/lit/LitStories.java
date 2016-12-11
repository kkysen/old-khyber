package sen.khyber.web.scrape.lit;

import sen.khyber.io.MyFiles;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class LitStories implements Iterable<LitStory> {
    
    private static final String OUT_DIR = "src/test/java/sen/khyber/lit/";
    private static final String AUTHOR_RESULTS_PATH = OUT_DIR + "LitAuthorSearchResults.txt";
    private static final String AUTHORS_PATH = OUT_DIR + "LitAuthors.txt";
    private static final String STORIES_PATH = OUT_DIR + "LitStories.txt";
    
    private final List<LitStory> stories;
    
    private final Map<String, Function<LitStory, ?>> classifiers = new HashMap<>(5);
    private final Map<String, Map<?, List<LitStory>>> groupings = new HashMap<>(5);
    
    private void addGrouping(final String name, final Function<LitStory, ?> classifier) {
        classifiers.put(name, classifier);
        groupings.put(name, null);
    }
    
    {
        addGrouping("titles", LitStory::getTitle);
        addGrouping("authors", LitStory::getAuthor);
        addGrouping("authorNames", LitStory::getAuthorName);
        addGrouping("categories", LitStory::getCategory);
        addGrouping("dates", LitStory::getDate);
        addGrouping("ratings", LitStory::getRating);
    }
    
    private LitStories(List<LitStory> stories, final boolean sorted) {
        if (stories == null) {
            stories = new ArrayList<>();
        }
        this.stories = stories;
        if (!sorted) {
            stories.sort(LitStory.TITLE_ORDER);
            stories.sort(LitStory.RATING_ORDER);
        }
    }
    
    public LitStories(final List<LitStory> stories) {
        this(stories, false);
    }
    
    private static List<LitStory> authorsToStories(final Set<LitAuthorSearchResult> authorResults) {
        final BiConsumer<List<LitStory>, LitAuthor> acc = //
                (list, author) -> list.addAll(author.getStories());
        final BiConsumer<List<LitStory>, List<LitStory>> combiner = //
                (list1, list2) -> list1.addAll(list2);
        return LitAuthorSearch.resultsToAuthors(authorResults)
                .collect(ArrayList<LitStory>::new, acc, combiner);
    }
    
    public LitStories(final Set<LitAuthorSearchResult> authorResults) {
        this(authorsToStories(authorResults));
    }
    
    private Map<?, List<LitStory>> groupBy(final String groupingName) {
        final Function<LitStory, ?> classifier = classifiers.get(groupingName);
        final Map<?, List<LitStory>> grouping = //
                stories.parallelStream().collect(Collectors.groupingByConcurrent(classifier));
        groupings.put(groupingName, grouping);
        return grouping;
    }
    
    private Map<?, List<LitStory>> getGroupingMap(final String groupingName) {
        return groupings.getOrDefault(groupingName, groupBy(groupingName));
    }
    
    private <K> Map<K, LitStories> getGrouping(final String groupingName) {
        @SuppressWarnings("unchecked")
        final Map<K, List<LitStory>> groupingMap = (Map<K, List<LitStory>>) getGroupingMap(groupingName);
        final Map<K, LitStories> grouping = new HashMap<>(groupingMap.size());
        for (final K k : groupingMap.keySet()) {
            grouping.put(k, new LitStories(groupingMap.get(k), true));
        }
        return grouping;
    }
    
    private Map<?, LitStories> getGroupingCopy(final String groupingName) {
        return new HashMap<>(getGrouping(groupingName));
    }
    
    @SuppressWarnings("unchecked")
    public Map<String, LitStories> getTitleMap() {
        return (Map<String, LitStories>) getGroupingCopy("titles");
    }
    
    @SuppressWarnings("unchecked")
    public Map<LitAuthor, LitStories> getAuthorMap() {
        return (Map<LitAuthor, LitStories>) getGroupingCopy("authors");
    }
    
    @SuppressWarnings("unchecked")
    public Map<String, LitStories> getAuthorNameMap() {
        return (Map<String, LitStories>) getGroupingCopy("authorNames");
    }
    
    @SuppressWarnings("unchecked")
    public Map<String, LitStories> getCategoryMap() {
        return (Map<String, LitStories>) getGroupingCopy("categories");
    }
    
    @SuppressWarnings("unchecked")
    public Map<String, LitStories> getDateMap() {
        return (Map<String, LitStories>) getGroupingCopy("dates");
    }
    
    @SuppressWarnings("unchecked")
    public Map<Double, LitStories> getRatingMap() {
        return (Map<Double, LitStories>) getGroupingCopy("ratings");
    }
    
    public Set<String> getTitles() {
        return getTitleMap().keySet();
    }
    
    public Set<LitAuthor> getAuthors() {
        return getAuthorMap().keySet();
    }
    
    public Set<String> getAuthorNames() {
        return getAuthorNameMap().keySet();
    }
    
    public Set<String> getCategories() {
        return getCategoryMap().keySet();
    }
    
    public Set<String> getDates() {
        return getDateMap().keySet();
    }
    
    public Set<Double> getRatings() {
        return getRatingMap().keySet();
    }
    
    private <K> LitStories get(final String groupingName, final K k) {
        return getGrouping(groupingName).get(k);
    }
    
    public LitStories titled(final String title) {
        return get("titles", title);
    }
    
    public LitStories byAuthor(final LitAuthor author) {
        return get("authors", author);
    }
    
    public LitStories byAuthorName(final String authorName) {
        return get("authorNames", authorName);
    }
    
    public LitStories inCategory(final String category) {
        return get("categories", category);
    }
    
    public LitStories fromDate(final String date) {
        return get("dates", date);
    }
    
    public LitStories rated(final double rating) {
        return get("ratings", rating);
    }
    
    public List<LitStory> getStories() {
        return new ArrayList<>(stories);
    }
    
    @Override
    public void forEach(final Consumer<? super LitStory> action) {
        stories.forEach(action);
    }
    
    public void printEach() {
        forEach(System.out::println);
    }
    
    @Override
    public Iterator<LitStory> iterator() {
        return new Iterator<LitStory>() {
            
            Iterator<LitStory> internal = stories.iterator();
            
            @Override
            public boolean hasNext() {
                return internal.hasNext();
            }
            
            @Override
            public LitStory next() {
                return internal.next();
            }
            
        };
    }
    
    public Stream<LitStory> parallelStream() {
        return stories.parallelStream();
    }
    
    private <K> void downloadBy(final String groupingName, final Path parentDir) throws IOException {
        final Path dir = Paths.get(parentDir.toString(), groupingName);
        MyFiles.createDirectoryOverwriting(dir);
        final Map<K, List<LitStory>> grouping = (Map<K, List<LitStory>>) groupBy(groupingName);
        for (final K key : grouping.keySet()) {
            final Path groupDir = Paths.get(dir.toString(), key.toString());
            MyFiles.createDirectoryOverwriting(groupDir);
            grouping.get(key).parallelStream().forEach(story -> {
                try {
                    story.download(groupDir);
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
    
    // FIXME 
    
    private <K> void downloadBy(final String groupingName) throws IOException {
        downloadBy(groupingName, Paths.get(Lit.DOWNLOAD_DIR));
    }
    
    public void downloadByAuthor() throws IOException {
        downloadBy("authors");
    }
    
    public void downloadByCategory() throws IOException {
        downloadBy("category");
    }
    
}
