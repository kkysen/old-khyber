public abstract class LibraryBook implements Comparable<LibraryBook> {
    
    protected @Getter @Setter String callNumber;
    
    public LibraryBook(String title, String author, String isbn, String callNumber) {
        super(title, author, isbn);
        this.callNumber = callNumber;
    }
    
    public abstract void checkout();
    
    public abstract void returned();
    
    public abstract String circulationStatus();
    
    public int compareTo(LibraryBook otherBook) {
        return callNumber.compareTo(otherBook.callNumber);
    }
    
    // TODO eclipse toString
    
}
