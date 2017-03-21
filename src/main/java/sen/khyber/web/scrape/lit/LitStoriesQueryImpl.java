package sen.khyber.web.scrape.lit;

import sen.khyber.util.CollectionUtils;
import sen.khyber.util.QueryImpl;
import sen.khyber.web.scrape.lit.LitStory.Property;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import lombok.Getter;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod(CollectionUtils.class)
public class LitStoriesQueryImpl extends QueryImpl<LitStory, LitStoriesQuery>
        implements LitStoriesQuery {
    
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
        final Map<Class<? extends T>, Integer> order = new HashMap<>(classOrder.size());
        for (int i = 0; i < classOrder.size(); i++) {
            order.put(classOrder.get(i), i);
        }
        return (o1, o2) -> order.getOrDefault(o2, Integer.MAX_VALUE)
                - order.getOrDefault(o2, Integer.MAX_VALUE);
    }
    
    private static final Comparator<Filter> FILTER_ORDER_COMPARATOR = classComparator(FILTER_ORDER);
    
    private final Map<Property<?>, Collection<?>> withProperties;
    
    private final Map<Property<?>,
    
    protected LitStoriesQueryImpl(final Stream<LitStory> stream) {
        super(stream);
        // TODO Auto-generated constructor stub
    }
    
    private <T> Predicate<LitStory> propertyFilter(final Property<T> property) {
        
    }
    
    @Override
    protected void optimizeFilters(final List<Filter> filters) {
        super.optimizeFilters(filters);
        filters.sort(FILTER_ORDER_COMPARATOR);
        for (final Property<?> property : Property.values) {
            filters.add(new Filter(propertyFilter(property)));
        }
    }
    
    @Override
    protected void optimizeSorts(final List<Sort> sorts) {
        super.optimizeSorts(sorts);
        //sorts.sort(SORT_ORDER_COMPARATOR);
    }
    
    @Override
    public <T> LitStoriesQuery withProperty(final Property<T> property, final T value) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public <T> LitStoriesQuery withProperties(final Property<T> property,
            final Collection<T> values) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public <T> LitStoriesQuery withPropertyGreaterThan(final Property<T> property, final T value) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public <T> LitStoriesQuery withPropertyLessThan(final Property<T> property, final T value) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public <T> LitStoriesQuery withPropertyBetween(final Property<T> property, final T lesser,
            final T greater) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
