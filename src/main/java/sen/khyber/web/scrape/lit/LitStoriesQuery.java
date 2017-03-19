package sen.khyber.web.scrape.lit;

import sen.khyber.util.Query;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 
 * 
 * @author Khyber Sen
 */
public interface LitStoriesQuery extends Query<LitStory> {
    
    @Override
    public LitStoriesQuery parallel();
    
    @Override
    public LitStoriesQuery sequential();
    
    @Override
    public LitStoriesQuery distinct();
    
    @Override
    public LitStoriesQuery sorted();
    
    @Override
    public LitStoriesQuery sorted(Comparator<? super LitStory> comparator);
    
    @Override
    public LitStoriesQuery filter(Predicate<? super LitStory> predicate);
    
    @Override
    public LitStoriesQuery filterOut(final Predicate<? super LitStory> predicate);
    
    @Override
    public LitStoriesQuery peek(final Consumer<? super LitStory> action);
    
    @Override
    public LitStoriesQuery limit(final long maxSize);
    
    public LitStoriesQuery byAuthor(final String author);
    
    public LitStoriesQuery byAuthors(final Collection<String> authors);
    
    public default LitStoriesQuery byAuthors(final String... authors) {
        return byAuthors(Arrays.asList(authors));
    }
    
    public LitStoriesQuery byLitAuthor(LitAuthor author);
    
    public LitStoriesQuery byLitAuthors(Collection<LitAuthor> authors);
    
    public default LitStoriesQuery byLitAuthors(final LitAuthor... authors) {
        return byLitAuthors(Arrays.asList(authors));
    }
    
    public LitStoriesQuery inCategory(String category);
    
    public LitStoriesQuery inCategories(Collection<String> categories);
    
    public default LitStoriesQuery inCategories(final String... categories) {
        return inCategories(Arrays.asList(categories));
    }
    
    public LitStoriesQuery rated(double rating);
    
    public LitStoriesQuery ratedAbove(double rating);
    
    public LitStoriesQuery ratedBelow(double rating);
    
    public LitStoriesQuery ratedBetween(double lowerRating, double higherRating);
    
    public LitStoriesQuery onDate(LocalDate date);
    
    public LitStoriesQuery afterDate(LocalDate date);
    
    public LitStoriesQuery beforeDate(LocalDate date);
    
    public LitStoriesQuery betweenDates(LocalDate olderDate, LocalDate newerDate);
    
    public LitStoriesQuery ofLength(int numPages);
    
    public LitStoriesQuery longerThan(int numPages);
    
    public LitStoriesQuery shorterThan(int numPages);
    
    public default LitStoriesQuery sortedByAuthor() {
        return sorted(LitStory.Property.AUTHOR.getOrder());
    }
    
    public default LitStoriesQuery sortedByCategory() {
        return sorted(LitStory.Property.CATEGORY.getOrder());
    }
    
    public default LitStoriesQuery sortedByRating() {
        return sorted(LitStory.Property.RATING.getOrder());
    }
    
    public default LitStoriesQuery sortedByDate() {
        return sorted(LitStory.Property.DATE.getOrder());
    }
    
    public default LitStoriesQuery sortedByLength() {
        return sorted(LitStory.Property.LENGTH.getOrder());
    }
    
    public default Map<LitAuthor, List<LitStory>> groupedByAuthor() {
        return groupedBy(LitStory.Property.AUTHOR.getGetter());
    }
    
    public default Map<String, List<LitStory>> groupedByAuthorName() {
        return groupedBy(LitStory.Property.AUTHOR_NAME.getGetter());
    }
    
    public default Map<String, List<LitStory>> groupedByCategory() {
        return groupedBy(LitStory.Property.CATEGORY.getGetter());
    }
    
    public default Map<LocalDate, List<LitStory>> groupedByDate() {
        return groupedBy(LitStory.Property.DATE.getGetter());
    }
    
    public static void main(final String[] args) {
        final LitStoriesQuery query = null;
        query.parallel()
                .byAuthor("author")
                .afterDate(LocalDate.of(2017, 3, 18))
                .inCategory("category")
                .ratedAbove(4)
                .limit(100)
                .longerThan(10)
                .sortedByRating()
                .stream()
                .map(LitStory::getHref)
                .forEachOrdered(System.out::println);
    }
    
}
