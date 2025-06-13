package ru.netology.shop.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.shop.data.DataHelper;
import ru.netology.shop.data.SQLHelper;
import ru.netology.shop.page.StartPage;

import javax.xml.crypto.Data;

import static com.codeborne.selenide.Selenide.open;

public class TravelTest {
    public static final String approvedCard = "1111222233334444";
    public static final String declineCard = "5555666677778888";
    public static final String hit = "APPROVED";
    public static final String fail = "DECLINED";

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void setup() {
        open("http://localhost:8080");
    }

    @AfterEach
    void cleanDB() {
        SQLHelper.cleanTables();
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }


    // WEB тесты

    // Позитивные сценарии

    @Test
    @DisplayName("Successful buy on APPROVED card")
    void shouldSuccessfulBuyOnApprovedCard() {
        var card = DataHelper.generateCard(approvedCard);
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.fillForm(card);
        buyPage.checkSuccessfulNotification();

        Assertions.assertEquals(hit, SQLHelper.getStatus());
    }

    @Test
    @DisplayName("Fail buy on DECLINED card")
    void shouldFailedBuyOnDeclinedCard() {
        var card = DataHelper.generateCard(declineCard);
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.fillForm(card);
        buyPage.checkErrorNotification();

        Assertions.assertEquals(fail, SQLHelper.getStatus());
    }

    @Test
    @DisplayName("Successful buy on APPROVED card with holder 2 latin symbol")
    void shouldFailedBuyOnApprocedCardWithHolder2Symbol() {
        var date = DataHelper.generateValidDate();
        var card = new DataHelper.CardInfo(
                approvedCard,
                date.getMonth(),
                date.getYear(),
                DataHelper.generateHolder2Symbol(),
                DataHelper.generateRequiredNumber(3, 3)
        );
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.fillForm(card);
        buyPage.checkSuccessfulNotification();

        Assertions.assertEquals(hit, SQLHelper.getStatus());
    }

    @Test
    @DisplayName("Successful buy on APPROVED card, but month is 01")
    void shouldSuccessfulBuyOnApprovedCardButMonthIs1() {
        var card = new DataHelper.CardInfo(
                approvedCard,
                "01",
                DataHelper.generateYearAboveCurrentByTerm(1, true),
                DataHelper.generateValidHolder(),
                DataHelper.generateRequiredNumber(3, 3)
        );
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.fillForm(card);
        buyPage.checkSuccessfulNotification();

        Assertions.assertEquals(hit, SQLHelper.getStatus());
    }

    @Test
    @DisplayName("Successful buy on APPROVED card, but month is 12")
    void shouldSuccessfulBuyOnApprovedCardButMonthIs12() {
        var card = new DataHelper.CardInfo(
                approvedCard,
                "12",
                DataHelper.generateValidDate().getYear(),
                DataHelper.generateValidHolder(),
                DataHelper.generateRequiredNumber(3, 3)
        );
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.fillForm(card);
        buyPage.checkSuccessfulNotification();

        Assertions.assertEquals(hit, SQLHelper.getStatus());
    }

    @Test
    @DisplayName("Successful buy on APPROVED card, but year is valid max (more by 5)")
    void shouldSuccessfulBuyOnApprovedCardButYearMoreBy5() {
        var card = new DataHelper.CardInfo(
                approvedCard,
                DataHelper.generateValidDate().getMonth(),
                DataHelper.generateYearAboveCurrentByTerm(5, true),
                DataHelper.generateValidHolder(),
                DataHelper.generateRequiredNumber(3, 3)
        );
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.fillForm(card);
        buyPage.checkSuccessfulNotification();

        Assertions.assertEquals(hit, SQLHelper.getStatus());
    }

    // Негативные сценарии

    @Test
    @DisplayName("Should get error, empty fields")
    void shouldGetErrorAllFieldsEmpty() {
        var card = new DataHelper.CardInfo(
                "",
                "",
                "",
                "",
                ""
        );
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.checkAllFieldError(card);
    }

    @Test
    @DisplayName("Should get error, empty field CardNumber")
    void shouldGetErrorEmtyFieldCardNumber() {
        var card = DataHelper.generateCard("");
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.checkFieldError(card, "number", "emptyField");
    }

