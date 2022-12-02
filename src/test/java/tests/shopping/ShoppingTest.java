package tests.shopping;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import tests.base.BaseTest;
import static constants.Constants.ProductNames.HTML5_WEBAPP_DEVELOPMENT_PRODUCT_NAME;
import static constants.Constants.ProductNames.THINKING_IN_HTML_PRODUCT_NAME;
import static constants.Constants.Urls.*;

public class ShoppingTest extends BaseTest {

    @Test(description = "Testing shopping cart functionality")
    public void shoppingTest() {
        basePage.openUrl(SHOPPING_URL);

        String searchQuery = "html";
        shoppingPage
                .performSearch(searchQuery);

        searchResultsPage
                .verifyBrowserTabTitle(searchQuery)
                .verifyEachProductContainsSearchQuery(searchQuery)
                .verifyEachProductLinkHasHrefAttribute()
                .verifySearchResultsPageTitle(searchQuery);

        productPage = searchResultsPage.openProductByName(THINKING_IN_HTML_PRODUCT_NAME);
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
        basketPage = productPage.clickViewBasketButton();
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
        basePage.openUrl(SHOPPING_URL);
        shoppingPage
                .performSearch(searchQuery)
                .verifySearchResultsPageTitle(searchQuery);
    }
}
