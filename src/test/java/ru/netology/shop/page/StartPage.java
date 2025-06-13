package ru.netology.shop.page;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class StartPage {
    private SelenideElement buyButton = $("button:not(.button_view_extra)");
    private SelenideElement creditButton = $("button.button_view_extra");

    public StartPage() {
        buyButton.shouldBe(visible);
        creditButton.shouldBe(visible);
    }

    public BuyPage clickBuyButton() {
        buyButton.click();
        return new BuyPage();
    }
}
