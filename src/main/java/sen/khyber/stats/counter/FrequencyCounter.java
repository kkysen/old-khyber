package sen.khyber.stats.counter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FrequencyCounter<T> implements Cloneable, Iterable<T> {
    
    private Map<T, Double> frequencies = new HashMap<>();
    private Class<T> type;
    
    public FrequencyCounter(Map<T, Double> map) {
        putAll(map);
    }
    
    public FrequencyCounter(Iterable<Map.Entry<T, Double>> iter) {
        putAll(iter);
    }
    
    public double put(T t, double frequency) {
        return frequencies.put(t, frequency);
    }
    
    public void putAll(Map<T, Double> map) {
        frequencies.putAll(map);
    }
    
    public void putAll(Iterable<Map.Entry<T, Double>> iter) {
        for (Map.Entry<T, Double> frequency : iter) {
            frequencies.put(frequency.getKey(), frequency.getValue());
        }
    }
    
    public void putAll(FrequencyCounter<T> freqCounter) {
        putAll(freqCounter.rawFrequencies());
    }
    
    public Map<T, Double> rawFrequencies() {
        return frequencies;
    }
    
    public Map<T, Integer> roundedFrequencies(int sigFigs) {
        Map<T, Integer> roundedFrequencies = new HashMap<>();
        for (T t : frequencies.keySet()) {
            int roundedFrequency = (int) (frequencies.get(t) * Math.pow(10, sigFigs));
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
