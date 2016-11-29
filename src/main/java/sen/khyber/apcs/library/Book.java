public class Book {
    
    protected @Getter @Setter String title;
    protected @Getter @Setter String author;
    protected @Getter @Setter String isbn;
    
    public Book() {
        title = author = isbn = null;
    }
    
    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }
    
    // TODO eclipse generate toString
    
}
