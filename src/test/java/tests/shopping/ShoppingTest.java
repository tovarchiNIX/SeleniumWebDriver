package tests.shopping;

import common.CommonActions;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.base.BasePage;
import pages.shopping.BasketPage;
import pages.shopping.ProductPage;
import pages.shopping.SearchResultsPage;
import pages.shopping.ShoppingPage;
import static constants.Constants.ProductNames.HTML5_WEBAPP_DEVELOPMENT_PRODUCT_NAME;
import static constants.Constants.ProductNames.THINKING_IN_HTML_PRODUCT_NAME;
import static constants.Constants.Urls.*;
import static constants.Constants.Urls.BASKET_URL;

public class ShoppingTest {
    WebDriver driver;

    @BeforeClass
    public void setup() {
        driver = CommonActions.createWebDriver();
    }

    @Test(description = "Testing shopping cart functionality")
    public void shoppingTest() {
        BasePage basePage = new BasePage(driver);
        basePage.openUrl(SHOPPING_URL);

        String searchQuery = "html";
        ShoppingPage shoppingPage = new ShoppingPage(driver);
        shoppingPage
                .performSearch(searchQuery);

        SearchResultsPage searchResultsPage = new SearchResultsPage(driver);
        searchResultsPage
                .verifyBrowserTabTitle(searchQuery)
                .verifyEachProductContainsSearchQuery(searchQuery)
                .verifyEachProductLinkHasHrefAttribute()
                .verifySearchResultsPageTitle(searchQuery);

        ProductPage productPage = searchResultsPage.openProductByName(THINKING_IN_HTML_PRODUCT_NAME);
        productPage
                .verifyCurrentUrl(THINKING_IN_HTML_URL);
        productPage
                .verifySaleImageOnProduct()
                .verifySaleTextOnSaleImage()
                .verifyOriginalPriceTextIsDisplayed()
                .verifyDiscountPriceTextIsDisplayed()
                .openRelatedProductByName(HTML5_WEBAPP_DEVELOPMENT_PRODUCT_NAME)
                .verifyCurrentUrl(HTML5_WEBAPP_DEVELOPMENT_URL);
        productPage
                .clickAddToBasketButton()
                .verifyProductNameisDisplayedInSuccessMessage(HTML5_WEBAPP_DEVELOPMENT_PRODUCT_NAME);

        String productPrice = productPage.getProductPrice();
        BasketPage basketPage = productPage.clickViewBasketButton();
        basketPage.verifyCurrentUrl(BASKET_URL);

        String initialQuantity = "1";
        String newQuantity = "3";
        String totalPriceAfterChange = "540.00";
        basketPage
                .verifyProductIsPresentInBasket(HTML5_WEBAPP_DEVELOPMENT_PRODUCT_NAME)
                .verifyProductPriceInBasket(productPrice)
                .verifyProductTotalPriceInBasket(productPrice)
                .verifyQuantityOfProduct(initialQuantity)
                .changeQuantityOfProduct(newQuantity)
                .verifyBasketIsUpdated()
                .verifyProductIsPresentInBasket(HTML5_WEBAPP_DEVELOPMENT_PRODUCT_NAME)
                .verifyProductPriceInBasket(productPrice)
                .verifyProductTotalPriceInBasket(totalPriceAfterChange)
                .verifyQuantityOfProduct(newQuantity);
    }

    @Test(description = "Testing search results with parametrization")
    @Parameters("searchQuery")
    public void parametrizedTest(String searchQuery) {
        BasePage basePage = new BasePage(driver);
        basePage.openUrl(SHOPPING_URL);

        ShoppingPage shoppingPage = new ShoppingPage(driver);
        shoppingPage.performSearch(searchQuery);
        SearchResultsPage searchResultsPage = new SearchResultsPage(driver);
        searchResultsPage.verifySearchResultsPageTitle(searchQuery);
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