    @Test
    @DisplayName("Should get error, empty field Month")
    void shouldGetErrorEmptyFieldMonth() {
        var card = new DataHelper.CardInfo(
                approvedCard,
                "",
                DataHelper.generateValidDate().getYear(),
                DataHelper.generateValidHolder(),
                DataHelper.generateRequiredNumber(3, 3)
        );
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.checkFieldError(card, "month", "emptyField");
    }

    @Test
    @DisplayName("Should get error, empty field Year")
    void shouldGetErrorEmptyFieldYear() {
        var card = new DataHelper.CardInfo(
                approvedCard,
                DataHelper.generateValidDate().getMonth(),
                "",
                DataHelper.generateValidHolder(),
                DataHelper.generateRequiredNumber(3, 3)
        );
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.checkFieldError(card, "year", "emptyField");
    }

    @Test
    @DisplayName("Should get error, empty field Holder")
    void shouldGetErrorEmptyFieldHolder() {
        var date = DataHelper.generateValidDate();
        var card = new DataHelper.CardInfo(
                approvedCard,
                date.getMonth(),
                date.getYear(),
                "",
                DataHelper.generateRequiredNumber(3, 3)
        );
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.checkFieldError(card, "holder", "emptyField");
    }

    @Test
    @DisplayName("Should get error, empty field CVC")
    void shouldGetErrorEmptyFieldCvc() {
        var date = DataHelper.generateValidDate();
        var card = new DataHelper.CardInfo(
                approvedCard,
                date.getMonth(),
                date.getYear(),
                DataHelper.generateValidHolder(),
                ""
        );
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.checkFieldError(card, "cvc", "emptyField");
    }

    @Test
    @DisplayName("Failed buy, invalid CardNumber (not APPROVED/DECLINED)")
    void shouldNotBuyInvalidCardNumber() {
        var card = DataHelper.generateCard(DataHelper.generateRandomCardNumber());
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.fillForm(card);
        buyPage.checkErrorNotification();

        Assertions.assertNull(SQLHelper.getStatus());
    }

    @Test
    @DisplayName("Should not input cyrillic symbol on field CardNumber")
    void shouldNotInputCyrillicSymbolOnCardNumber() {
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.checkEmptyAfterInput("number", DataHelper.generateCyrillicString());
    }

    @Test
    @DisplayName("Should not input specials symbol on field CardNumber")
    void shouldNotInputSpecialsSymbolOnCardNumber() {
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.checkEmptyAfterInput("number", DataHelper.generateSpecialSymbols());
    }

    @Test
    @DisplayName("Should not input cyrillic symbol on field Month")
    void shouldNotInputCyrillicSymbolOnMonth() {
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.checkEmptyAfterInput("month", DataHelper.generateCyrillicString());
    }

    @Test
    @DisplayName("Should not input latin symbol on field Month")
    void shouldNotInputLatinSymbolOnMonth() {
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.checkEmptyAfterInput("month", DataHelper.generateLatinString(10, 10));
    }

    @Test
    @DisplayName("Should not input specials symbol on field Month")
    void shouldNotInputSpecialsSymbolOnMonth() {
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.checkEmptyAfterInput("month", DataHelper.generateSpecialSymbols());
    }

    @Test
    @DisplayName("Should not input cyrillic symbol on field Year")
    void shouldNotInputCyrillicSymbolOnYear() {
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.checkEmptyAfterInput("year", DataHelper.generateCyrillicString());
    }

    @Test
    @DisplayName("Should not input latin symbol on field Year")
    void shouldNotInputLatinSymbolOnYear() {
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.checkEmptyAfterInput("year", DataHelper.generateLatinString(10, 10));
    }

    @Test
    @DisplayName("Should not input specials symbol on field Year")
    void shouldNotInputSpecialsSymbolOnYear() {
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.checkEmptyAfterInput("year", DataHelper.generateSpecialSymbols());
    }

    @Test
    @DisplayName("Should not input cyrillic symbol on field Holder")
    void shouldNotInputCyrillicSymbolOnHolder() {
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.checkEmptyAfterInput("holder", DataHelper.generateCyrillicString());
    }

    @Test
    @DisplayName("Should not input numeral symbol on field Holder")
    void shouldNotInputNumeralSymbolOnHolder() {
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.checkEmptyAfterInput("holder", DataHelper.generateRequiredNumber(10, 10));
    }

