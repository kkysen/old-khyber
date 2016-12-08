package sen.khyber.web.search.google.tests;

import sen.khyber.web.Internet;
import sen.khyber.web.search.google.GoogleSearch;
import sen.khyber.web.search.google.GoogleSearchResult;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class KhyberGoogleSearchTest {
    
    public static void main(final String[] args) throws Exception {
        final GoogleSearch search = new GoogleSearch("Khyber Sen", 100);
        final Stream<String> urls = search.getResults().parallelStream().map(GoogleSearchResult::getUrl);
        final Document doc = Jsoup.parseBodyFragment(urls.collect(Collectors.joining("\n")));
        Internet.suppressExceptions();
        final Stream<String> allUrls = Internet.getLinkedDocuments(doc, 1).flatMap(Internet::getLinkedUrls);
        final Set<String> urlSet = allUrls.filter(url -> url.endsWith(".jpg")).collect(Collectors.toCollection(TreeSet::new));
        urlSet.forEach(System.out::println);
    }
    
}
