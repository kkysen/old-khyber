package sen.khyber.web.scrape.lit;

import static sen.khyber.web.scrape.lit.LitStory.Property.AUTHOR;
import static sen.khyber.web.scrape.lit.LitStory.Property.AUTHOR_NAME;
import static sen.khyber.web.scrape.lit.LitStory.Property.CATEGORY;
import static sen.khyber.web.scrape.lit.LitStory.Property.DATE;
import static sen.khyber.web.scrape.lit.LitStory.Property.LENGTH;
import static sen.khyber.web.scrape.lit.LitStory.Property.RATING;

import sen.khyber.util.BaseQuery;
import sen.khyber.util.Query;
import sen.khyber.web.scrape.lit.LitStory.Property;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author Khyber Sen
 */
public interface LitStoriesQuery extends Query<LitStory>, BaseQuery<LitStory, LitStoriesQuery> {
    
    public <T> LitStoriesQuery withProperty(Property<T> property, T value);
    
    public <T> LitStoriesQuery withProperties(Property<T> property, Collection<T> values);
    
    public <T> LitStoriesQuery withPropertyGreaterThan(Property<T> property, T value);
    
    public <T> LitStoriesQuery withPropertyLessThan(Property<T> property, T value);
    
    public <T> LitStoriesQuery withPropertyBetween(Property<T> property, T lesser, T greater);
    
    public default LitStoriesQuery sortedBy(final Property<?> property) {
        return sorted(property.getOrder());
    }
    
    public default <T> Map<T, List<LitStory>> groupedBy(final Property<T> property) {
        return groupedBy(property.getGetter());
    }
    
    public default LitStoriesQuery byAuthor(final String author) {
        return withProperty(AUTHOR_NAME, author);
    }
    
    public default LitStoriesQuery byAuthors(final Collection<String> authors) {
        return withProperties(AUTHOR_NAME, authors);
    }
    
    public default LitStoriesQuery byAuthors(final String... authors) {
        return byAuthors(Arrays.asList(authors));
    }
    
    public default LitStoriesQuery byLitAuthor(final LitAuthor author) {
        return withProperty(AUTHOR, author);
    }
    
    public default LitStoriesQuery byLitAuthors(final Collection<LitAuthor> authors) {
        return withProperties(AUTHOR, authors);
    }
    
    public default LitStoriesQuery byLitAuthors(final LitAuthor... authors) {
        return byLitAuthors(Arrays.asList(authors));
    }
    
    public default LitStoriesQuery inCategory(final String category) {
        return withProperty(CATEGORY, category);
    }
    
    public default LitStoriesQuery inCategories(final Collection<String> categories) {
        return withProperties(CATEGORY, categories);
    }
    
    public default LitStoriesQuery inCategories(final String... categories) {
        return inCategories(Arrays.asList(categories));
    }
    
    public default LitStoriesQuery rated(final double rating) {
        return withProperty(RATING, rating);
    }
    
    public default LitStoriesQuery ratedAbove(final double rating) {
        return withPropertyGreaterThan(RATING, rating);
    }
    
    public default LitStoriesQuery ratedBelow(final double rating) {
        return withPropertyLessThan(RATING, rating);
    }
    
    public default LitStoriesQuery ratedBetween(final double lowerRating,
            final double higherRating) {
        return withPropertyBetween(RATING, lowerRating, higherRating);
    }
    
    public default LitStoriesQuery onDate(final LocalDate date) {
        return withProperty(DATE, date);
    }
    
    public default LitStoriesQuery afterDate(final LocalDate date) {
        return withPropertyGreaterThan(DATE, date);
    }
    
    public default LitStoriesQuery beforeDate(final LocalDate date) {
        return withPropertyLessThan(DATE, date);
    }
    
    public default LitStoriesQuery betweenDates(final LocalDate olderDate,
            final LocalDate newerDate) {
        return withPropertyBetween(DATE, olderDate, newerDate);
    }
    
    public default LitStoriesQuery ofLength(final int numPages) {
        return withProperty(LENGTH, numPages);
    }
    
    public default LitStoriesQuery longerThan(final int numPages) {
        return withPropertyGreaterThan(LENGTH, numPages);
    }
    
    public default LitStoriesQuery shorterThan(final int numPages) {
        return withPropertyLessThan(LENGTH, numPages);
    }
    
    public default LitStoriesQuery sortedByAuthor() {
        return sortedBy(Property.AUTHOR);
    }
    
    public default LitStoriesQuery sortedByCategory() {
        return sortedBy(Property.CATEGORY);
    }
    
    public default LitStoriesQuery sortedByRating() {
        return sortedBy(Property.RATING);
    }
    
    public default LitStoriesQuery sortedByDate() {
        return sortedBy(Property.DATE);
    }
    
    public default LitStoriesQuery sortedByLength() {
        return sortedBy(Property.LENGTH);
    }
    
    public default Map<LitAuthor, List<LitStory>> groupedByAuthor() {
        return groupedBy(Property.AUTHOR);
    }
    
    public default Map<String, List<LitStory>> groupedByAuthorName() {
        return groupedBy(Property.AUTHOR_NAME);
    }
    
    public default Map<String, List<LitStory>> groupedByCategory() {
        return groupedBy(Property.CATEGORY);
    }
    
    public default Map<LocalDate, List<LitStory>> groupedByDate() {
        return groupedBy(Property.DATE);
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
