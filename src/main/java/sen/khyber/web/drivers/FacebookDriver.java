package sen.khyber.web.drivers;

import java.util.function.Consumer;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class FacebookDriver {
    
    private final WebDriver driver;
    
    public FacebookDriver() {
        final ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        WebDriverInit.CHROME.init();
        driver = new ChromeDriver(options);
        driver.get("https://www.facebook.com/");
    }
    
    public void login(final String email, final String password) {
        final WebElement emailLogin = driver.findElement(By.id("email"));
        emailLogin.sendKeys(email);
        final WebElement passwordLogin = driver.findElement(By.id("pass"));
        passwordLogin.sendKeys(password);
        passwordLogin.submit();
    }
    
    private void keepTrying(final By refresher, final Consumer<WebElement> action) {
        WebElement element = driver.findElement(refresher);
        for (boolean finished = false; !finished; element = driver.findElement(refresher)) {
            try {
                action.accept(element);
                finished = true;
            } catch (final StaleElementReferenceException e) {
                continue;
            }
        }
    }
    
    @SafeVarargs
    private final void keepTrying(final By refresher, final Consumer<WebElement>... actions) {
        for (final Consumer<WebElement> action : actions) {
            keepTrying(refresher, action);
        }
    }
    
    public static void main(final String[] args) {
        final FacebookDriver fbDriver = new FacebookDriver();
        fbDriver.login("yuangenghis@gmail.com", "temujinborjigin@facebook");
        //final WebDriver driver = fbDriver.driver;
        fbDriver.keepTrying(By.className("_1frb"), elem -> elem.sendKeys("Khyber Sen"),
                WebElement::submit);
        fbDriver.keepTrying(By.className("_2yez"), WebElement::click);
    }
    
}
