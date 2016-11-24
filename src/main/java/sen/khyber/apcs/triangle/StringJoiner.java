package sen.khyber.apcs.triangle;

public class StringJoiner {
    
    private StringBuilder sb = new StringBuilder();
    private CharSequence delimiter;
    private CharSequence end;
    
    public StringJoiner(CharSequence delimiter, CharSequence start, CharSequence end) {
        this.delimiter = delimiter;
        this.end = end;
        sb.append(start);
    }
    
    public StringJoiner(CharSequence delimiter) {
        this(delimiter, "", "");
    }
    
    public void add(CharSequence s) {
        sb.append(s);
        sb.append(delimiter);
    }
    
    public String toString() {
        sb.append(end);
        return sb.toString();
    }
    
}
