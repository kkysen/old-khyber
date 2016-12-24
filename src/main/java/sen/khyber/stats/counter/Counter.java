package sen.khyber.stats.counter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 
 * 
 * @author Khyber Sen
 * @param <T> type counted
 */
public class Counter<T> implements Cloneable, Iterable<T> {
    
    private final Map<T, AtomicLong> counter = new ConcurrentHashMap<>();
    private Class<T> type;
    
    public Counter() {}
    
    public void setType(final Class<T> type) {
        this.type = type;
    }
    
    public Class<T> getType() {
        return type;
    }
    
    @Override
    public Counter<T> clone() {
        final Counter<T> copy = new Counter<>();
        copy.addMap(new HashMap<T, AtomicLong>(counter));
        return copy;
    }
    
    public int size() {
        return counter.size();
    }
    
    public long length() {
        long length = 0;
        for (final AtomicLong i : counter.values()) {
            length += i.get();
        }
        return length;
    }
    
    public boolean contains(final T t) {
        return counter.containsKey(t);
    }
    
    @Override
    public String toString() {
        return counter.toString();
    }
    
    public long get(final T t) {
        return counter.get(t).get();
    }
    
    @Override
    public Iterator<T> iterator() {
        return counter.keySet().iterator();
    }
    
    public Set<Map.Entry<T, Long>> counts() {
        return unsortedCounts().entrySet();
    }
    
    private class ValuesIterator implements Iterator<T> {
        
        Iterator<Map.Entry<T, Long>> nonrepeatedValues = 
                unsortedCounts().entrySet().iterator();
        Map.Entry<T, Long> next;
        T t;
        long count = 0;
        
        @Override
        public boolean hasNext() {
            if (nonrepeatedValues.hasNext()) {
                if (count > 0) {
                    return true;
                } else {
                    next = nonrepeatedValues.next();
                    t = next.getKey();
                    count = next.getValue();
                    return nonrepeatedValues.hasNext();
                }
            } else {
                return false;
            }
        }
        
        @Override
        public T next() {
            count--;
            return t;
        }
        
        @Override
        public void remove() {
            
        }
        
    }
    
    private class ValuesIterable implements Iterable<T> {
        
        @Override
        public Iterator<T> iterator() {
            return new ValuesIterator();
        }
        
    }
    
    public Iterable<T> values() {
        return new ValuesIterable();
    }
    
    public String toSortedString() {
        final List<Map.Entry<T, Long>> sortedCounts = sortedCounts();
        final StringJoiner joiner = new StringJoiner(", ", "{", "}");
        for (final Map.Entry<T, Long> count : sortedCounts) {
            joiner.add(count.toString());
        }
        return joiner.toString();
    }
    
    private static String quote(final Object obj) {
        return "\"" + obj + "\"";
    }
    
    protected String toCSVLine(final T t, final AtomicLong count) {
        return quote(t) + "," + quote(count);
    }
    
    protected String toCSVLine(final Map.Entry<T, Long> count) {
        return quote(count.getKey()) + "," + quote(count.getValue());
    }
    
    // may not work if too long
    public String toCSV() {
        final StringJoiner joiner = new StringJoiner("\n");
        for (final T t : counter.keySet()) {
            joiner.add(toCSVLine(t, counter.get(t)));
        }
        return joiner.toString();
    }
    
    // may not work if too long
    public String toSortedCSV() {
        final List<Map.Entry<T, Long>> sortedCounts = sortedCounts();
        final StringJoiner joiner = new StringJoiner("\n");
        for (final Map.Entry<T, Long> count : sortedCounts) {
            joiner.add(toCSVLine(count));
        }
        return joiner.toString();
    }
    
    // may run out of memory (int overflow), so use BufferedWriter
    public void save(final Path path) throws IOException {
        final BufferedWriter writer = new BufferedWriter(new FileWriter(path.toString()));
        for (final T t : counter.keySet()) {
            writer.write(toCSVLine(t, counter.get(t)));
            writer.newLine();
            writer.flush();
        }
        writer.close();
    }
    
    // may run out of memory (int overflow), so use BufferedWriter
    public void saveSorted(final Path path) throws IOException {
        final BufferedWriter writer = new BufferedWriter(new FileWriter(path.toString()));
        for (final Map.Entry<T, Long> count : sortedCounts()) {
            writer.write(toCSVLine(count));
            writer.newLine();
            writer.flush();
        }
        writer.close();
    }
    
    public void add(final T t) {
        if (counter.containsKey(t)) {
            counter.get(t).getAndIncrement();
        } else {
            counter.put(t, new AtomicLong(1));
        }
    }
    
    private void add(final T t, final int count) {
        if (counter.containsKey(t)) {
            counter.get(t).getAndAdd(count);
        } else {
            counter.put(t, new AtomicLong(count));
        }
    }
    
    public void addAll(final Iterable<? extends T> iter) {
        for (final T t : iter) {
            add(t);
        }
    }
    