    @Test
    @DisplayName("Should not input specials symbol on field Holder")
    void shouldNotInputSpecialsSymbolOnHolder() {
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.checkEmptyAfterInput("holder", DataHelper.generateSpecialSymbols());
    }

    @Test
    @DisplayName("Should not input cyrillic symbol on field CVC")
    void shouldNotInputCyrillicSymbolOnCvc() {
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.checkEmptyAfterInput("cvc", DataHelper.generateCyrillicString());
    }

    @Test
    @DisplayName("Should not input latin symbol on field CVC")
    void shouldNotInputLatinSymbolOnCvc() {
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.checkEmptyAfterInput("cvc", DataHelper.generateLatinString(10, 10));
    }

    @Test
    @DisplayName("Should not input specials symbol on field CVC")
    void shouldNotInputSpecialsSymbolOnCvc() {
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.checkEmptyAfterInput("cvc", DataHelper.generateSpecialSymbols());
    }

    @Test
    @DisplayName("Should get error, invalid month less min")
    void shouldGetErrorInvalidMonthLessMin() {
        var card = new DataHelper.CardInfo(
                approvedCard,
                "00",
                DataHelper.generateValidDate().getYear(),
                DataHelper.generateValidHolder(),
                DataHelper.generateRequiredNumber(3, 3)
        );
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.checkFieldError(card, "month", "incorrectDate");
    }

    @Test
    @DisplayName("Should get error, invalid month equals 13")
    void shouldGetErrorInvalidMonthEquals13() {
        var card = new DataHelper.CardInfo(
                approvedCard,
                "13",
                DataHelper.generateValidDate().getYear(),
                DataHelper.generateValidHolder(),
                DataHelper.generateRequiredNumber(3, 3)
        );
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.checkFieldError(card, "month", "incorrectDate");
    }

    @Test
    @DisplayName("Should get error, invalid month above max")
    void shouldGetErrorInvalidMonthAboveMax() {
        var card = new DataHelper.CardInfo(
                approvedCard,
                DataHelper.generateRandomNumber2Symbol(14, 99),
                DataHelper.generateValidDate().getYear(),
                DataHelper.generateValidHolder(),
                DataHelper.generateRequiredNumber(3, 3)
        );
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.checkFieldError(card, "month", "incorrectDate");
    }

    @Test
    @DisplayName("Should get error, invalid month less current, year current")
    void shouldGetErrorInvalidMonthLessCurrentYearCurrent() {
        var card = new DataHelper.CardInfo(
                approvedCard,
                DataHelper.generateMonthLessCurrent(),
                DataHelper.generateCurrentDate().getYear(),
                DataHelper.generateValidHolder(),
                DataHelper.generateRequiredNumber(3, 3)
        );
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.checkFieldError(card, "month", "expiredCard");
    }

    @Test
    @DisplayName("Should get error, invalid year less current by 1")
    void shouldGetErrorInvalidYearLessCurrentBy1() {
        var card = new DataHelper.CardInfo(
                approvedCard,
                DataHelper.generateCurrentDate().getMonth(),
                DataHelper.generateYearAboveCurrentByTerm(1, false),
                DataHelper.generateValidHolder(),
                DataHelper.generateRequiredNumber(3, 3)
        );
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.checkFieldError(card, "year", "expiredCard");
    }

    @Test
    @DisplayName("Should get error, invalid year less current")
    void shouldGetErrorInvalidYearLessCurrent() {
        var card = new DataHelper.CardInfo(
                approvedCard,
                DataHelper.generateCurrentDate().getMonth(),
                DataHelper.generateRandomNumber2Symbol(0,
                        (Integer.parseInt(DataHelper.generateCurrentDate().getYear())) - 2),
                DataHelper.generateValidHolder(),
                DataHelper.generateRequiredNumber(3, 3)
        );
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.checkFieldError(card, "year", "expiredCard");
    }

    @Test
    @DisplayName("Should get error, invalid year above current by 6")
    void shouldGetErrorInvalidYearAboveCurrentBy1() {
        var card = new DataHelper.CardInfo(
                approvedCard,
                DataHelper.generateCurrentDate().getMonth(),
                DataHelper.generateYearAboveCurrentByTerm(6, true),
                DataHelper.generateValidHolder(),
                DataHelper.generateRequiredNumber(3, 3)
        );
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.checkFieldError(card, "year", "incorrectDate");
    }

