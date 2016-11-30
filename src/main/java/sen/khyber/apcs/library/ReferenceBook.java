package sen.khyber.apcs.library;

import lombok.Getter;
import lombok.Setter;

/**
 * a reference book stored in a collection that can't be checked out
 * 
 * @author Khyber Sen
 */
public class ReferenceBook extends LibraryBook {
    
    private @Getter @Setter String collection;
    
    public ReferenceBook(final String title, final String author, final String isbn,
            final String callNumber, final String collection) {
        super(title, author, isbn, callNumber);
        this.collection = collection;
    }
    
    @Override
    public void checkout(final String patron, final String due) {
        System.out.println("cannot check out a reference book");
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void returned() {
        System.out.println("reference book could not have been checked out -- return impossible");
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String circulationStatus() {
        return "non-circulating reference book";
    }
    
}
