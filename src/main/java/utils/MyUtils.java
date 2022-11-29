package utils;

import org.openqa.selenium.WebElement;

public class MyUtils {

    public static String getPriceWithoutCurrencySymbol(WebElement priceElement) {
        String priceWithCurrencySymbol = priceElement.getText();
        return priceWithCurrencySymbol.replace(Character
                .toString(priceWithCurrencySymbol.charAt(0)), "");
    }
}
