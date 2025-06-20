package ru.netology.shop.page;

import com.codeborne.selenide.*;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebElement;
import ru.netology.shop.data.DataHelper;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class BuyPage {
    private SelenideElement numberField = $("[placeholder='0000 0000 0000 0000']");
    private SelenideElement monthField = $("[placeholder='08']");
    private SelenideElement yearField = $("[placeholder='22']");
    private SelenideElement holderField = $("input:not([placeholder])");
    private SelenideElement cvcField = $("[placeholder='999']");
    private SelenideElement continueButton = $(".form-field button");

    private SelenideElement numberError = numberField.parent().sibling(0);
    private SelenideElement monthError = monthField.parent().sibling(0);
    private SelenideElement yearError = yearField.parent().sibling(0);
    private SelenideElement holderError = holderField.parent().sibling(0);
    private SelenideElement cvcError = cvcField.parent().sibling(0);

    private SelenideElement successNotification = $(".notification_status_ok");
    private SelenideElement successNotificationClose = $(".notification_status_ok button");
    private SelenideElement errorNotification = $(".notification_status_error");
    private SelenideElement errorNotificationClose = $(".notification_status_error button");

    Map<String, SelenideElement> fields = new HashMap<>() {{
        put("number", numberField);
        put("month", monthField);
        put("year", yearField);
        put("holder", holderField);
        put("cvc", cvcField);
    }};

    Map<String, SelenideElement> fieldsError = new HashMap<>() {{
        put("number", numberError);
        put("month", monthError);
        put("year", yearError);
        put("holder", holderError);
        put("cvc", cvcError);
    }};

    public void BuyPage() {
        numberField.shouldBe(visible);
        monthField.shouldBe(visible);
        yearField.shouldBe(visible);
        holderField.shouldBe(visible);
        cvcField.shouldBe(visible);
        continueButton.shouldBe(visible);
    }

    public void fillForm(DataHelper.CardInfo cardInfo) {
        numberField.setValue(cardInfo.getNumber());
        monthField.setValue(cardInfo.getMonth());
        yearField.setValue(cardInfo.getYear());
        holderField.setValue(cardInfo.getHolder());
        cvcField.setValue(cardInfo.getCvc());
        continueButton.click();
    }

    public void checkSuccessfulNotification() {
        successNotification.shouldBe(visible, Duration.ofSeconds(15)).
                shouldHave(text("Операция одобрена Банком"));
        successNotificationClose.click();
        errorNotification.shouldNotBe(visible,Duration.ofSeconds(5));
    }

    public void checkErrorNotification() {
        errorNotification.shouldBe(visible,Duration.ofSeconds(15)).
                shouldHave(text("Ошибка! Банк отказал в проведении операции."));
        errorNotificationClose.click();
        successNotification.shouldNotBe(visible,Duration.ofSeconds(5));
    }

    public void findFieldError(String field, String error) {
        fieldsError.get(field).shouldBe(visible).shouldHave(text(error));
    }

    public void checkFieldError(DataHelper.CardInfo cardInfo, String field, String typeError) {
        fillForm(cardInfo);
        findFieldError(field, typeError);
    }

    public void checkAllFieldError(DataHelper.CardInfo cardInfo, String error) {
        fillForm(cardInfo);
        findFieldError("number", error);
        findFieldError("month", error);
        findFieldError("year", error);
        findFieldError("holder", error);
        findFieldError("cvc", error);
    }

    public void checkEmptyAfterInput(String field, String input) {
        fields.get(field).setValue(input).shouldBe(empty);
    }

    public void checkAboveMaxInput(String field, DataHelper.Input input) {
        fields.get(field).setValue(input.getInput()).should(Condition.exactValue(input.getExpected()));
    }
}
