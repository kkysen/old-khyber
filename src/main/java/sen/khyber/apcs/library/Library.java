package sen.khyber.apcs.library;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Consumer;

public class Library {
    
    List<LibraryBook> bookList = new ArrayList<>();
    
    public Library() {}
    
    /**
     * adds the given book to the library
     * 
     * @param book book to add
     */
    public void addBook(final LibraryBook book) {
        bookList.add(book);
    }
    
    /**
     * prints all books in the library
     */
    public void printLibrary() {
        System.out.println("Listing of books in the library\n");
        System.out.println(this);
        System.out.println("End of book listing\n");
    }
    
    @Override
    public String toString() {
        final StringJoiner sj = new StringJoiner("\n");
        bookList.forEach(book -> sj.add(book.toString()));
        return sj.toString();
    }
    
    /**
     * locates a book in the library
     * 
     * @param book book being search in the library
     * @return book object if book is found
     *         null otherwise
     */
    public LibraryBook findBook(final LibraryBook book) {
        final int index = Collections.binarySearch(bookList, book);
        return index < 0 ? null : bookList.get(index);
    }
    
    /**
     * sort books in the library by call number
     */
    public void sortLibrary() {
        bookList.sort(null);
    }
    
    private LibraryBook searchItem(final String callNum) {
        return new CirculatingBook("", "", "", callNum);
    }
    
    private void findBookAnd(final String callNum, final Consumer<LibraryBook> action,
            final String actionName) {
        final LibraryBook book = findBook(searchItem(callNum));
        if (book == null) {
            System.out.println("Book " + callNum + " not found -- " + actionName + " impossible\n");
        } else {
            try {
                action.accept(book);
            } catch (final UnsupportedOperationException e) {
                //e.printStackTrace();
            }
        }
    }
    
    /**
     * performs processing for checking a book out of the library
     * 
     * @param patron person checking out book
     * @param dueDate date book is due to be returned
     * @param callNum call number of book
     */
    public void checkout(final String patron, final String dueDate, final String callNum) {
        findBookAnd(callNum, book -> book.checkout(patron, dueDate), "checkout");
    }
    
    /**
     * processes checked-out book that is being returned
     * 
     * @param callNum call number of book being returned
     */
    public void returned(final String callNum) {
        findBookAnd(callNum, LibraryBook::returned, "return");
    }
    
}
