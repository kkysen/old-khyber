package sen.khyber.util;

import java.util.Iterator;

/**
 * 
 * 
 * @author Khyber Sen
 */
class RangeIterator implements Iterator<Integer> {
    
    private int current;
    private final int stop;
    private final int step;
    
    protected RangeIterator(final int start, final int stop, final int step) {
        current = start - step;
        this.stop = stop - step;
        this.step = step;
    }
    
    @Override
    public boolean hasNext() {
        return current != stop;
    }
    
    @Override
    public Integer next() {
        current += step;
        return current;
    }
    
}
