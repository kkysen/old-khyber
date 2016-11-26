package sen.khyber.stats.counter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 
 * 
 * @author Khyber Sen
 * @param <T> frequency type
 */
public class FrequencyCounter<T> implements Cloneable, Iterable<T> {
    
    private final Map<T, Double> frequencies = new HashMap<>();
    private Class<T> type;
    
    public FrequencyCounter(final Map<T, Double> map) {
        putAll(map);
    }
    
    public FrequencyCounter(final Iterable<Map.Entry<T, Double>> iter) {
        putAll(iter);
    }
    
    public double put(final T t, final double frequency) {
        return frequencies.put(t, frequency);
    }
    
    public void putAll(final Map<T, Double> map) {
        frequencies.putAll(map);
    }
    
    public void putAll(final Iterable<Map.Entry<T, Double>> iter) {
        for (final Map.Entry<T, Double> frequency : iter) {
            frequencies.put(frequency.getKey(), frequency.getValue());
        }
    }
    
    public void putAll(final FrequencyCounter<T> freqCounter) {
        putAll(freqCounter.rawFrequencies());
    }
    
    public Map<T, Double> rawFrequencies() {
        return frequencies;
    }
    
    public Map<T, Integer> roundedFrequencies(final int sigFigs) {
        final Map<T, Integer> roundedFrequencies = new HashMap<>();
        for (final T t : frequencies.keySet()) {
            final int roundedFrequency = (int) (frequencies.get(t) * Math.pow(10, sigFigs));
            roundedFrequencies.put(t, roundedFrequency);
        }
        return roundedFrequencies;
    }
    
    @Override
    public Iterator<T> iterator() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
