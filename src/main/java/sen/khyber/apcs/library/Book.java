package sen.khyber.apcs.library;

import sen.khyber.reflect.Reflect;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * represents a book with a title, author, and isbn
 * 
 * @author Khyber Sen
 */
public class Book {
    
    protected @Getter @Setter String author;
    protected @Getter @Setter String title;
    protected @Getter @Setter String isbn;
    
    public Book() {}
    
    public Book(final String author, final String title, final String isbn) {
        this.author = author;
        this.title = title;
        this.isbn = isbn;
    }
    
    /**
     * optionally adds extra "fields" that should be included in #toString.
     * This implementation adds no extra fields but is meant for subclasses to use.
     * 
     * @see #toString
     *
     * @param augmentedToStringFields a map of the names of field names to their values as Objects
     *                                to be augmented by extra "fields" that should be included in #toString
     */
    protected void augmentToStringFields(final Map<String, Object> augmentedToStringFields) {}
    
    /**
     * uses reflection to include all fields,
     * in addition to added fields from #augmentToStringFields
     
     * @see #augmentToStringFields
     */
    @Override
    public String toString() {
        final Map<String, Object> augmentedToStringFields = Reflect.getStringInstanceVarEntries(this);
        augmentToStringFields(augmentedToStringFields);
        return Reflect.joinStringMap(augmentedToStringFields, this);
    }
    
}
