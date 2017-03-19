package sen.khyber.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 
 * 
 * @author Khyber Sen
 */
public interface Query<T> {
    
    public Stream<T> stream();
    
    public Spliterator<T> spliterator();
    
    public default Iterator<T> iterator() {
        return Spliterators.iterator(spliterator());
    }
    
    public default Iterable<T> iterable() {
        return () -> Query.this.iterator();
    }
    
    public void close();
    
    public boolean isParallel();
    
    public Query<T> parallel();
    
    public Query<T> sequential();
    
    public Query<T> distinct();
    
    public default Query<T> sorted() {
        return sorted(null);
    }
    
    public Query<T> sorted(Comparator<? super T> comparator);
    
    public Query<T> filter(Predicate<? super T> predicate);
    
    public default Query<T> filterOut(final Predicate<? super T> predicate) {
        return filter(predicate.negate());
    }
    
    public Query<T> peek(final Consumer<? super T> action);
    
    public Query<T> limit(final long maxSize);
    
    public void forEach(final Consumer<? super T> action);
    
    public void forEachOrdered(Consumer<? super T> action);
    
    public Object[] toArray();
    
    public <A> A[] toArray(final IntFunction<A[]> generator);
    
    public T reduce(final T identity, final BinaryOperator<T> accumulator);
    
    public Optional<T> reduce(final BinaryOperator<T> accumulator);
    
    public <U> U reduce(final U identity, final BiFunction<U, ? super T, U> accumulator,
            final BinaryOperator<U> combiner);
    
    public <R> R collect(final Supplier<R> supplier, final BiConsumer<R, ? super T> accumulator,
            final BiConsumer<R, R> combiner);
    
    public <R, A> R collect(final Collector<? super T, A, R> collector);
    
    public Optional<T> min(final Comparator<? super T> comparator);
    
    public Optional<T> max(final Comparator<? super T> comparator);
    
    public long count();
    
    public boolean anyMatch(final Predicate<? super T> predicate);
    
    public boolean allMatch(final Predicate<? super T> predicate);
    
    public boolean noneMatch(final Predicate<? super T> predicate);
    
    public Optional<T> first();
    
    public Optional<T> any();
    
    public default List<T> toList() {
        return collect(Collectors.toList());
    }
    
    public default Set<T> toSet() {
        return collect(Collectors.toSet());
    }
    
    public default Collection<T> toCollection(
            final Supplier<? extends Collection<T>> collectionSupplier) {
        return collect(Collectors.toCollection(collectionSupplier));
    }
    
    public default <K> Map<K, List<T>> groupedBy(
            final Function<? super T, ? extends K> classifier) {
        return collect(Collectors.groupingBy(classifier));
    }
    
    public default void printEach() {
        forEach(System.out::println);
    }
    
    public default void printEachOrdered() {
        forEachOrdered(System.out::println);
    }
    
}