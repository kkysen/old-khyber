package sen.khyber.web.scrape.lit;

import sen.khyber.util.CollectionUtils;
import sen.khyber.util.QueryImpl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

import lombok.Getter;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod(CollectionUtils.class)
public class LitStoriesQueryImpl extends QueryImpl<LitStory, LitStoriesQuery>
        implements LitStoriesQuery {
    
    private static interface LitFilter<T> extends UsingLitProperty<T> {
        
        public Predicate<? super LitStory> getPredicate();
        
    }
    
    private class GetterFilter<T> extends Filter implements LitFilter<T> {
        
        private final @Getter LitProperty<T> property;
        
        public GetterFilter(final LitProperty<T> property, final T value) {
            super(story -> value.equals(property.getGetter().apply(story)));
            this.property = property;
        }
        
    }
    
    private class CollectionFilter<T> extends Filter implements LitFilter<T> {
        
        private final @Getter LitProperty<T> property;
        private final @Getter int size;
        
        public CollectionFilter(final LitProperty<T> property,
                final Collection<? extends T> collection) {
            super(story -> collection.toHashSet().contains(property.getGetter().apply(story)));
            this.property = property;
            size = collection.size();
        }
        
    }
    
    private final Map<LitProperty<?>, Collection<?>> withProperties = new HashMap<>();
    
    //private final Map<LitProperty<?>,
    
    protected LitStoriesQueryImpl(final Stream<LitStory> stream) {
        super(stream);
        // TODO Auto-generated constructor stub
    }
    
    private <T> Predicate<LitStory> propertyFilter(final LitProperty<T> property) {
        
    }
    
    @Override
    protected void optimizeFilters(final List<Filter> filters) {
        super.optimizeFilters(filters);
        filters.sort(FILTER_ORDER_COMPARATOR);
        for (final LitProperty<?> property : LitProperty.values) {
            filters.add(new Filter(propertyFilter(property)));
        }
    }
    
    @Override
    protected void optimizeSorts(final List<Sort> sorts) {
        super.optimizeSorts(sorts);
        //sorts.sort(SORT_ORDER_COMPARATOR);
    }
    
    @Override
    public <T> LitStoriesQuery withProperty(final LitProperty<T> property, final T value) {
        
        return this;
    }
    
    @Override
    public <T> LitStoriesQuery withProperties(final LitProperty<T> property,
            final Collection<T> values) {
        // TODO Auto-generated method stub
        return this;
    }
    
    @Override
    public <T> LitStoriesQuery withPropertyGreaterThan(final LitProperty<T> property,
            final T value) {
        // TODO Auto-generated method stub
        return this;
    }
    
    @Override
    public <T> LitStoriesQuery withPropertyLessThan(final LitProperty<T> property, final T value) {
        // TODO Auto-generated method stub
        return this;
    }
    
}
