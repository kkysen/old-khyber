public class CirculatingBook extends LibraryBook {
    
    private @Getter @Setter String currentHolder = null;
    private @Getter @Setter String dueDate = null;
    
    public CirculatingBook(String title, String author, String isbn, String callNumber) {
        super(title, author, isbn, callNumber);
    }
    
    // FIXME
    public void checkout() {
        System.out.println("cannot check out a reference book");
        // throw new Exception();
    }
    
    // FIXME
    public void returned() {
        System.out.println("reference book could not have been checked out -- return impossible");
        // throw new Exception();
    }
    
    // FIXME
    public String circulationStatus() {
        return "non-circulating reference book";
    }
    
    // TODO eclipse generate toString
    
}
