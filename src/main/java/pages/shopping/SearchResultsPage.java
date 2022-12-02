package pages.shopping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import pages.base.BasePage;

public class SearchResultsPage extends BasePage {
    /* Fields */
    private final Logger logger = LogManager.getLogger(SearchResultsPage.class.getName());

    public SearchResultsPage(WebDriver driver) {
        super(driver);
    }
    /* Locators */
    private static final By pageTitle = By.cssSelector("#content>h1");
    private static final By productLinks = By.cssSelector("h2 a");

    /* Methods */
    public ProductPage openProductByName(String productName) {
        logger.info("Open product by its name: " + productName);
        boolean itemIsPresent = false;
        WebElement productToSelect = null;
        for (WebElement link : driver.findElements(productLinks)) {
            if (link.getAttribute("title").equals(productName)) {
                itemIsPresent = true;
                productToSelect = link;
            }
        }
        if (itemIsPresent){
            // This scroll is needed here due to Google Ads overlapping the page content
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,300)", "");
            productToSelect.click();
        } else {
            logger.error("Unable to find product by given name: " + productName);
            Assert.fail();
        }
        return new ProductPage(driver);
    }

    public SearchResultsPage verifyBrowserTabTitle(String searchQuery) {
        logger.info("Verify that browser tab contains: " + searchQuery);
        Assert.assertTrue(driver.getTitle().contains(searchQuery));
        return this;
    }

    public SearchResultsPage verifySearchResultsPageTitle(String searchQuery) {
        logger.info("Verify that search results page contains: " + searchQuery);
        String title = driver.findElement(pageTitle).getText();
        Assert.assertEquals(title, String.format("SEARCH RESULTS FOR: %s", searchQuery.toUpperCase()));
        return this;
    }

    public SearchResultsPage verifyEachProductContainsSearchQuery(String searchQuery) {
        logger.info("Verify that each product on the page contains: " + searchQuery);
        for (WebElement link : driver.findElements(productLinks)) {
            Assert.assertTrue(link.getAttribute("title").toLowerCase().contains(searchQuery));
        }
        return this;
    }

    public SearchResultsPage verifyEachProductLinkHasHrefAttribute() {
        logger.info("Verify that each product link has 'href' attribute and starts with 'https://'");
        for (WebElement link : driver.findElements(productLinks)) {
            Assert.assertTrue(link.getAttribute("href").startsWith("https://"));
        }
        return this;
    }
}