    public void addAll(final T[] arr) {
        for (final T t : arr) {
            add(t);
        }
    }
    
    public void addMap(final Map<? extends T, ? extends Number> map) {
        for (final T t : map.keySet()) {
            if (counter.containsKey(t)) {
                counter.get(t).getAndAdd(map.get(t).intValue());
            } else {
                final Number i = map.get(t);
                AtomicLong atom;
                if (i instanceof AtomicLong) {
                    atom = (AtomicLong) i;
                } else {
                    atom = new AtomicLong(i.intValue());
                }
                counter.put(t, atom);
            }
        }
    }
    
    protected String[] parseCSVLine(final String line) {
        final String[] splitLine = line.split("\",\"");
        splitLine[0] = splitLine[0].substring(1);
        splitLine[1] = splitLine[1].substring(0, splitLine[1].length() - 1);
        return splitLine;
    }
    
    private void readCSV(final Path path, final Function<String, T> parser, final boolean forAdd) 
            throws IOException {
        System.out.println("test");
        Files.lines(path)
        .forEach(line -> {
            final String[] splitLine = parseCSVLine(line);
            final T t = parser.apply(splitLine[0]);
            final int count = Integer.parseInt(splitLine[1]);
            if (forAdd) {
                add(t, count);
            } else {
                subtract(t, count);
            }
        });
    }
    
    public void addCSV(final Path path, final Function<String, T> parser) 
            throws IOException {
        readCSV(path, parser, true);
    }
    
    public void addCounter(final Counter<? extends T> otherCounter) {
        addMap(otherCounter.rawCount());
    }
    
    public boolean subtract(final T t) {
        if (counter.containsKey(t)) {
            counter.get(t).getAndDecrement();
            return true;
        }
        return false;
    }
    
    public boolean subtract(final T t, final int count) {
        if (counter.containsKey(t)) {
            final AtomicLong i = counter.get(t);
            i.getAndAdd(- count);
            if (i.get() < 0) {
                i.set(0);
            }
            return true;
        }
        return false;
    }
    
    public void subtractAll(final Iterable<? extends T> iter) {
        for (final T t : iter) {
            subtract(t);
        }
    }
    
    public void subtractAll(final T[] arr) {
        for (final T t : arr) {
            subtract(t);
        }
    }
    
    public void subtractMap(final Map<? extends T, ? extends Number> map) {
        for (final T t : map.keySet()) {
            if (counter.containsKey(t)) {
                counter.get(t).getAndAdd(- map.get(t).intValue());
            }
        }
    }
    
    public void subtractCSV(final Path path, final Function<String, T> parser) 
            throws IOException {
        readCSV(path, parser, false);
    }
    
    public void subtractCounter(final Counter<? extends T> otherCounter) {
        subtractMap(otherCounter.rawCount());
    }
    
    public long remove(final T t) {
        return counter.remove(t).get();
    }
    
    public List<Long> removeAll(final Iterable<? extends T> iter) {
        final List<Long> removedCounts = new ArrayList<>();
        for (final T t : iter) {
            removedCounts.add(remove(t));
        }
        return removedCounts;
    }
    
    public void removeIf(final Predicate<? super T> filter) {
        final Iterator<T> iter = counter.keySet().iterator();
        while (iter.hasNext()) {
            if (filter.test(iter.next())) {
                iter.remove();
            }
        }
        /*for (T t : counter.keySet()) {
            if (filter.test(t)) {
                remove(t);
            }
        }*/
    }
    
    public void retainIf(final Predicate<? super T> filter) {
        removeIf(filter.negate());
    }
    
    public Map<T, AtomicLong> rawCount() {
        return counter;
    }
    
    public Set<T> unsortedElements() {
        final Set<T> elements = new HashSet<>();
        for (final T t : counter.keySet()) {
            final long count = counter.get(t).get();
            for (int i = 0; i < count; i++) {
                elements.add(t);
            }
        }
        return elements;
    }
    
    public Map<T, Long> unsortedCounts() {
        final Map<T, Long> unsortedCounts = new HashMap<>();
        for (final T t : counter.keySet()) {
            unsortedCounts.put(t, counter.get(t).get());
        }
        return unsortedCounts;
    }
    
    /*public static <K, V> List<Map.Entry<K, V>> 
        sortMapByValue(Map<K, V> map, Comparator<Map.Entry<? extends K, ? extends V>> comparator) {
        List<Map.Entry<K, V>> sortedMap = new ArrayList<>(map.entrySet());
        Collections.sort(sortedMap, comparator);
        return sortedMap;
    }
    
    public static <K, V> Map<K, V> listMappingtoMap(List<Map.Entry<K, V>> listMap) {
        Map<K, V> map = new HashMap<>();
        for (Map.Entry<K, V> entry : listMap) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }*/
    
    private static <T, U> List<T> getElements(final List<Map.Entry<T, U>> mapAsList) {
        final List<T> elements = new ArrayList<>(mapAsList.size());
        for (final Map.Entry<T, U> entry : mapAsList) {
            elements.add(entry.getKey());
        }
        return elements;
    }
    
