package sen.khyber.stats.counter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * 
 * 
 * @author Khyber Sen
 * @param <T> frequency type
 */
public class BarGraph<T> {
    
    private Map<T, Long> counter;
    private Set<T> keys;
    private int maxKeyLength;
    private long maxCount;
    private String title;
    private final int scaleMarkerStep = 5;
    private final List<String> scale = new ArrayList<>(4);
    private Set<String> bars;
    private Class<T> type;
    
    public BarGraph(final Map<T, Long> counter, final Class<T> type) {
        this.counter = counter;
        this.type = type;
    }
    
    public BarGraph(final Counter<T> counter) {
        this(counter.unsortedCounts(), counter.getType());
    }
    
    public BarGraph(final List<Map.Entry<T, Long>> listCounter, final Class<T> type) {
        final Map<T, Long> mapCounter = new HashMap<>(listCounter.size());
        for (final Map.Entry<T, Long> count : listCounter) {
            mapCounter.put(count.getKey(), count.getValue());
        }
        counter = mapCounter;
        this.type = type;
    }
    
    private static StringBuilder multiply(final CharSequence s, int times, final CharSequence start, final CharSequence end) {
        final StringBuilder sb = new StringBuilder(start);
        while (times-- > 0) {
            sb.append(s);
        }
        sb.append(end);
        return sb;
    }
    
    private static StringBuilder multiply(final CharSequence s, final int times, final CharSequence start) {
        return multiply(s, times, start, "");
    }
    
    private static StringBuilder multiply(final CharSequence s, final int times) {
        return multiply(s, times, "");
    }
    
    private static <U> int maxLength(final Collection<U> objects) {
        int max = Integer.MIN_VALUE;
        for (final U u : objects) {
            final int length = u.toString().length();
            if (length > max) {
                max = length;
            }
        }
        return max;
    }
    
    private static StringBuilder padLeft(final String s, int length) {
        final StringBuilder sb = new StringBuilder();
        length -= s.length();
        while (sb.length() < length) {
            sb.append(' ');
        }
        sb.append(s);
        return sb;
    }
    
    /*private static int maxLength(Set<? extends CharSequence> strings) {
        int max = Integer.MIN_VALUE;
        for (CharSequence s : strings) {
            if (s.length() > max) {
                max = s.length();
            }
        }
        return max;
    }*/
    
    private static StringBuilder getBar(final int i) {
        return multiply("-", i, " +", " " + i);
    }
    
    private void indent(final StringBuilder sb) {
        sb.append(multiply(" ", maxKeyLength));
    }
    
    private StringBuilder indent() {
        final StringBuilder sb = new StringBuilder();
        indent(sb);
        return sb;
    }
    
    private void getScale() {
        scale.add("");
        
        final StringBuilder sb = indent();
        for (int i = 0; i <= maxCount; i += scaleMarkerStep) {
            final String iAsString = String.valueOf(i);
            sb.append(multiply(" ", scaleMarkerStep - iAsString.length(), iAsString));
        }
        scale.add(sb.toString());
        sb.delete(maxKeyLength, sb.length());
        
        for (int i = 0; i <= maxCount; i++) {
            if (i % scaleMarkerStep == 0) {
                sb.append('+');
            } else {
                sb.append('-');
            }
        }
        scale.add(sb.toString());
        sb.delete(maxKeyLength, sb.length());
        
        sb.append('+');
        scale.add(sb.toString());
    }
    
    private void getTitle() {
        title = "Count of ";
        if (type == null) {
            title += "Unknown";
        } else {
            title += type.getSimpleName() + "s";
        }
        title = multiply(" ", maxKeyLength, "", title).toString();
    }
    
    private void getBars() {
        bars = new TreeSet<>();
        for (final T t : keys) {
            final StringBuilder bar = padLeft(t.toString(), maxKeyLength).append(getBar(counter.get(t).intValue()));
            bars.add(bar.toString());
        }
    }
    
    @Override
    public String toString() {
        keys = counter.keySet();
        maxKeyLength = maxLength(keys) + 1;
        maxCount = Collections.max(counter.values()).longValue();
        getTitle();
        getScale();
        maxKeyLength--;
        getBars();
        final List<String> graph = new ArrayList<>(1 + scale.size() + bars.size());
        graph.add(title);
        graph.addAll(scale);
        graph.addAll(bars);
        return '\n' + String.join("\n", graph) + '\n';
    }
    
}