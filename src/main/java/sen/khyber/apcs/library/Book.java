package sen.khyber.apcs.library;

import sen.khyber.reflect.Reflect;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class Book {
    
    protected @Getter @Setter String title;
    protected @Getter @Setter String author;
    protected @Getter @Setter String isbn;
    
    public Book() {}
    
    public Book(final String title, final String author, final String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }
    
    protected void augmentToStringFields(final Map<String, Object> augmentedToStringFields) {}
    
    @Override
    public String toString() {
        final Map<String, Object> augmentedToStringFields = Reflect.getStringInstanceVarEntries(this);
        augmentToStringFields(augmentedToStringFields);
        return Reflect.joinStringMap(augmentedToStringFields, this);
    }
    
}
