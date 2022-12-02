package common;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.Assert;
import static common.Config.BROWSER;

public class CommonActions {
    public static WebDriver createWebDriver() {
        WebDriver driver = null;

        switch (BROWSER) {
            case "chrome":
                driver = new ChromeDriver();
                WebDriverManager.chromedriver().setup();
                break;
            case "firefox":
                driver = new FirefoxDriver();
                WebDriverManager.firefoxdriver().setup();
                break;
            case "ie":
                driver = new InternetExplorerDriver();
                WebDriverManager.iedriver().setup();
                break;
            case "edge":
                driver = new EdgeDriver();
                WebDriverManager.edgedriver().setup();
                break;
            default:
                Assert.fail("Incorrect browser provided: " + BROWSER);
        }
        driver.manage().window().maximize();
        return driver;
    }
}
