package pages.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class BasePage {
    /* Fields */
    protected WebDriver driver;
    private final Logger logger = LogManager.getLogger(BasePage.class.getName());

    public BasePage(WebDriver driver) {
        this.driver = driver;
    }

    /* Methods */
    public void openUrl(String url) {
        driver.get(url);
    }

    public void verifyCurrentUrl(String urlToVerify) {
        logger.info("Verify current URL: " + urlToVerify);
        /* This sleep is needed here to manually close iframe with ads. Ads pop up in different places and different size*/
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Assert.assertEquals(urlToVerify, driver.getCurrentUrl());
    }

    public String getPriceWithoutCurrencySymbol(By element) {
        logger.info("Get price without currency symbol");
        String priceWithCurrencySymbol = driver.findElement(element).getText();
        return priceWithCurrencySymbol.replace(Character
                .toString(priceWithCurrencySymbol.charAt(0)), "");
    }
}
