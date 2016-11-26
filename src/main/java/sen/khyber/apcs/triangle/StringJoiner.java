package sen.khyber.apcs.triangle;

/**
 * 
 * 
 * @author Khyber Sen
 */
@Deprecated
public class StringJoiner {
    
    private final StringBuilder sb = new StringBuilder();
    private final CharSequence delimiter;
    private final CharSequence end;
    
    public StringJoiner(final CharSequence delimiter, final CharSequence start, final CharSequence end) {
        this.delimiter = delimiter;
        this.end = end;
        sb.append(start);
    }
    
    public StringJoiner(final CharSequence delimiter) {
        this(delimiter, "", "");
    }
    
    public void add(final CharSequence s) {
        sb.append(s);
        sb.append(delimiter);
    }
    
    @Override
    public String toString() {
        sb.append(end);
        return sb.toString();
    }
    
}
