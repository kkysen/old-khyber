package sen.khyber.web.drivers;

/**
 * 
 * 
 * @author Khyber Sen
 */
public enum WebDriverInit {
    
    CHROME("webdriver.chrome.driver", "chromedriver.exe"),
    FIREFOX("webdriver.gecko.driver", "geckodriver.exe"),
    EDGE("webdriver.edge.driver", "unknown"),
    IE("webdriver.ie.driver", "unknown");
    
    private static class Constants {
        
        private static final String DIRECTORY = "C:/Users/kkyse/OneDrive/CS/Eclipse/webdrivers/";
        
    }
    
    private String property;
    private String path;
    
    private WebDriverInit(final String property, final String path) {
        this.property = property;
        this.path = Constants.DIRECTORY + path;
    }
    
    public void init() {
        System.setProperty(property, path);
    }
    
}
