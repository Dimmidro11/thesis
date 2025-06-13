package ru.netology.shop.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataHelper {

    private static final Faker faker = new Faker();
    private DataHelper() {}

    @Value
    public static class CardInfo {
        private String number;
        private String month;
        private String year;
        private String holder;
        private String cvc;
    }

    @Value
    public static class Date {
        private String month;
        private String year;
    }

    public static CardInfo generateCard(String cardNumber) {
        return new CardInfo(
                cardNumber,
                generateValidDate().getMonth(),
                generateValidDate().getYear(),
                generateValidHolder(),
                generateRequiredNumber(3, 3)
        );
    }

    public static String generateRandomCardNumber() {
        return faker.business().creditCardNumber();
    }

    public static String generateRequiredNumber(int minQuantityNumber, int maxQuantityNumber) {
        return faker.number().digits(
                faker.number().numberBetween(minQuantityNumber, maxQuantityNumber)
        );
    }

    public static String generateRandomNumber2Symbol(int minCount, int maxCount) {
        return String.format("%02d", faker.number().numberBetween(minCount, maxCount));
    }

    public static String generateRandomNumber() {
        return String.valueOf(faker.number().numberBetween(0, 9));
    }

    public static Date generateCurrentDate() {
        return new Date(
                LocalDate.now().format(DateTimeFormatter.ofPattern("MM")),
                LocalDate.now().format(DateTimeFormatter.ofPattern("yy"))
        );
    }

    public static String generateYearAboveCurrentByTerm(int diff, boolean future) {
        int term = future ? diff : - diff;
        return LocalDate.now().plusYears(term).
                format(DateTimeFormatter.ofPattern("yy"));
    }

    public static Date generateValidDate() {
        LocalDate validDate = LocalDate.now().
                plusMonths(faker.number().numberBetween(1, 60));
        return new Date(
                validDate.format(DateTimeFormatter.ofPattern("MM")),
                validDate.format(DateTimeFormatter.ofPattern("yy"))
                );
    }

    public static String generateMonthLessCurrent() {
        LocalDate today = LocalDate.now();
        int range =  faker.number().numberBetween(1, today.getMonthValue());
        return LocalDate.now().minusMonths(range).
                format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String generateValidHolder() {
        return faker.name().fullName().toUpperCase();
    }

    public static String generateHolder2Symbol() {
        Faker faker = new Faker(new Locale("en"));
        return faker.letterify("? ?").toUpperCase();
    }

    public static String generateCyrillicString() {
        String cyrillicAlphabet = "ЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮйцукенгшщзхфывапролджэячсмитьбю";
        Random random = new Random();

        int length = faker.number().numberBetween(1, 11);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(cyrillicAlphabet.
                    charAt(random.nextInt(cyrillicAlphabet.length())));
        }
        return sb.toString();
    }

    public static String generateLatinString(int minQuantity, int maxQuantity) {
        String latinAlphabet = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm";
        Random random = new Random();

        int length = faker.number().numberBetween(minQuantity, maxQuantity);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(latinAlphabet.
                    charAt(random.nextInt(latinAlphabet.length())));
        }
        return sb.toString();
    }

    public static String generateSpecialSymbols() {
        String specialSymbolsAlphabet = "!@#$%^&*()_+=-?><,.|';:`\"#\\/№~][{}";
        Random random = new Random();

        int length = faker.number().numberBetween(1, 11);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(specialSymbolsAlphabet.
                    charAt(random.nextInt(specialSymbolsAlphabet.length())));
        }
        return sb.toString();
    }
}
