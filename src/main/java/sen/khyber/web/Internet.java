package sen.khyber.web;

// TODO add Selenium support for rendered documents

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.IncorrectnessListener;
import com.gargoylesoftware.htmlunit.InteractivePage;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.html.HTMLParserListener;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;

/**
 * utilities for reading the html from a website
 * 
 * @author Khyber Sen
 */
public class Internet {
    
    /**
     * Jsoup user agent string for Chrome
     */
    private static final String JSOUP_USER_AGENT = 
            "Mozilla/5.0 (Windows NT 10.0; WOW64) "
            + "AppleWebKit/537.36 (KHTML, like Gecko) "
            + "Chrome/53.0.2785.143 Safari/537.36";
    
    /**
     * Jsoup Google referrer
     */
    private static final String JSOUP_REFERRER = "http://www.google.com";
    
    /**
     * HtmlUnit Chrome browser version for WebClient
     */
    private static final BrowserVersion HTML_UNIT_BROWSER_VERSION = 
            BrowserVersion.CHROME;
    
    /**
     * reads the html of a website and returns it as a String
     * 
     * @param url URL of the website to be read
     * @return a String of the html of the website
     * @throws IOException an IOException
     */
    public static String read(URL url) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        return sb.toString();
    }
    
    /**
     * returns a Jsoup Document
     * 
     * @param url URL of the website
     * @return a Jsoup Document of the website
     * @throws IOException an IOException
     */
    public static Document getDocument(String url) throws IOException {
        return Jsoup.connect(url)
                    .userAgent(JSOUP_USER_AGENT)
                    .referrer(JSOUP_REFERRER)
                    .get();
    }
    
    public static Stream<Document> getDocuments(Stream<String> urls) {
        return urls.map(url -> {
            try {
                return getDocument(url);
            } catch (IOException e) {
                //e.printStackTrace();
                return Jsoup.parse("");
            }
        });
    }
    
    public static Stream<Document> getDocuments(String... urls) {
        return getDocuments(Stream.of(urls));
    }
    
    public static Stream<Document> getDocuments(Collection<String> urls) {
        return getDocuments(urls.parallelStream());
    }
    
    private static WebClient getSilencedWebClient() {
        
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", 
                "org.apache.commons.logging.impl.NoOpLog");
        Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
        Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
        
        final WebClient webClient = new WebClient(HTML_UNIT_BROWSER_VERSION);
        
        WebClientOptions webClientOptions = webClient.getOptions();
        webClientOptions.setCssEnabled(false);
        webClientOptions.setThrowExceptionOnFailingStatusCode(false);
        webClientOptions.setThrowExceptionOnScriptError(false);
        
        webClient.setCssErrorHandler(new SilentCssErrorHandler());
        
        webClient.setIncorrectnessListener(new IncorrectnessListener() {
            
            @Override
            public void notify(String arg0, Object arg1) {}
            
        });
        
        webClient.setJavaScriptErrorListener(new JavaScriptErrorListener() {
            
            @Override
            public void loadScriptError(InteractivePage arg0, URL arg1, 
                    Exception arg2) {}
            
            @Override
            public void malformedScriptURL(InteractivePage arg0, String arg1, 
                    MalformedURLException arg2) {}
            
            @Override
            public void scriptException(InteractivePage arg0, 
                    ScriptException arg1) {}
            
            @Override
            public void timeoutError(InteractivePage arg0, long arg1,
                    long arg2) {}
            
        });
        
        webClient.setHTMLParserListener(new HTMLParserListener() {
            
            @Override
            public void error(String arg0, URL arg1, String arg2, int arg3,
                    int arg4, String arg5) {}
            
            @Override
            public void warning(String arg0, URL arg1, String arg2, int arg3,
                    int arg4, String arg5) {}
            
        });
        
        return webClient;
        
    }
    
    public static Document getRenderedDocument(String url) 
            throws FailingHttpStatusCodeException, MalformedURLException, IOException {
        final WebClient webClient = getSilencedWebClient();
        HtmlPage page = webClient.getPage(url);
        File tempFile = new File("tempHtmlPage.html");
        page.save(tempFile);
        Document doc = Jsoup.parse(tempFile, "UTF-8");
        webClient.close();
        tempFile.delete();
        return doc;
    }
    
    public static Stream<Document> getRenderedDocuments(Stream<String> urls) {
        return urls.map(url -> {
            try {
                return getRenderedDocument(url);
            } catch (FailingHttpStatusCodeException | IOException e) {
                //e.printStackTrace();
                return Jsoup.parse("");
            }
        });
    }
    
    public static Stream<Document> getRenderedDocuments(String... urls) {
        return getRenderedDocuments(Stream.of(urls));
    }
    
    public static Stream<Document> getRenderedDocuments(Collection<String> urls) {
        return getRenderedDocuments(urls.parallelStream());
    }
    
    public static void main(String[] args) throws Exception {
        System.out.println(getRenderedDocument("http://google.com/search?q=hello").html());
    }
    
}
