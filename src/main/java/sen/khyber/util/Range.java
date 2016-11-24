package sen.khyber.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Range implements Iterable<Integer> {
    
    private int start, stop, step;
    private int originalStop;
    
    public Range(int start, int stop, int step) {
        this.start = start;
        this.stop = stop;
        this.step = step;
        checkForZeroStep();
        handleBackwardsStep();
        handleIndivisibleStep();
    }
    
    private void checkForZeroStep() {
        if (step == 0) {
            throw new IllegalArgumentException(toString() + ": " + "step cannot be 0");
        }
    }
    
    private void handleBackwardsStep() {
        if (   ((start < stop) && (step < 0)) 
            || ((start > stop) && (step > 0))
            ) {
            stop = start;
        }
    }
    
    private void handleIndivisibleStep() {
        originalStop = stop;
        stop -= range() % step;
    }
    
    public Range(int start, int stop) {
        this(start, stop, 1);
    }
    
    public Range(int stop) {
        this(0, stop);
    }
    
    public int range() {
        return stop - start;
    }
    
    public int length() {
        return range() / step;
    }
    
    @Override
    public String toString() {
        return "(" + start + ", " + originalStop + ", " + step + ")";
    }
    
    @Override
    public RangeIterator iterator() {
        return new RangeIterator(start, stop, step);
    }
    
    public int[] toArray() {
        int[] arr = new int[length()];
        RangeIterator iter = iterator();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = iter.next();
        }
        return arr;
    }
    
    public List<Integer> toList() {
        List<Integer> list = new ArrayList<>(length());
        for (Integer i : this) {
            list.add(i);
        }
        return list;
    }
    
    public Set<Integer> toSet() {
        Set<Integer> set = new HashSet<>(length());
        for (Integer i : this) {
            set.add(i);
        }
        return set;
    }
    
    public static void main(String[] args) {
        System.out.println(new Range(1000, 0, -1).toSet());
    }
    
}
