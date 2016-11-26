package sen.khyber.language;

import sen.khyber.io.MyFiles;

import java.nio.file.Paths;
import java.util.Random;
import java.util.StringJoiner;

public class LoremIpsum {
    
    private static final String standard = "Lorem ipsum dolor sit amet, "
            + "consectetur adipisicing elit, "
            + "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. "
            + "Ut enim ad minim veniam, "
            + "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. "
            + "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. "
            + "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
    
    private static final String[] words = {
        "a", "ac", "accumsan", "ad", "adipiscing",
        "aenean", "aliquam", "aliquet", "amet", "ante",
        "aptent", "arcu", "at", "auctor", "augue",
        "bibendum", "blandit", "class", "commodo", "condimentum",
        "congue", "consectetur", "consequat", "conubia", "convallis",
        "cras", "cubilia", "cum", "curabitur", "curae",
        "cursus", "dapibus", "diam", "dictum", "dictumst",
        "dignissim", "dis", "dolor", "donec", "dui",
        "duis", "egestas", "eget", "eleifend", "elementum",
        "elit", "enim", "erat", "eros", "est",
        "et", "etiam", "eu", "euismod", "facilisi",
        "facilisis", "fames", "faucibus", "felis", "fermentum",
        "feugiat", "fringilla", "fusce", "gravida", "habitant",
        "habitasse", "hac", "hendrerit", "himenaeos", "iaculis",
        "id", "imperdiet", "in", "inceptos", "integer",
        "interdum", "ipsum", "justo", "lacinia", "lacus",
        "laoreet", "lectus", "leo", "libero", "ligula",
        "litora", "lobortis", "lorem", "luctus", "maecenas",
        "magna", "magnis", "malesuada", "massa", "mattis",
        "mauris", "metus", "mi", "molestie", "mollis",
        "montes", "morbi", "mus", "nam", "nascetur",
        "natoque", "nec", "neque", "netus", "nibh",
        "nisi", "nisl", "non", "nostra", "nulla",
        "nullam", "nunc", "odio", "orci", "ornare",
        "parturient", "pellentesque", "penatibus", "per", "pharetra",
        "phasellus", "placerat", "platea", "porta", "porttitor",
        "posuere", "potenti", "praesent", "pretium", "primis",
        "proin", "pulvinar", "purus", "quam", "quis",
        "quisque", "rhoncus", "ridiculus", "risus", "rutrum",
        "sagittis", "sapien", "scelerisque", "sed", "sem",
        "semper", "senectus", "sit", "sociis", "sociosqu",
        "sodales", "sollicitudin", "suscipit", "suspendisse", "taciti",
        "tellus", "tempor", "tempus", "tincidunt", "torquent",
        "tortor", "tristique", "turpis", "ullamcorper", "ultrices",
        "ultricies", "urna", "ut", "varius", "vehicula",
        "vel", "velit", "venenatis", "vestibulum", "vitae",
        "vivamus", "viverra", "volutpat", "vulputate",
    };
    private static final String[] punctuation = {".", "?"};
    private static final Random random = new Random();
    
    public LoremIpsum() {}
    
    public String word() {
        return words[random.nextInt(words.length)];
    }
    
    public String punctuation() {
        return punctuation[random.nextInt(punctuation.length)];
    }
    
    public String words(int count) {
        final StringJoiner sj = new StringJoiner(" ");
        while (count-- > 0) {
            sj.add(word());
        }
        return sj.toString();
    }
    
    public String sentenceFragment() {
        return words(random.nextInt(10) + 3);
    }
    
    private static String capitalize(final String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }
    
    public String sentence() {
        final StringBuilder sb = new StringBuilder();
        sb.append(capitalize(word()));
        sb.append(" ");
        if (random.nextBoolean()) {
            for (int r = random.nextInt(3) + 1; r > 0; r--) {
                sb.append(sentenceFragment()).append(", ");
            }
        }
        sb.append(sentenceFragment());
        sb.append(punctuation());
        return sb.toString();
    }
    
    public String sentences(int count) {
        final StringJoiner sj = new StringJoiner("  ");
        while (count-- > 0) {
            sj.add(sentence());
        }
        return sj.toString();
    }
    
    public String paragraph(final boolean useStandard) {
        return useStandard ? standard : sentences(random.nextInt(3) + 2);
    }
    
    public String paragraph() {
        return paragraph(false);
    }
    
    public String paragraphs(int count, final boolean useStandard) {
        final StringJoiner sj = new StringJoiner("\n\n", paragraph(useStandard), "");
        while (count-- > 1) {
            sj.add(paragraph());
        }
        return sj.toString();
    }
    
    public String paragraphs(final int count) {
        return paragraphs(count, false);
    }
    
    public String story(final boolean useStandard) {
        return paragraphs(random.nextInt(3) + 2, useStandard);
    }
    
    public String story() {
        return story(false);
    }
    
    public static void main(final String[] args) throws Exception {
        final LoremIpsum ipsum = new LoremIpsum();
        MyFiles.write(Paths.get("testFile.txt"), ipsum.paragraphs(1000));
        //System.out.println(ipsum.story());
        //Files.write(Paths.get("src/testFile.txt"), "hello, world".getBytes(charset));
        //System.out.println(Paths.get("src/testFile.txt").toAbsolutePath());
    }
    
}
