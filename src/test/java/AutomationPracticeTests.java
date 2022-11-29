import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import java.time.Duration;
import java.util.List;
import utils.MyUtils;

public class AutomationPracticeTests {

    WebDriver driver;
    String url = "https://practice.automationtesting.in/shop/";
    Logger logger = LogManager.getLogger(AutomationPracticeTests.class.getName());
    String searchQuery;
    WebDriverWait wait;

    @BeforeClass
    void setup() {
        logger.info("Setting up browser");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebDriverManager.chromedriver().setup();
        driver.manage().window().maximize();
    }

    @Test(description = "Testing search and shopping cart functionality")
    void shoppingTest() {
        logger.info("Starting test case...");
        driver.get(url);

        /* Landing Page */
        WebElement searchBar = driver.findElement(By.cssSelector("#s"));

        searchQuery = "html";
        logger.info("Performing search");
        searchBar.sendKeys(searchQuery + Keys.ENTER);

        /* Search Results Page */
        WebElement searchResultsPageTitle = driver.findElement(By.cssSelector("#content h1 em"));
        List<WebElement> searchResultsLinks = driver.findElements(By.cssSelector("h2 a"));

        logger.info("Asserting that browser tab title contains search query");
        Assert.assertTrue(driver.getTitle().contains(searchQuery));

        logger.info("Asserting that search results page title equals to search query");
        Assert.assertEquals(searchQuery, searchResultsPageTitle.getText().toLowerCase());

        logger.info("Asserting that each result link contains search query and each link has 'href' attribute");
        for (WebElement link : searchResultsLinks) {
            Assert.assertTrue(link.getAttribute("title").toLowerCase().contains(searchQuery));
            Assert.assertTrue(link.getAttribute("href").startsWith("https://"));
        }

        logger.info("Open product by its title");
        searchQuery = "Thinking in HTML";
        boolean itemIsPresent = false;
        WebElement productToSelect = null;
        for (WebElement link : searchResultsLinks) {
            if (link.getAttribute("title").equals(searchQuery)) {
                itemIsPresent = true;
                productToSelect = link;
            }
        }
        if (itemIsPresent){
            // This scroll is needed here due to Google Ads overlapping the page content
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,300)", "");
            productToSelect.click();
        } else {
            logger.error("Unable to find product by given name: " + searchQuery);
            throw new IllegalArgumentException();
        }

        /* 'Thinking in HTML' product Page */
        logger.info("Asserting that new shopping page with a certain shopping item has been opened");
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Assert.assertTrue(driver.getCurrentUrl().endsWith("/thinking-in-html/"));

        logger.info("Verifying that shopping item has 'Sale!' label along with corresponding image");
        WebElement saleImage = driver.findElement(By.className("onsale"));
        Assert.assertTrue(saleImage.isDisplayed());
        Assert.assertEquals(saleImage.getText(), "SALE!" );

        logger.info("Verifying that discount price is displayed along with original price");
        WebElement originalPriceLabel = driver.findElement(
                By.cssSelector("#product-163 > div.summary.entry-summary del > span"));
        WebElement discountPriceLabel = driver.findElement(By
                .cssSelector("#product-163 > div.summary.entry-summary ins > span"));
        Assert.assertTrue(originalPriceLabel.isDisplayed());
        Assert.assertTrue(discountPriceLabel.isDisplayed());

        logger.info("Find related product by name and open it");
        List<WebElement> relatedProducts = driver.findElements(By.cssSelector("ul.products>li"));

        searchQuery = "HTML5 WebApp Develpment";
        itemIsPresent = false;
        productToSelect = null;
        for (WebElement product : relatedProducts) {
            WebElement viewProductLink = product.findElement(By.cssSelector("a[class*='LoopProduct']"));
            WebElement productName = viewProductLink.findElement(By.cssSelector("h3"));
            if (productName.getText()
                    .equals(searchQuery)) {
                itemIsPresent = true;
                productToSelect = viewProductLink;
            }
        }

