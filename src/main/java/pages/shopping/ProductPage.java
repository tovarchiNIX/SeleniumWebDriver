package pages.shopping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import pages.base.BasePage;
import java.util.List;

public class ProductPage extends BasePage {
    /* Fields */
    private final Logger logger = LogManager.getLogger(ProductPage.class.getName());

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    /* Locators */
    private static final By saleImage = By.className("onsale");
    private static final By priceLabel = By.cssSelector("div.summary.entry-summary div > .price > span");
    private static final By originalPriceLabel = By.cssSelector("div.summary.entry-summary del > span");
    private static final By discountPriceLabel = By.cssSelector("div.summary.entry-summary ins > span");
    private static final By relatedProducts = By.cssSelector("ul.products>li");
    private static final By productLink = By.cssSelector("a[class*='LoopProduct']");
    private static final By productName = By.cssSelector("h3");
    private static final By addToBasketButton = By.cssSelector("button[type='submit']");
    private static final By messageSection = By.cssSelector("div[class='woocommerce-message']");
    private static final By viewBasketButton = By.cssSelector("#content > div.woocommerce-message > a");

    /* Methods */
    public ProductPage openRelatedProductByName(String relatedProductToOpen) {
        logger.info("Open product by name from Related Products section");
        boolean itemIsPresent = false;
        WebElement productToSelect = null;
        List<WebElement> allRelatedProducts = driver.findElements(relatedProducts);
        for (WebElement product : allRelatedProducts) {
            String productNameText = product.findElement(productLink).findElement(productName).getText();
            if (productNameText.equals(relatedProductToOpen)) {
                itemIsPresent = true;
                productToSelect = driver.findElement(productLink).findElement(productName);
            }
        }

        if (itemIsPresent){
            // This scroll is needed here due to Google Ads overlapping the page content
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,1000)", "");
            productToSelect.click();
        } else {
            logger.error("Unable to find product by given name: " + relatedProductToOpen);
            Assert.fail();
        }
        return new ProductPage(driver);
    }

    public ProductPage clickAddToBasketButton() {
        logger.info("Click 'Add to Basket' button");
        driver.findElement(addToBasketButton).click();
        return this;
    }

    public BasketPage clickViewBasketButton() {
        logger.info("Click 'View Basket' button");
        driver.findElement(viewBasketButton).click();
        return new BasketPage(driver);
    }

    public String getProductPrice() {
        logger.info("Get product price");
        return getPriceWithoutCurrencySymbol(priceLabel);
    }

    public String getOriginalProductPrice() {
        logger.info("Get original product price");
        return getPriceWithoutCurrencySymbol(originalPriceLabel);
    }

    public String getDiscountProductPrice() {
        logger.info("Get discount product price");
        return getPriceWithoutCurrencySymbol(discountPriceLabel);
    }

    public ProductPage verifySaleImageOnProduct() {
        logger.info("Verify that image with sale mark is displayed on product card");
        Assert.assertTrue(driver.findElement(saleImage).isDisplayed());
        return this;
    }

    public ProductPage verifySaleTextOnSaleImage() {
        logger.info("Verify that sale specific text is displayed on product card");
        Assert.assertEquals(driver.findElement(saleImage).getText(), "SALE!" );
        return this;
    }

    public ProductPage verifyOriginalPriceTextIsDisplayed() {
        logger.info("Verify that original price is displayed");
        Assert.assertTrue(driver.findElement(originalPriceLabel).isDisplayed());
        return this;
    }

    public ProductPage verifyDiscountPriceTextIsDisplayed() {
        logger.info("Verify that discount price is displayed");
        Assert.assertTrue(driver.findElement(discountPriceLabel).isDisplayed());
        return this;
    }

    public ProductPage verifyProductNameisDisplayedInSuccessMessage(String productName) {
        logger.info("Verify that appropriate product name is displayed in success message");
        Assert.assertTrue(driver.findElement(messageSection).getText().contains(productName));
        return this;
    }
}