    public List<Map.Entry<T, Long>> sortedCounts() {
        final List<Map.Entry<T, Long>> sortedCounts = 
                new ArrayList<>(unsortedCounts().entrySet());
        Collections.sort(sortedCounts, new MapEntryComparator<T, Long>());
        return sortedCounts;
        //return sortMapByValue(unsortedCounts(), counterComparator);
    }
    
    public List<T> sortedElements() {
        return getElements(sortedCounts());
    }
    
    public List<Map.Entry<T, Long>> mostCommonCounts(int numMostCommon) {
        if (numMostCommon > size()) {
            numMostCommon = size();
        }
        return sortedCounts().subList(0, numMostCommon);
    }
    
    public List<Map.Entry<T, Long>> mostCommonCounts() {
        return mostCommonCounts(10);
    }
    
    public List<T> mostCommonElements(final int numMostCommon) {
        return getElements(mostCommonCounts(numMostCommon));
    }
    
    public List<T> mostCommonElements() {
        return mostCommonElements(10);
    }
    
    public List<Map.Entry<T, Long>> leastCommonCounts(int numLeastCommon) {
        if (numLeastCommon > size()) {
            numLeastCommon = size();
        }
        final List<Map.Entry<T, Long>> leastCommonCounts = 
                sortedCounts().subList(size() - numLeastCommon, size());
        Collections.reverse(leastCommonCounts);
        return leastCommonCounts;
    }
    
    public List<Map.Entry<T, Long>> leastCommonCounts() {
        return leastCommonCounts(10);
    }
    
    public List<T> leastCommonElements(final int numLeastCommon) {
        return getElements(leastCommonCounts(numLeastCommon));
    }
    
    public List<T> leastCommonElements() {
        return leastCommonElements(10);
    }
    
    private double percentage(final T t, final long length) {
        return counter.get(t).doubleValue() / length;
    }
    
    public double percentage(final T t) {
        return percentage(t, length());
    }
    
    public Map<T, Double> unsortedPercentages() {
        final long length = length();
        final Map<T, Double> percentages = new HashMap<>();
        for (final T t : counter.keySet()) {
            percentages.put(t, percentage(t, length));
        }
        return percentages;
    }
    
    public List<Map.Entry<T, Double>> sortedPercentages() {
        final List<Map.Entry<T, Double>> percentages = 
                new ArrayList<>(unsortedPercentages().entrySet());
        Collections.sort(percentages, new MapEntryComparator<T, Double>());
        return percentages;
    }
    
    public FrequencyCounter<T> frequencyCounter() {
        return new FrequencyCounter<>(unsortedPercentages());
    }
    
    public List<Map.Entry<T, Double>> mostCommonPercentages(int numMostCommon) {
        if (numMostCommon > size()) {
            numMostCommon = size();
        }
        return sortedPercentages().subList(0, numMostCommon);
    }
    
    public List<Map.Entry<T, Double>> mostCommonPercentages() {
        return mostCommonPercentages(10);
    }
    
    public List<Map.Entry<T, Double>> leastCommonPercentages(int numLeastCommon) {
        if (numLeastCommon > size()) {
            numLeastCommon = size();
        }
        final List<Map.Entry<T, Double>> leastCommonPercentages = 
                sortedPercentages().subList(size() - numLeastCommon, size());
        Collections.reverse(leastCommonPercentages);
        return leastCommonPercentages;
    }
    
    public List<Map.Entry<T, Double>> leastCommonPercentages() {
        return leastCommonPercentages(10);
    }
    
    public BarGraph<T> toBarGraph(final Class<T> type) {
        setType(type);
        return new BarGraph<T>(this);
    }
    
    public BarGraph<T> toBarGraph(final Class<T> type, final int numMostCommon) {
        return new BarGraph<T>(mostCommonCounts(numMostCommon), type);
    }
    
    public static void main(final String[] args) throws Exception {
        
        final Path test = Paths.get("testFile.txt");
        
        final WordCounter wordCounter = WordCounter.fromFile(test);
        //System.out.println(wordCounter.mostCommonCounts(100));
        //System.out.println(wordCounter.mostCommonPercentages());
        //System.out.println(wordCounter.toBarGraph(100));
        
        //System.out.println();
        
        //CharCounter charCounter = CharCounter.fromFile(test);
        //System.out.println(charCounter.mostCommonCounts(100));
        //System.out.println(charCounter.mostCommonPercentages());
        //System.out.println(charCounter.toBarGraph(100));
        
        //MyFiles.write(Paths.get("wordCountGraph.txt"), wordCounter.toBarGraph());
        //MyFiles.write(Paths.get("charCountGraph.txt"), charCounter.toBarGraph());
        
        wordCounter.sortedCounts().forEach(System.out::println);
        System.out.println();
        final List<String> values = new ArrayList<>();
        wordCounter.values().iterator().forEachRemaining(values::add);
        System.out.println(values.subList(0, 100));
    }
    
}