        if (itemIsPresent){
            // This scroll is needed here due to Google Ads overlapping the page content
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,1000)", "");
            productToSelect.click();
        } else {
            logger.error("Unable to find product by given name: " + searchQuery);
            throw new IllegalArgumentException();
        }

        /* 'HTML5 WebApp Develpment' product Page */
        logger.info("Verifying that appropriate page is opened");
        Assert.assertTrue(driver.getCurrentUrl().endsWith("/html5-webapp-develpment/"));

        WebElement addToBasketButton = driver.findElement(By.cssSelector("button[type='submit']"));
        addToBasketButton.click();

        logger.info("Verifying that information text contains product name after clicking 'Add to Basket' button");
        WebElement message = driver.findElement(By.cssSelector("div[class='woocommerce-message']"));
        Assert.assertTrue(message.getText().contains(searchQuery));

        logger.info("Clicking View Basket button");
        WebElement viewBasketButton = driver.findElement(By.cssSelector("#content > div.woocommerce-message > a"));
        viewBasketButton.click();

        /* 'Shopping Cart / Basket' Page */
        logger.info("Verifying that basket is opened");
        Assert.assertTrue(driver.getCurrentUrl().endsWith("/basket/"));

        logger.info("Verifying item details");
        WebElement shoppingCartItemName = driver.findElement(By.cssSelector("table tbody>tr>td.product-name>a"));
        WebElement shoppingCartItemPrice = driver.findElement(By.cssSelector("table tbody>tr>td.product-price>span"));
        WebElement shoppingCartItemTotalPrice = driver.findElement(By.cssSelector("table tbody>tr>td.product-subtotal>span"));
        WebElement shoppingCartItemQuantity = driver.findElement(By.cssSelector("table tbody>tr>td.product-quantity input"));
        String itemName = shoppingCartItemName.getText();
        String itemPrice = MyUtils.getPriceWithoutCurrencySymbol(shoppingCartItemPrice);
        String itemTotalPrice = MyUtils.getPriceWithoutCurrencySymbol(shoppingCartItemTotalPrice);
        Assert.assertEquals(itemName, searchQuery);
        Assert.assertEquals(itemPrice, "180.00");
        Assert.assertEquals(itemTotalPrice, "180.00");

        logger.info("Changing quantity of a product in shopping cart");
        shoppingCartItemQuantity.sendKeys(Keys.DELETE, Keys.NUMPAD3, Keys.ENTER);
        wait.until(ExpectedConditions.textToBe(By.cssSelector(".woocommerce-message"), "Basket updated."));

        logger.info("Verifying that total price is updated and name and price remains the same");
        WebElement itemNameAfterChange = driver.findElement(By.cssSelector("table tbody>tr>td.product-name>a"));
        WebElement itemPriceAfterChange = driver.findElement(By.cssSelector("table tbody>tr>td.product-price>span"));
        WebElement totalPriceUpdated = driver.findElement(By.cssSelector("table tbody>tr>td.product-subtotal>span"));
        Assert.assertEquals(itemNameAfterChange.getText(), itemName);
        Assert.assertEquals(MyUtils.getPriceWithoutCurrencySymbol(itemPriceAfterChange), itemPrice);
        Assert.assertEquals(MyUtils.getPriceWithoutCurrencySymbol(totalPriceUpdated), "540.00");
    }

    @Test(description = "Testing search results with parametrization")
    @Parameters({"searchOption"})
    void parametrizedTest(String searchOption) {
        logger.info("Starting test case...");
        driver.get(url);

        /* Landing Page */
        WebElement searchBar = driver.findElement(By.cssSelector("#s"));

        logger.info("Performing search");
        searchBar.sendKeys(searchOption + Keys.ENTER);

        logger.info("Asserting that search results page is displayed for the search query");
        WebElement searchResultsTitle = driver.findElement(By.cssSelector("#content>h1"));
        Assert.assertEquals(searchResultsTitle.getText(), String.format("SEARCH RESULTS FOR: %s", searchOption.toUpperCase()));
    }

    @AfterClass
    void tearDown() throws InterruptedException {
        Thread.sleep(3000L);
        driver.quit();
    }
}
