package sen.khyber.apcs.library;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * represents a library book comparable by its call number,
 * and thus sortable
 * 
 * @author Khyber Sen
 */
public abstract class LibraryBook extends Book implements Comparable<LibraryBook> {
    
    protected @Getter @Setter String callNum;
    
    public LibraryBook(final String title, final String author, final String isbn,
            final String callNum) {
        super(title, author, isbn);
        this.callNum = callNum;
    }
    
    public abstract void checkout(String patron, String due);
    
    public abstract void returned();
    
    public abstract String circulationStatus();
    
    @Override
    public int compareTo(final LibraryBook otherBook) {
        return callNum.compareTo(otherBook.callNum);
    }
    
    @Override
    protected void augmentToStringFields(final Map<String, Object> augmentedToStringFields) {
        super.augmentToStringFields(augmentedToStringFields);
        augmentedToStringFields.put("circulationStatus", circulationStatus());
    }
    
}
