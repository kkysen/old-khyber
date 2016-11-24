package sen.khyber.stats.counter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class BarGraph<T> {
    
    private Map<T, Long> counter;
    private Set<T> keys;
    private int maxKeyLength;
    private long maxCount;
    private String title;
    private int scaleMarkerStep = 5;
    private List<String> scale = new ArrayList<>(4);
    private Set<String> bars;
    private Class<T> type;
    
    public BarGraph(Map<T, Long> counter, Class<T> type) {
        this.counter = counter;
        this.type = type;
    }
    
    public BarGraph(Counter<T> counter) {
        this(counter.unsortedCounts(), counter.getType());
    }
    
    public BarGraph(List<Map.Entry<T, Long>> listCounter, Class<T> type) {
        Map<T, Long> mapCounter = new HashMap<>(listCounter.size());
        for (Map.Entry<T, Long> count : listCounter) {
            mapCounter.put(count.getKey(), count.getValue());
        }
        counter = mapCounter;
        this.type = type;
    }
    
    private static StringBuilder multiply(CharSequence s, int times, CharSequence start, CharSequence end) {
        StringBuilder sb = new StringBuilder(start);
        while (times-- > 0) {
            sb.append(s);
        }
        sb.append(end);
        return sb;
    }
    
    private static StringBuilder multiply(CharSequence s, int times, CharSequence start) {
        return multiply(s, times, start, "");
    }
    
    private static StringBuilder multiply(CharSequence s, int times) {
        return multiply(s, times, "");
    }
    
    private static <U> int maxLength(Collection<U> objects) {
        int max = Integer.MIN_VALUE;
        for (U u : objects) {
            int length = u.toString().length();
            if (length > max) {
                max = length;
            }
        }
        return max;
    }
    
    private static StringBuilder padLeft(String s, int length) {
        StringBuilder sb = new StringBuilder();
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
    
    private static StringBuilder getBar(int i) {
        return multiply("-", i, " +", " " + i);
    }
    
    private void indent(StringBuilder sb) {
        sb.append(multiply(" ", maxKeyLength));
    }
    
    private StringBuilder indent() {
        StringBuilder sb = new StringBuilder();
        indent(sb);
        return sb;
    }
    
    private void getScale() {
        scale.add("");
        
        StringBuilder sb = indent();
        for (int i = 0; i <= maxCount; i += scaleMarkerStep) {
            String iAsString = String.valueOf(i);
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
        for (T t : keys) {
            StringBuilder bar = padLeft(t.toString(), maxKeyLength).append(getBar(counter.get(t).intValue()));
            bars.add(bar.toString());
        }
    }
    
    public String toString() {
        keys = counter.keySet();
        maxKeyLength = maxLength(keys) + 1;
        maxCount = Collections.max(counter.values()).longValue();
        getTitle();
        getScale();
        maxKeyLength--;
        getBars();
        List<String> graph = new ArrayList<>(1 + scale.size() + bars.size());
        graph.add(title);
        graph.addAll(scale);
        graph.addAll(bars);
        return '\n' + String.join("\n", graph) + '\n';
    }
    
}