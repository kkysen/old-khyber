package sen.khyber.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import lombok.Getter;

/**
 * 
 * 
 * @author Khyber Sen
 * @param <T> type returned by this streaming query
 * @param <Q> for extending
 */
public class QueryImpl<T, Q extends BaseQuery<T, Q>> implements Query<T>, BaseQuery<T, Q> {
    
    /*
     * distinct is kind of filter
     * apply all filters before distinct
     * 
     * cheap parallel + sequential calls made at same time
     * 
     * peek order unchanged
     * 
     * sorts all at the end
     * abstract method for arranging sorts
     * 
     * limits must be made with respect to filters DONE
     * 
     */
    
    private Stream<T> stream;
    private boolean closed = false;
    private boolean parallel;
    
    private int numOps;
    private long limit = Long.MAX_VALUE;
    private boolean limited;
    private long skip;
    private boolean distinct;
    private final List<Consumer<? super T>> peeks = new ArrayList<>();
    private final List<Filter> filters = new ArrayList<>();
    private final List<Sort> sorts = new ArrayList<>();
    
    protected QueryImpl(final Stream<T> stream) {
        this.stream = stream;
        stream.onClose(() -> {
            closed = true;
        });
        parallel = stream.isParallel();
    }
    
    private enum Type {
        DISTINCT,
        FILTER,
        PEEK,
        SORT,
        LIMIT
    }
    
    private class Operation {
        
        private final @Getter Function<Stream<T>, Stream<T>> operation;
        private final @Getter int opNum = numOps++;
        
        protected Operation(final Function<Stream<T>, Stream<T>> operation, final Type type) {
            this.operation = stream -> {
                if (parallel) {
                    stream.parallel();
                } else {
                    stream.sequential();
                }
                if (type == Type.FILTER) {
                    stream = applyLimitSkip(stream);
                }
                stream = applyPeeks(stream);
                return operation.apply(stream);
            };
        }
        
        public void apply() {
            System.out.println(opNum + " " + getClass().getSimpleName());
            stream = operation.apply(stream);
        }
        
    }
    
    protected class Filter extends Operation {
        
        private final @Getter Predicate<? super T> predicate;
        
        public Filter(final Predicate<? super T> predicate) {
            super(stream -> {
                return stream.filter(predicate);
            }, Type.FILTER);
            filters.add(this);
            this.predicate = predicate;
        }
        
    }
    
    protected class Sort extends Operation {
        
        private final @Getter Comparator<? super T> comparator;
        
        public Sort(final Comparator<? super T> comparator) {
            super(stream -> {
                if (comparator == null) {
                    return stream.sorted();
                } else {
                    return stream.sorted(comparator);
                }
            }, Type.SORT);
            sorts.add(this);
            this.comparator = comparator;
        }
        
    }
    
    @Override
    public Stream<T> stream() {
        applyAll();
        return stream;
    }
    
    @Override
    public Spliterator<T> spliterator() {
        return stream.spliterator();
    }
    
    @Override
    public final void close() {
        closed = true;
    }
    
    private void checkClosed() {
        if (closed) {
            throw new IllegalStateException("closed");
        }
    }
    
