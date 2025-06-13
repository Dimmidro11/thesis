package ru.netology.shop.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Assertions;
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

    Map<String, String> errors = new HashMap<>() {{
        put("success", "Операция одобрена Банком");
        put("error", "Ошибка! Банк отказал в проведении операции.");
        put("emptyField", "Поле обязательно для заполнения");
        put("incorrectDate", "Неверно указан срок действия карты");
        put("expiredCard", "Истёк срок действия карты");
        put("invalidFormat", "Неверный формат");
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
                shouldHave(text(errors.get("success")));
        successNotificationClose.click();
        errorNotification.shouldNotBe(visible,Duration.ofSeconds(5));
    }

    public void checkErrorNotification() {
        errorNotification.shouldBe(visible,Duration.ofSeconds(15)).
                shouldHave(text(errors.get("error")));
        errorNotificationClose.click();
        successNotification.shouldNotBe(visible,Duration.ofSeconds(5));
    }

    public void findFieldError(String field, String typeError) {
        fieldsError.get(field).shouldBe(visible).shouldHave(text(errors.get(typeError)));
    }

    public void checkFieldError(DataHelper.CardInfo cardInfo, String field, String typeError) {
        fillForm(cardInfo);
        findFieldError(field, typeError);
    }

    public void checkAllFieldError(DataHelper.CardInfo cardInfo) {
        fillForm(cardInfo);
        findFieldError("number", "emptyField");
        findFieldError("month", "emptyField");
        findFieldError("year", "emptyField");
        findFieldError("holder", "emptyField");
        findFieldError("cvc", "emptyField");
    }

    public void checkEmptyAfterInput(String field, String input) {
        fields.get(field).setValue(input).shouldBe(empty);
    }

    public void checkAboveMaxInput(String field, String input) {
        String expectedValue = "";
        switch (field) {
            case "number":
                expectedValue = input.substring(0, Math.min(input.length(), 16));
                expectedValue = expectedValue.replaceAll(".{4}", "$0 ").trim();
                break;
            case "month":
            case "year":
                expectedValue = input.substring(0, Math.min(input.length(), 2));
                break;
            case "holder":
                expectedValue = input.substring(0, Math.min(input.length(), 19));
                break;
            case "cvc":
                expectedValue = input.substring(0, Math.min(input.length(), 3));
                break;
        }
        fields.get(field).setValue(input);
        Assertions.assertEquals(expectedValue, fields.get(field).val());
    }
}