    @Test
    @DisplayName("Should get error, invalid year above max")
    void shouldGetErrorInvalidYearAboveMax() {
        var card = new DataHelper.CardInfo(
                approvedCard,
                DataHelper.generateCurrentDate().getMonth(),
                DataHelper.generateRandomNumber2Symbol((Integer.parseInt(DataHelper.generateCurrentDate().getYear()) + 6), 99),
                DataHelper.generateValidHolder(),
                DataHelper.generateRequiredNumber(3, 3)
        );
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.checkFieldError(card, "year", "incorrectDate");
    }

    @Test
    @DisplayName("Should get error, CardNumber less 16 numbers")
    void shouldGetErrorCardNumLess16numbers() {
        var card = DataHelper.generateCard(DataHelper.generateRequiredNumber(1, 15));
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.checkFieldError(card, "number", "invalidFormat");
    }

    @Test
    @DisplayName("Should not input CardNumber above 16 numbers")
    void shouldNotInputCardNumAbove16numbers() {
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.checkAboveMaxInput("number", DataHelper.generateRequiredNumber(17, 99));
    }

    @Test
    @DisplayName("Should get error, Month less 2 numbers")
    void shouldGetErrorMonthLess2Numbers() {
        var card = new DataHelper.CardInfo(
          approvedCard,
          DataHelper.generateRandomNumber(),
          DataHelper.generateValidDate().getYear(),
          DataHelper.generateValidHolder(),
          DataHelper.generateRequiredNumber(3, 3)
        );
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.checkFieldError(card, "month", "invalidFormat");
    }

    @Test
    @DisplayName("Should not input Month above 2 numbers")
    void shouldNotInputMonthAbove2numbers() {
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.checkAboveMaxInput("month", DataHelper.generateRequiredNumber(3, 99));
    }

    @Test
    @DisplayName("Should get error, Year less 2 numbers")
    void shouldGetErrorYearLess2Numbers() {
        var card = new DataHelper.CardInfo(
                approvedCard,
                DataHelper.generateCurrentDate().getMonth(),
                DataHelper.generateRandomNumber(),
                DataHelper.generateValidHolder(),
                DataHelper.generateRequiredNumber(3, 3)
        );
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.checkFieldError(card, "year", "invalidFormat");
    }

    @Test
    @DisplayName("Should not input Year above 2 numbers")
    void shouldNotInputYearAbove2numbers() {
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.checkAboveMaxInput("year", DataHelper.generateRequiredNumber(3, 99));
    }

    @Test
    @DisplayName("Should get error, Holder less min (1 symbol)")
    void shouldGetErrorHolderLessMin1Symbol() {
        var card = new DataHelper.CardInfo(
                approvedCard,
                DataHelper.generateValidDate().getMonth(),
                DataHelper.generateValidDate().getYear(),
                DataHelper.generateLatinString(1, 1),
                DataHelper.generateRequiredNumber(3, 3)
        );
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.checkFieldError(card, "holder", "invalidFormat");
    }

    @Test
    @DisplayName("Should not input Holder above max (20 symbols)")
    void shouldNotInputHolderAboveMaxSymbols() {
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.checkAboveMaxInput("holder", DataHelper.generateLatinString(31, 40));
    }

    @Test
    @DisplayName("Should get error, CVC less min (2 symbols)")
    void shouldGetErrorCvcLessMin2Symbols() {
        var card = new DataHelper.CardInfo(
                approvedCard,
                DataHelper.generateValidDate().getMonth(),
                DataHelper.generateValidDate().getYear(),
                DataHelper.generateValidHolder(),
                DataHelper.generateRequiredNumber(2, 2)
        );
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.checkFieldError(card, "cvc", "invalidFormat");
    }

    @Test
    @DisplayName("Should not input CVC above max (3 symbols)")
    void shouldNotInputCvcAboveMaxSymbols() {
        var startPage = new StartPage();
        var buyPage = startPage.clickBuyButton();
        buyPage.checkAboveMaxInput("cvc", DataHelper.generateRequiredNumber(4, 10));
    }
}
