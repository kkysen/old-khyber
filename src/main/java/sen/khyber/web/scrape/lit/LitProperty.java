package sen.khyber.web.scrape.lit;

import sen.khyber.util.Property;
import sen.khyber.web.scrape.lit.Lit.Category;
import sen.khyber.web.scrape.lit.LitStory.AuthorComparator;
import sen.khyber.web.scrape.lit.LitStory.CategoryComparator;
import sen.khyber.web.scrape.lit.LitStory.DateComparator;
import sen.khyber.web.scrape.lit.LitStory.LengthComparator;
import sen.khyber.web.scrape.lit.LitStory.RatingComparator;
import sen.khyber.web.scrape.lit.LitStory.TitleComparator;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.function.Function;

import lombok.Getter;

/**
 * no type parameters allowed for enums
 * 
 * @author Khyber Sen
 * @param <T> type of property
 */
public final class LitProperty<T> implements Property<LitStory, T> {
    
    private static final Comparator<LitStory> TITLE_ORDER = new TitleComparator();
    private static final Comparator<LitStory> AUTHOR_ORDER = new AuthorComparator();
    private static final Comparator<LitStory> RATING_ORDER = new RatingComparator().reversed();
    private static final Comparator<LitStory> DATE_ORDER = new DateComparator().reversed();
    private static final Comparator<LitStory> LENGTH_ORDER = new LengthComparator().reversed();
    private static final Comparator<LitStory> CATEGORY_ORDER = new CategoryComparator();
    
    public static final LitProperty<String> TITLE = //
            new LitProperty<>(LitStory::getTitle, TITLE_ORDER);
    
    public static final LitProperty<LitAuthor> AUTHOR = //
            new LitProperty<>(LitStory::getAuthor, AUTHOR_ORDER);
    
    public static final LitProperty<String> AUTHOR_NAME = //
            new LitProperty<>(LitStory::getAuthorName, AUTHOR_ORDER);
    
    public static final LitProperty<Category> CATEGORY = //
            new LitProperty<>(LitStory::getCategory, CATEGORY_ORDER);
    
    public static final LitProperty<Double> RATING = //
            new LitProperty<>(LitStory::getRating, RATING_ORDER);
    
    public static final LitProperty<LocalDate> DATE = //
            new LitProperty<>(LitStory::getDate, DATE_ORDER);
    
    public static final LitProperty<String> DATE_STRING = //
            new LitProperty<>(LitStory::getDateString, DATE_ORDER);
    
    public static final LitProperty<Integer> LENGTH = //
            new LitProperty<>(LitStory::safeNumPages, LENGTH_ORDER);
    
    private final @Getter Function<LitStory, T> getter;
    private final @Getter Comparator<LitStory> order;
    
    private LitProperty(final Function<LitStory, T> getter,
            final Comparator<LitStory> order) {
        this.getter = getter;
        this.order = order;
    }
    
    /**
     * ordered by decreasing rarity
     */
    public static final LitProperty<?>[] values = {
        TITLE, AUTHOR, AUTHOR_NAME, RATING, DATE, DATE_STRING, LENGTH, CATEGORY,
    };
    
    @SuppressWarnings("unchecked")
    public static <T> LitProperty<T> typeOf(final Function<LitStory, T> getter) {
        final T value = getter.apply(LitStory.DUMMY);
        if (value instanceof LitAuthor) {
            return (LitProperty<T>) AUTHOR;
        }
        if (value instanceof LocalDate) {
            return (LitProperty<T>) DATE;
        }
        if (value instanceof Double) {
            return (LitProperty<T>) RATING;
        }
        if (value instanceof Integer) {
            return (LitProperty<T>) LENGTH;
        }
        switch ((String) value) {
            case "authorName":
                return (LitProperty<T>) AUTHOR_NAME;
            case "title":
                return (LitProperty<T>) TITLE;
            case "category":
                return (LitProperty<T>) CATEGORY;
            default:
                return null;
        }
    }
    
    public static LitProperty<?> typeOf(final Comparator<LitStory> comparator) {
        if (!(comparator instanceof UsingLitProperty<?>)) {
            return null;
        }
        return ((UsingLitProperty<?>) comparator).getProperty();
    }
    
}