package sen.khyber.language;

import lombok.Getter;

public enum Language {
    
    ENGLISH ("English", "src/sen.khyber.language/lexicons/english.txt", "src/sen.khyber.language/lexicons/english_letter_frequencies.csv"),
    //SPANISH ("Spanish", "src/sen.khyber.language/lexicons/spanish.txt"),
    FRENCH ("Spanish", "src/sen.khyber.language/lexicons/french.txt", ""),
    ;
    
    private @Getter String name;
    private @Getter String lexiconPath;
    private @Getter String letterFrequenciesPath;
    
    private Language(final String name, final String lexiconPath, final String letterFrequenciesPath) {
        this.name = name;
        this.lexiconPath = lexiconPath;
        this.letterFrequenciesPath = letterFrequenciesPath;
    }
    
}
