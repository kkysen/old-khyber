package sen.khyber.web.scrape.lit;

import static sen.khyber.web.scrape.lit.LitProperty.AUTHOR;
import static sen.khyber.web.scrape.lit.LitProperty.AUTHOR_NAME;
import static sen.khyber.web.scrape.lit.LitProperty.CATEGORY;
import static sen.khyber.web.scrape.lit.LitProperty.DATE;
import static sen.khyber.web.scrape.lit.LitProperty.LENGTH;
import static sen.khyber.web.scrape.lit.LitProperty.RATING;

import sen.khyber.util.BaseQuery;
import sen.khyber.util.Query;
import sen.khyber.web.scrape.lit.Lit.Category;

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
    
    public <T> LitStoriesQuery withProperty(LitProperty<T> property, T value);
    
    public <T> LitStoriesQuery withProperties(LitProperty<T> property, Collection<T> values);
    
    public <T> LitStoriesQuery withPropertyGreaterThan(LitProperty<T> property, T value);
    
    public <T> LitStoriesQuery withPropertyLessThan(LitProperty<T> property, T value);
    
    public default <T> LitStoriesQuery withPropertyBetween(final LitProperty<T> property,
            final T lesser, final T greater) {
        return withPropertyGreaterThan(property, lesser)
                .withPropertyLessThan(property, greater);
    }
    
    public default LitStoriesQuery sortedBy(final LitProperty<?> property) {
        return sorted(property.getOrder());
    }
    
    public default <T> Map<T, List<LitStory>> groupedBy(final LitProperty<T> property) {
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
    
    public default LitStoriesQuery inCategory(final Category category) {
        return withProperty(CATEGORY, category);
    }
    
    public default LitStoriesQuery inCategories(final Collection<Category> categories) {
        return withProperties(CATEGORY, categories);
    }
    
    public default LitStoriesQuery inCategories(final Category... categories) {
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
        return sortedBy(LitProperty.AUTHOR);
    }
    
    public default LitStoriesQuery sortedByCategory() {
        return sortedBy(LitProperty.CATEGORY);
    }
    
    public default LitStoriesQuery sortedByRating() {
        return sortedBy(LitProperty.RATING);
    }
    
    public default LitStoriesQuery sortedByDate() {
        return sortedBy(LitProperty.DATE);
    }
    
    public default LitStoriesQuery sortedByLength() {
        return sortedBy(LitProperty.LENGTH);
    }
    
    public default Map<LitAuthor, List<LitStory>> groupedByAuthor() {
        return groupedBy(LitProperty.AUTHOR);
    }
    
    public default Map<String, List<LitStory>> groupedByAuthorName() {
        return groupedBy(LitProperty.AUTHOR_NAME);
    }
    
    public default Map<Category, List<LitStory>> groupedByCategory() {
        return groupedBy(LitProperty.CATEGORY);
    }
    
    public default Map<LocalDate, List<LitStory>> groupedByDate() {
        return groupedBy(LitProperty.DATE);
    }
    
    public static void main(final String[] args) {
        final LitStoriesQuery query = null;
        query.parallel()
                .byAuthor("author")
                .afterDate(LocalDate.of(2017, 3, 18))
                .inCategory(Category.valueOf(0))
                .ratedAbove(4)
                .limit(100)
                .longerThan(10)
                .sortedByRating()
                .stream()
                .map(LitStory::getHref)
                .forEachOrdered(System.out::println);
    }
    
}
