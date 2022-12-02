package pages.shopping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pages.base.BasePage;
import java.time.Duration;
import static constants.Constants.TimeoutVariable.EXPLICITLY_WAIT_TIMEOUT;

public class BasketPage extends BasePage {
    /* Fields */
    private final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(EXPLICITLY_WAIT_TIMEOUT));
    private final Logger logger = LogManager.getLogger(BasketPage.class.getName());

    public BasketPage(WebDriver driver) {
        super(driver);
    }

    /* Locators */
    private static final By productLink = By.cssSelector("table tbody>tr>td.product-name>a");
    private static final By productPrice = By.cssSelector("table tbody>tr>td.product-price>span");
    private static final By productTotalPrice = By.cssSelector("table tbody>tr>td.product-subtotal>span");
    private static final By productQuantityField = By.cssSelector("table tbody>tr>td.product-quantity input");
    private static final By updatedBasketMessage = By.cssSelector(".woocommerce-message");

    /* Methods */
    public BasketPage changeQuantityOfProduct(String newQuantity) {
        logger.info("Change product quantity to: " + newQuantity);
        WebElement priceFieldInput = driver.findElement(productQuantityField);
        wait.until(ExpectedConditions.elementToBeClickable(priceFieldInput));
        priceFieldInput.sendKeys(Keys.DELETE, newQuantity, Keys.ENTER);
        return this;
    }

    public BasketPage verifyProductIsPresentInBasket(String productName) {
        logger.info("Verify that product is present in a basket: " + productName);
        String productInBasket = driver.findElement(productLink).getText();
        Assert.assertEquals(productInBasket, productName);
        return this;
    }

    public BasketPage verifyProductPriceInBasket(String expectedPrice) {
        logger.info("Verify product price in a basket. Expected price is: " + expectedPrice);
        String actualPrice = getPriceWithoutCurrencySymbol(productPrice);
        Assert.assertEquals(actualPrice, expectedPrice);
        return this;
    }

    public BasketPage verifyProductTotalPriceInBasket(String expectedTotalPrice) {
        logger.info("Verify total product price in a basket. Expected price is: " + expectedTotalPrice);
        String actualTotalPrice = getPriceWithoutCurrencySymbol(productTotalPrice);
        Assert.assertEquals(actualTotalPrice, expectedTotalPrice);
        return this;
    }

    public BasketPage verifyQuantityOfProduct(String expectedQuantity) {
        logger.info("Verify quantity of a product in a basket. Expected quantity is: " + expectedQuantity);
        String actualQuantity = driver.findElement(productQuantityField).getAttribute("value");
        Assert.assertEquals(actualQuantity, expectedQuantity);
        return this;
    }

    public BasketPage verifyBasketIsUpdated() {
        logger.info("Verify that basket is updated after the change");
        wait.until(ExpectedConditions.textToBe(updatedBasketMessage, "Basket updated."));
        return this;
    }
}
