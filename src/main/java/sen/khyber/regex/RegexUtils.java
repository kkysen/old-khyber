package sen.khyber.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {
    
    private static final String urlPattern = 
        "\\b(https?|ftp|file)://"
        + "[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    
    public static List<String> findMatches(CharSequence s, String pattern) {
        Matcher matcher = Pattern.compile(pattern).matcher(s);
        List<String> matches = new ArrayList<>();
        while (matcher.find()) {
            matches.add(matcher.group());
        }
        return matches;
    }
    
    public static List<String> findUrls(CharSequence s) {
        return findMatches(s, urlPattern);
    }
    
}
