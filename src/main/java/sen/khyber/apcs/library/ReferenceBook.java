public class ReferenceBook extends LibraryBook {
    
    private @Getter @Setter String collection;
    
    public ReferenceBook(String title, String author, String isbn, String callNumber, String collection) {
        super(title, author, isbn, callNumber);
        this.collection = collection;
    }
    
    public void checkout() {
        System.out.println("cannot check out a reference book");
        // throw new Exception();
    }
    
    public void returned() {
        System.out.println("reference book could not have been checked out -- return impossible");
        // throw new Exception();
    }
    
    public String circulationStatus() {
        return "non-circulating reference book";
    }
    
    // TODO eclipse generate toString
    
}
