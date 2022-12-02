package tests.base;

import common.CommonActions;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterSuite;
import pages.base.BasePage;
import pages.shopping.BasketPage;
import pages.shopping.ProductPage;
import pages.shopping.SearchResultsPage;
import pages.shopping.ShoppingPage;

public class BaseTest {
    protected WebDriver driver = CommonActions.createWebDriver();
    protected BasePage basePage = new BasePage(driver);
    protected ShoppingPage shoppingPage = new ShoppingPage(driver);
    protected SearchResultsPage searchResultsPage = new SearchResultsPage(driver);
    protected ProductPage productPage = new ProductPage(driver);
    protected BasketPage basketPage = new BasketPage(driver);

    @AfterSuite(alwaysRun = true)
    public void close() {
        driver.quit();
    }
}
