package sen.khyber.web.scrape.lit;

import sen.khyber.util.CollectionUtils;
import sen.khyber.util.QueryImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import lombok.Getter;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod(CollectionUtils.class)
public class LitStoriesQueryImpl extends QueryImpl<LitStory> implements LitStoriesQuery {
    
    private class GetterFilter<T> extends Filter {
        
        public GetterFilter(final T value, final Function<? super LitStory, ? extends T> getter) {
            super(story -> value.equals(getter.apply(story)));
        }
        
    }
    
    private class CollectionFilter<T> extends Filter {
        
        private final @Getter int size;
        
        public CollectionFilter(final Collection<? extends T> collection,
                final Function<? super LitStory, ? extends T> getter) {
            super(story -> collection.toHashSet().contains(getter.apply(story)));
            size = collection.size();
        }
        
    }
    
    private class AuthorNameFilter extends GetterFilter<String> {
        
        public AuthorNameFilter(final String author) {
            super(author, LitStory.Property.AUTHOR_NAME.getGetter());
        }
        
    }
    
    private class AuthorsFilter extends Filter {
        
        public AuthorsFilter(final Collection<String> authors) {
            super(story -> authors.toHashSet().contains(story.getAuthorName()));
        }
        
    }
    
    private static List<Class<? extends Filter>> FILTER_ORDER = new ArrayList<>();
    static {
        FILTER_ORDER.add(AuthorNameFilter.class);
    }
    
    public static <T> Comparator<T> classComparator(final List<Class<? extends T>> classOrder) {
        final Map<Class<? extends T>, Integer> order = new HashMap<>(classOrder.length);
        for (int i = 0; i < classOrder.size(); i++) {
            order.put(classOrder.get(i), i);
        }
        return (o1, o2) -> order.getOrDefault(o2, Integer.MAX_VALUE)
                - order.getOrDefault(o2, Integer.MAX_VALUE);
    }
    
    private static final Comparator<Filter> FILTER_ORDER_COMPARATOR = classComparator(FILTER_ORDER);
    
    protected LitStoriesQueryImpl(final Stream<LitStory> stream) {
        super(stream);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    protected void optimizeFilters(final List<Filter> filters) {
        super.optimizeFilters(filters);
        filters.sort(FILTER_ORDER_COMPARATOR);
    }
    
    @Override
    protected void optimizeSorts(final List<Sort> sorts) {
        super.optimizeSorts(sorts);
        //sorts.sort(SORT_ORDER_COMPARATOR);
    }
    
    @Override
    public LitStoriesQuery byAuthor(final String author) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public LitStoriesQuery byAuthors(final Collection<String> authors) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public LitStoriesQuery inCategory(final String category) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public LitStoriesQuery inCategories(final Collection<String> categories) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public LitStoriesQuery rated(final double rating) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public LitStoriesQuery ratedAbove(final double rating) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public LitStoriesQuery ratedBelow(final double rating) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public LitStoriesQuery ratedBetween(final double lowerRating, final double higherRating) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public LitStoriesQuery onDate(final LocalDate date) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public LitStoriesQuery afterDate(final LocalDate date) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public LitStoriesQuery beforeDate(final LocalDate date) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public LitStoriesQuery betweenDates(final LocalDate olderDate, final LocalDate newerDate) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
