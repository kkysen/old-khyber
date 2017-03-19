package sen.khyber.util;

import java.util.Comparator;
import java.util.Iterator;
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
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class StreamWrapper<T> implements Stream<T> {
    
    private final Stream<T> stream;
    
    public StreamWrapper(final Stream<T> stream) {
        this.stream = stream;
    }
    
    @Override
    public Iterator<T> iterator() {
        return stream.iterator();
    }
    
    @Override
    public Spliterator<T> spliterator() {
        return stream.spliterator();
    }
    
    @Override
    public boolean isParallel() {
        return stream.isParallel();
    }
    
    @Override
    public StreamWrapper<T> sequential() {
        stream.sequential();
        return this;
    }
    
    @Override
    public StreamWrapper<T> parallel() {
        stream.parallel();
        return this;
    }
    
    @Override
    public StreamWrapper<T> unordered() {
        stream.unordered();
        return this;
    }
    
    @Override
    public StreamWrapper<T> onClose(final Runnable closeHandler) {
        stream.onClose(closeHandler);
        return this;
    }
    
    @Override
    public void close() {
        stream.close();
    }
    
    @Override
    public StreamWrapper<T> filter(final Predicate<? super T> predicate) {
        return new StreamWrapper<>(stream.filter(predicate));
    }
    
    @Override
    public <R> StreamWrapper<R> map(final Function<? super T, ? extends R> mapper) {
        return new StreamWrapper<>(stream.map(mapper));
    }
    
    @Override
    public IntStream mapToInt(final ToIntFunction<? super T> mapper) {
        return stream.mapToInt(mapper);
    }
    
    @Override
    public LongStream mapToLong(final ToLongFunction<? super T> mapper) {
        return stream.mapToLong(mapper);
    }
    
    @Override
    public DoubleStream mapToDouble(final ToDoubleFunction<? super T> mapper) {
        return stream.mapToDouble(mapper);
    }
    
    @Override
    public <R> StreamWrapper<R> flatMap(
            final Function<? super T, ? extends Stream<? extends R>> mapper) {
        return new StreamWrapper<>(stream.flatMap(mapper));
    }
    
    @Override
    public IntStream flatMapToInt(final Function<? super T, ? extends IntStream> mapper) {
        return stream.flatMapToInt(mapper);
    }
    
    @Override
    public LongStream flatMapToLong(final Function<? super T, ? extends LongStream> mapper) {
        return stream.flatMapToLong(mapper);
    }
    
    @Override
    public DoubleStream flatMapToDouble(final Function<? super T, ? extends DoubleStream> mapper) {
        return stream.flatMapToDouble(mapper);
    }
    
    @Override
    public StreamWrapper<T> distinct() {
        return new StreamWrapper<>(stream.distinct());
    }
    
    @Override
    public StreamWrapper<T> sorted() {
        return new StreamWrapper<>(stream.sorted());
    }
    
    @Override
    public StreamWrapper<T> sorted(final Comparator<? super T> comparator) {
        return new StreamWrapper<>(stream.sorted(comparator));
    }
    
    @Override
    public StreamWrapper<T> peek(final Consumer<? super T> action) {
        return new StreamWrapper<>(stream.peek(action));
    }
    
    @Override
    public StreamWrapper<T> limit(final long maxSize) {
        return new StreamWrapper<>(stream.limit(maxSize));
    }
    
    @Override
    public StreamWrapper<T> skip(final long n) {
        return new StreamWrapper<>(stream.skip(n));
    }
    
    @Override
    public void forEach(final Consumer<? super T> action) {
        stream.forEach(action);
    }
    
    @Override
    public void forEachOrdered(final Consumer<? super T> action) {
        stream.forEachOrdered(action);
    }
    
    @Override
    public Object[] toArray() {
        return stream.toArray();
    }
    
    @Override
    public <A> A[] toArray(final IntFunction<A[]> generator) {
        return stream.toArray(generator);
    }
    
    @Override
    public T reduce(final T identity, final BinaryOperator<T> accumulator) {
        return stream.reduce(identity, accumulator);
    }
    
    @Override
    public Optional<T> reduce(final BinaryOperator<T> accumulator) {
        return stream.reduce(accumulator);
    }
    
    @Override
    public <U> U reduce(final U identity, final BiFunction<U, ? super T, U> accumulator,
            final BinaryOperator<U> combiner) {
        return stream.reduce(identity, accumulator, combiner);
    }
    
    @Override
    public <R> R collect(final Supplier<R> supplier, final BiConsumer<R, ? super T> accumulator,
            final BiConsumer<R, R> combiner) {
        return stream.collect(supplier, accumulator, combiner);
    }
    
    @Override
    public <R, A> R collect(final Collector<? super T, A, R> collector) {
        return stream.collect(collector);
    }
    
    @Override
    public Optional<T> min(final Comparator<? super T> comparator) {
        return stream.min(comparator);
    }
    
    @Override
    public Optional<T> max(final Comparator<? super T> comparator) {
        return stream.max(comparator);
    }
    
    @Override
    public long count() {
        return stream.count();
    }
    
    @Override
    public boolean anyMatch(final Predicate<? super T> predicate) {
        return stream.anyMatch(predicate);
    }
    
    @Override
    public boolean allMatch(final Predicate<? super T> predicate) {
        return stream.allMatch(predicate);
    }
    
    @Override
    public boolean noneMatch(final Predicate<? super T> predicate) {
        return stream.noneMatch(predicate);
    }
    
    @Override
    public Optional<T> findFirst() {
        return stream.findFirst();
    }
    
    @Override
    public Optional<T> findAny() {
        return stream.findAny();
    }
    
}
