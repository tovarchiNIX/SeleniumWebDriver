package pages.shopping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import pages.base.BasePage;

public class ShoppingPage extends BasePage {
    /* Fields */
    private final Logger logger = LogManager.getLogger(ShoppingPage.class.getName());

    public ShoppingPage(WebDriver driver) {
        super(driver);
    }

    /* Locators */
    private static final By searchField = By.id("s");

    /* Methods */
    public SearchResultsPage performSearch(String searchQuery) {
        logger.info("Perform search by: " + searchQuery);
        driver.findElement(searchField).sendKeys(searchQuery, Keys.ENTER);
        return new SearchResultsPage(driver);
    }
}
