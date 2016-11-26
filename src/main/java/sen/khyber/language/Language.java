package sen.khyber.language;

import lombok.Getter;

public enum Language {
    
    ENGLISH ("English", "english.txt", "english_letter_frequencies.csv"),
    FRENCH ("Spanish", "french.txt", ""),
    ;
    
    private static class Constants {
        
        private static final String LEXICON_DIRECTORY = "src/main/java/sen/khyber/language/lexicons/";
        
    }
    
    private @Getter String name;
    private @Getter String lexiconPath;
    private @Getter String letterFrequenciesPath;
    
    private Language(final String name, final String lexiconPath, final String letterFrequenciesPath) {
        this.name = name;
        this.lexiconPath = Constants.LEXICON_DIRECTORY + lexiconPath;
        this.letterFrequenciesPath = Constants.LEXICON_DIRECTORY + letterFrequenciesPath;
    }
    
}