    @Override
    public final boolean isParallel() {
        return parallel;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public final Q parallel() {
        parallel = true;
        return (Q) this;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public final Q sequential() {
        parallel = false;
        return (Q) this;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public final Q distinct() {
        distinct = true;
        return (Q) this;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public final Q sorted(final Comparator<? super T> comparator) {
        new Sort(comparator);
        return (Q) this;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public final Q filter(final Predicate<? super T> predicate) {
        new Filter(predicate);
        return (Q) this;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public final Q peek(final Consumer<? super T> action) {
        peeks.add(action);
        return (Q) this;
    }
    
    private void checkNonNegative(final long n, final String what) {
        if (n < 0) {
            throw new IllegalArgumentException(what + " cannot be negative");
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public final Q limit(final long maxSize) {
        checkNonNegative(maxSize, "maxSize");
        if (maxSize < limit) {
            limit = maxSize;
        }
        return (Q) this;
    }
    
    @SuppressWarnings("unchecked")
    public final Q skip(final long n) {
        checkNonNegative(n, "skip");
        skip += n;
        return (Q) this;
    }
    
    private Stream<T> applyLimit(Stream<T> stream) {
        if (!limited) {
            stream = stream.limit(limit);
            limited = true;
        }
        return stream;
    }
    
    private Stream<T> applySkip(Stream<T> stream) {
        if (skip != 0) {
            stream = stream.skip(skip);
            skip = 0;
        }
        return stream;
    }
    
    private Stream<T> applyLimitSkip(final Stream<T> stream) {
        return applySkip(applyLimit(stream));
    }
    
    private Stream<T> applyPeeks(Stream<T> stream) {
        if (peeks.size() > 0) {
            for (final Consumer<? super T> peek : peeks) {
                stream = stream.peek(peek);
            }
            peeks.clear();
        }
        return stream;
    }
    
    private void applyPendingOperations() {
        stream = applyLimitSkip(stream);
        stream = applyPeeks(stream);
    }
    
    private void applyFilters() {
        checkClosed();
        optimizeFilters(filters);
        filters.forEach(Filter::apply);
        applyPendingOperations();
        if (distinct) {
            stream = stream.distinct();
        }
    }
    
    private void applySorts() {
        optimizeSorts(sorts);
        applyPendingOperations();
        sorts.forEach(Sort::apply);
    }
    
    private void applyAll() {
        applyFilters();
        applySorts();
    }
    
    protected void optimizeFilters(final List<Filter> filters) {}
    
    protected void optimizeSorts(final List<Sort> sorts) {}
    
    @Override
    public final void forEach(final Consumer<? super T> action) {
        applyAll();
        stream.forEach(action);
    }
    
    @Override
    public final void forEachOrdered(final Consumer<? super T> action) {
        applyAll();
        stream.forEachOrdered(action);
    }
    
    @Override
    public final Object[] toArray() {
        applyAll();
        return stream.toArray();
    }
    
    @Override
    public final <A> A[] toArray(final IntFunction<A[]> generator) {
        applyAll();
        return stream.toArray(generator);
    }
    
    @Override
    public final T reduce(final T identity, final BinaryOperator<T> accumulator) {
        applyAll();
        return stream.reduce(identity, accumulator);
    }
    
    @Override
    public final Optional<T> reduce(final BinaryOperator<T> accumulator) {
        applyAll();
        return stream.reduce(accumulator);
    }
    
    @Override
    public final <U> U reduce(final U identity, final BiFunction<U, ? super T, U> accumulator,
            final BinaryOperator<U> combiner) {
        applyAll();
        return stream.reduce(identity, accumulator, combiner);
    }
    
    @Override
    public final <R> R collect(final Supplier<R> supplier,
            final BiConsumer<R, ? super T> accumulator,
            final BiConsumer<R, R> combiner) {
        applyAll();
        return stream.collect(supplier, accumulator, combiner);
    }
    
    @Override
    public final <R, A> R collect(final Collector<? super T, A, R> collector) {
        applyAll();
        return stream.collect(collector);
    }
    
    @Override
    public final Optional<T> min(final Comparator<? super T> comparator) {
        applyFilters();
        return stream.min(comparator);
    }
    
    @Override
    public final Optional<T> max(final Comparator<? super T> comparator) {
        applyFilters();
        return stream.min(comparator);
    }
    
    @Override
    public final long count() {
        applyFilters();
        return stream.count();
    }
    
    @Override
    public final boolean anyMatch(final Predicate<? super T> predicate) {
        applyFilters();
        return stream.anyMatch(predicate);
    }
    
    @Override
    public final boolean allMatch(final Predicate<? super T> predicate) {
        applyFilters();
        return stream.allMatch(predicate);
    }
    
    @Override
    public final boolean noneMatch(final Predicate<? super T> predicate) {
        applyFilters();
        return stream.noneMatch(predicate);
    }
    
    @Override
    public final Optional<T> first() {
        applyAll();
        return stream.findFirst();
    }
    
    @Override
    public final Optional<T> any() {
        applyFilters();
        return stream.findAny();
    }
    
    public static void main(final String[] args) {
        final String[] strings = {"hello", "world", "khyber", "sen"};
        final Query<String> query = new QueryImpl<>(
                new ArrayList<>(Arrays.asList(strings)).stream());
        query.parallel().filter(s -> s.length() >= 5).filter(s -> s != "sen").limit(5).sorted()
                .filterOut(s -> s.charAt(0) == 'w').forEachOrdered(System.out::println);
    }
    
}
