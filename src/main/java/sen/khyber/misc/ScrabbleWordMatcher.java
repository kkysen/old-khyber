package sen.khyber.misc;

import sen.khyber.language.Language;
import sen.khyber.language.Lexicon;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class ScrabbleWordMatcher {
    
    private static final Lexicon ENGLISH = Lexicon.safe(Language.ENGLISH);
    
    public static void main(final String[] args) {
        ENGLISH.matches("\\S*([a-z])\\1\\S*([a-z])\\2\\S*([a-z])\\3\\S*([a-z])\\4.*")
                .forEach(System.out::println);
    }
    
}
