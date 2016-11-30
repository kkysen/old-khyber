package sen.khyber.apcs.library;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class CirculatingBook extends LibraryBook {
    
    private @Getter @Setter String currentHolder = null;
    private @Getter @Setter String dueDate = null;
    
    public CirculatingBook(final String title, final String author, final String isbn, final String callNumber) {
        super(title, author, isbn, callNumber);
    }
    
    @Override
    public void checkout(final String currentHolder, final String dueDate) {
        this.currentHolder = currentHolder;
        this.dueDate = dueDate;
    }
    
    @Override
    public void returned() {
        currentHolder = null;
        dueDate = null;
    }
    
    @Override
    public String circulationStatus() {
        if (currentHolder == null) {
            return "book available on shelves";
        }
        return "checked out by " + currentHolder + " until " + dueDate;
    }
    
}
