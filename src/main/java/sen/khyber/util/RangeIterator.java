package sen.khyber.util;

import java.util.Iterator;


class RangeIterator implements Iterator<Integer> {
    
    private int current, stop, step;
    
    protected RangeIterator(int start, int stop, int step) {
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
