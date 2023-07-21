package com.lukaszsuma.regexdatagenerator.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SpecialInputDataTest {

    @DisplayName("Testing correct parameters for NAME SpecialInputData")
    @ParameterizedTest(name = "startAt: {0}, female: {1}")
    @MethodSource("getCorrectArgumentsForNameAndSurnameSpecialInputData")
    void shouldGetRandomValueBasedOnParametersForNAME(String letter, String isFemale) {
        // given
        String parameter = String.format("NAME|startAt=%s,female=%s", letter, isFemale);
        // when
        Optional<String> optionalName = SpecialInputData.NAME.generateData().apply(parameter);
        // then
        assertTrue(optionalName.isPresent());
        String name = optionalName.get();
        boolean female = Boolean.parseBoolean(isFemale);
        if (!female && letter.equals("v")) {
            assertEquals("Jan", name);
        } else {
            assertTrue(name.startsWith(letter.toUpperCase()));
            if (female) {
                assertTrue(name.endsWith("a"));
            } else {
                assertFalse(name.endsWith("a"));
            }
        }
    }

    @DisplayName("Testing incorrect parameters for NAME SpecialInputData")
    @ParameterizedTest(name = "startAt: {0}, female: {1}")
    @MethodSource("getTwoIncorrectParameters")
    void shouldReturnEmptyOptionalOrDefaultValueForMaleBasedOnIncorrectParamsForNAME(String letter, String isFemale) {
        // given
        String parameter = String.format("NAME|startAt=%s,female=%s", letter, isFemale);
        // when
        Optional<String> optionalName = SpecialInputData.NAME.generateData().apply(parameter);
        // then
        assertTrue(optionalName.isEmpty() || optionalName.get().equals("Jan"));
    }

    @DisplayName("Testing incorrect startAt value but female always as true for NAME SpecialInputData")
    @ParameterizedTest(name = "startAt: {0}, female: {1}")
    @MethodSource("getIncorrectFirstAndCorrectSecondOneParametersForNameAndSurnameSpecialInputData")
    void shouldReturnEmptyOptionalOrDefaultValueForFemaleBasedOnIncorrectParamsForNAME(String letter, String isFemale) {
        // given
        String parameter = String.format("NAME|startAt=%s,female=%s", letter, isFemale);
        // when
        Optional<String> optionalName = SpecialInputData.NAME.generateData().apply(parameter);
        // then
        assertTrue(optionalName.isEmpty() || optionalName.get().equals("Janina"));
    }

    @DisplayName("Testing incorrect parameters and arguments for NAME SpecialInputData")
    @ParameterizedTest(name = "startAt: {0}, female: {1}, startAtVal: {2}, femaleVal: {3}")
    @MethodSource("getFourIncorrectParameters")
    void shouldNotThrowAnyExceptionIfAllArgsAndParamsAreSetWronglyForNameSpecialInputData(
            String startAt, String female, String letter, String isFemale) {
        // given
        String parameter = String.format("NAME|%s=%s,%s=%s", startAt, female, letter, isFemale);
        // then
        assertDoesNotThrow(() -> SpecialInputData.NAME.generateData().apply(parameter));
    }

    @DisplayName("Testing correct parameters for SURNAME SpecialInputData")
    @ParameterizedTest(name = "startAt: {0}, female: {1}")
    @MethodSource("getCorrectArgumentsForNameAndSurnameSpecialInputData")
    void shouldReturnRandomValueWhenParamsAreCorrectForSURNAME(String letter, String isFemale) {
        // given
        String parameter = String.format("SURNAME|startAt=%s,female=%s", letter, isFemale);
        // when
        Optional<String> optionalOfName = SpecialInputData.SURNAME.generateData().apply(parameter);
        // then
        assertTrue(optionalOfName.isPresent());
        String surname = optionalOfName.get();
        if (letter.equals("v")) {
            if (Boolean.parseBoolean(isFemale)) {
                assertEquals("Kowalska", surname);
            } else {
                assertEquals("Kowalski", surname);
            }
        } else {
            assertTrue(surname.startsWith(letter.toUpperCase()));
        }
    }

    @DisplayName("Testing incorrect startAt value but female always as true for SURNAME SpecialInputData")
    @ParameterizedTest(name = "startAt: {0}, female: {1}")
    @MethodSource("getIncorrectFirstAndCorrectSecondOneParametersForNameAndSurnameSpecialInputData")
    void shouldReturnEmptyOptionalOrDefaultValueForFemaleWhenWrongParamsArePassedForSURNAME(String letter, String isFemale) {
        // given
        String parameter = String.format("SURNAME|startAt=%s,female=%s", letter, isFemale);
        // when
        Optional<String> optionalName = SpecialInputData.SURNAME.generateData().apply(parameter);
        // then
        assertTrue(optionalName.isEmpty() || optionalName.get().equals("Kowalska"));
    }

    @DisplayName("Testing incorrect data for SURNAME SpecialInputData")
    @ParameterizedTest(name = "startAt: {0}, female: {1}")
    @MethodSource("getTwoIncorrectParameters")
    void shouldReturnEmptyOptionalOrDefaultValueForMaleWhenWrongParamsArePassedForSURNAME(String letter, String isFemale) {
        // given
        String parameter = String.format("SURNAME|startAt=%s,female=%s", letter, isFemale);
        // when
        Optional<String> optionalName = SpecialInputData.SURNAME.generateData().apply(parameter);
        // then
        assertTrue(optionalName.isEmpty() || optionalName.get().equals("Kowalski"));
    }

    @DisplayName("Testing incorrect parameters and arguments for SURNAME SpecialInputData")
    @ParameterizedTest(name = "startAt: {0}, female: {1}, startAtVal: {2}, femaleVal: {3}")
    @MethodSource("getFourIncorrectParameters")
    void shouldNotThrowAnyExceptionWhenAllArgsAndParamsAreSetWronglyForSURNAME(
            String startAt, String female, String letter, String isFemale) {
        // given
        String parameter = String.format("SURNAME|%s=%s,%s=%s", startAt, female, letter, isFemale);
        // then
        assertDoesNotThrow(() -> SpecialInputData.SURNAME.generateData().apply(parameter));
    }

    @DisplayName("Testing correct parameters for PESEL SpecialInputData")
    @ParameterizedTest(name = "canBeAsBornAfter2000: {0}, isFemale: {1}, onlyAdults: {2}")
    @MethodSource("getThreeParametersTrueAndFalse")
    void shouldReturnPeselBasedOnParametersForPESEL(String canBeAsBornAfter2000, String isFemale, String onlyAdults) {
        // given
        String parameter = String.format("PESEL|canBeAsBornAfter2000=%s,isFemale=%s,onlyAdults=%s",
                canBeAsBornAfter2000, isFemale, onlyAdults);
        // then
        Optional<String> shouldBePesel = assertDoesNotThrow(() ->
                SpecialInputData.PESEL.generateData().apply(parameter));
        assertTrue(shouldBePesel.isPresent());
    }

    @DisplayName("Testing incorrect parameters for PESEL SpecialInputData")
    @ParameterizedTest(name = "canBeAsBornAfter2000: {0}, isFemale: {1}, onlyAdults: {2}")
    @MethodSource("getThreeIncorrectParameters")
    void shouldNotThrowAnyErrorWhenWrongParametersArePassedForPesel(
            String canBeAsBornAfter2000, String isFemale, String onlyAdults) {
        // given
        String parameter = String.format("PESEL|canBeAsBornAfter2000=%s,isFemale=%s,onlyAdults=%s",
                canBeAsBornAfter2000, isFemale, onlyAdults);
        // then
        assertDoesNotThrow(() -> SpecialInputData.PESEL.generateData().apply(parameter));
    }

    @DisplayName("Testing incorrect parameters and arguments for PESEL SpecialInputData")
    @ParameterizedTest(name = "canBeAsBornAfter2000: {0}, canBeAsBornAfter2000Val: {3} isFemale: {1}, " +
            "isFemaleVal: {4} onlyAdults: {2}, onlyAdultsVal: {5}")
    @MethodSource("getSixIncorrectParameters")
    void shouldNotThrowAnyExceptionWhenWrongArgumentsAndParametersArePassedForPESEL(
            String canBeAsBornAfter2000, String isFemale, String onlyAdults,
            String canBeAsBornAfter2000Val, String isFemaleVal, String onlyAdultsVal) {
        // given
        String parameter = String.format("PESEL|%s=%s,%s=%s,%s=%s",
                canBeAsBornAfter2000, canBeAsBornAfter2000Val, isFemale, isFemaleVal, onlyAdults, onlyAdultsVal);
        // then
        assertDoesNotThrow(() -> SpecialInputData.PESEL.generateData().apply(parameter));
    }

    @DisplayName("Testing correct parameters for IBAN SpecialInputData")
    @ParameterizedTest(name = "country: {0}, bankName: {1} formatted: {2}, withLetters: {3}")
    @MethodSource("getCorrectParametersForIBANSpecialInputData")
    void shouldReturnCorrectIBANWhenCorrectParametersArePassedForIBAN(
            AvailableCountries country, PolandBankId bankName, boolean formatted, boolean withLetters) {
        // given
        String parameter = String.format("IBAN|country=%s,bankName=%s,formatted=%s,withLetters=%s",
                country, bankName, formatted, withLetters);
        // then
        Optional<String> shouldBeIBAN = SpecialInputData.IBAN.generateData().apply(parameter);
        assertTrue(shouldBeIBAN.isPresent());
        String iban = shouldBeIBAN.get();
        int ibanControlNumberFromGeneratedIBAN;
        if (formatted) {
            if (withLetters) {
                ibanControlNumberFromGeneratedIBAN = Integer.parseInt(iban.substring(2, 4));
                iban = iban.substring(4).replace(StringSeparator.EMPTY_SPACE, StringSeparator.EMPTY_STRING);
            } else {
                ibanControlNumberFromGeneratedIBAN = Integer.parseInt(iban.substring(0, 2));
                iban = iban.substring(2).replace(StringSeparator.EMPTY_SPACE, StringSeparator.EMPTY_STRING);
            }
        } else if (withLetters) {
            ibanControlNumberFromGeneratedIBAN = Integer.parseInt(iban.substring(2, 4));
            iban = iban.substring(4);
        } else {
            ibanControlNumberFromGeneratedIBAN = Integer.parseInt(iban.substring(0, 2));
            iban = iban.substring(2);
        }
        StringBuilder sb = new StringBuilder(iban);
        String numberForCountry = CountryLettersToNumber.convertCountryLettersToNumber(country.name());
        int ibanControlNumber = IBANValidator.getIbanControlNumber(sb, numberForCountry);
        assertEquals(ibanControlNumber, ibanControlNumberFromGeneratedIBAN);
    }

    @DisplayName("Testing incorrect parameters for IBAN SpecialInputData")
    @ParameterizedTest(name = "country: {0}, bankName: {1} formatted: {2}, withLetters: {3}")
    @MethodSource("getFourIncorrectParameters")
    void shouldReturnEmptyOptionalOrIBANWithDefaultCountryAndWithNoLettersAndNoFormattingWhenWrongParamsArePassedForIBAN(
            String country, String bankName, String formatted, String withLetters) {
        // given
        String parameter = String.format("IBAN|country=%s,bankName=%s,formatted=%s,withLetters=%s",
                country, bankName, formatted, withLetters);
        // then
        Optional<String> shouldBeIBAN = SpecialInputData.IBAN.generateData().apply(parameter);
        if(shouldBeIBAN.isPresent()) {
            String iban = shouldBeIBAN.get();
            assertDoesNotThrow(() -> {
                String sub1 = iban.substring(0, iban.length() / 2);
                String sub2 = iban.substring(iban.length() / 2);
                Long.parseLong(sub1);
                Long.parseLong(sub2);
            });
            int ibanControlNumberFromGenerateIban = Integer.parseInt(iban.substring(0, 2));
            String number = CountryLettersToNumber.convertCountryLettersToNumber("PL");
            int ibanControlNumber = IBANValidator.getIbanControlNumber(new StringBuilder(iban.substring(2)), number);
            assertEquals(ibanControlNumber, ibanControlNumberFromGenerateIban);
        }
    }

    @DisplayName("Testing incorrect parameters and arguments for IBAN SpecialInputData")
    @ParameterizedTest(name = "country: {0}, bankName: {1} formatted: {2}, withLetters: {3}")
    @MethodSource("getEightIncorrectParameters")
    void shouldReturnEmptyOptionalOrIBANWithDefaultCountryAndWithNoLettersAndNoFormattingWhenWrongArgsAndParamsArePassedForIBAN(
            String country, String countryVal, String bankName, String bankNameVal, String formatted,
            String formattedVal, String withLetters, String withLettersVal) {
        // given
        String parameter = String.format("IBAN|%s=%s,%s=%s,%s=%s,%s=%s",
                country, countryVal, bankName, bankNameVal, formatted, formattedVal, withLetters, withLettersVal);
        // then
        Optional<String> shouldBeIBAN = SpecialInputData.IBAN.generateData().apply(parameter);
        if(shouldBeIBAN.isPresent()) {
            String iban = shouldBeIBAN.get();
            assertDoesNotThrow(() -> {
                String sub1 = iban.substring(0, iban.length() / 2);
                String sub2 = iban.substring(iban.length() / 2);
                Long.parseLong(sub1);
                Long.parseLong(sub2);
            });
            int ibanControlNumberFromGenerateIban = Integer.parseInt(iban.substring(0, 2));
            String number = CountryLettersToNumber.convertCountryLettersToNumber("PL");
            int ibanControlNumber = IBANValidator.getIbanControlNumber(new StringBuilder(iban.substring(2)), number);
            assertEquals(ibanControlNumber, ibanControlNumberFromGenerateIban);
        }
    }

    private static Stream<Arguments> getCorrectParametersForIBANSpecialInputData() {
        int availableCountryLength = AvailableCountries.values().length;
        int polandBankIdLength = PolandBankId.values().length;
        int numberOfAllCombinations = availableCountryLength * polandBankIdLength * 4;
        Stream<Arguments> stream = Stream.empty();
        for (int i = 0; i < numberOfAllCombinations; i++) {
            int availableCountryIndex = availableCountryLength * i / numberOfAllCombinations;
            int polandBankIdIndex = polandBankIdLength * i / numberOfAllCombinations;
            int modulo = i % 4;
            switch (modulo) {
                case 0 -> {
                    stream = Stream.concat(stream, Stream.of(
                            Arguments.of(
                                    AvailableCountries.values()[availableCountryIndex],
                                    PolandBankId.values()[polandBankIdIndex], true, true)));
                }
                case 1 -> {
                    stream = Stream.concat(stream, Stream.of(
                            Arguments.of(
                                    AvailableCountries.values()[availableCountryIndex],
                                    PolandBankId.values()[polandBankIdIndex], true, false)));
                }
                case 2 -> {
                    stream = Stream.concat(stream, Stream.of(
                            Arguments.of(
                                    AvailableCountries.values()[availableCountryIndex],
                                    PolandBankId.values()[polandBankIdIndex], false, true)));
                }
                case 3 -> {
                    stream = Stream.concat(stream, Stream.of(
                            Arguments.of(
                                    AvailableCountries.values()[availableCountryIndex],
                                    PolandBankId.values()[polandBankIdIndex], false, false)));
                }
            }
        }
        return stream;
    }

    private static Stream<Arguments> getSixIncorrectParameters() {
        return getThreeIncorrectParameters()
                .map(arg -> {
                    Object[] params = arg.get();
                    Object[] newParams = new Object[params.length * 2];
                    for (int i = 0; i < newParams.length; i++) {
                        newParams[i] = params[i / 2];
                    }
                    return Arguments.of(newParams);
                });
    }

    private static Stream<Arguments> getCorrectArgumentsForNameAndSurnameSpecialInputData() {
        int allCorrectPossibilities = ('z' - 'a' - 2) * 2;
        Arguments[] arguments = new Arguments[allCorrectPossibilities];
        String[] allLetters = generateArrayOfLetters();
        for (int i = 0; i < allCorrectPossibilities; i++) {
            arguments[i] = Arguments.of(allLetters[i / 2], String.valueOf(i % 2 != 0));
        }
        return Stream.of(arguments);
    }

    private static Stream<Arguments> getTwoIncorrectParameters() {
        return Stream.of(
                Arguments.of("abcd", "falsy"),
                Arguments.of("ABCD", "truthy"),
                Arguments.of("null", "null"),
                Arguments.of(null, null),
                Arguments.of("-1", "1"),
                Arguments.of("-", "-"),
                Arguments.of("--", "--"),
                Arguments.of(StringSeparator.PIPE, StringSeparator.PIPE),
                Arguments.of("||", "||"),
                Arguments.of(StringSeparator.COMMA, StringSeparator.COMMA),
                Arguments.of(",,", ",,"),
                Arguments.of(StringSeparator.EMPTY_STRING, StringSeparator.EMPTY_STRING),
                Arguments.of(StringSeparator.EMPTY_SPACE, StringSeparator.EMPTY_SPACE)
        );
    }

    private static Stream<Arguments> getIncorrectFirstAndCorrectSecondOneParametersForNameAndSurnameSpecialInputData() {
        return Stream.of(
                Arguments.of("abcd", "true"),
                Arguments.of("ABCD", "true"),
                Arguments.of("null", "true"),
                Arguments.of(null, "true"),
                Arguments.of("-1", "true"),
                Arguments.of("-", "true"),
                Arguments.of("--", "true"),
                Arguments.of(StringSeparator.PIPE, "true"),
                Arguments.of("||", "true"),
                Arguments.of(StringSeparator.COMMA, "true"),
                Arguments.of(",,", "true"),
                Arguments.of(StringSeparator.EMPTY_STRING, "true"),
                Arguments.of(StringSeparator.EMPTY_SPACE, "true")
        );
    }

    private static Stream<Arguments> getFourIncorrectParameters() {
        return Stream.of(
                Arguments.of("abcd", "falsy", StringSeparator.EMPTY_SPACE, StringSeparator.EMPTY_SPACE),
                Arguments.of("ABCD", "truthy", StringSeparator.EMPTY_STRING, StringSeparator.EMPTY_STRING),
                Arguments.of("null", "null", "null", "null"),
                Arguments.of(null, null, null, null),
                Arguments.of("-1", "1", StringSeparator.PIPE, StringSeparator.PIPE),
                Arguments.of("-", "-", ",,", ",,"),
                Arguments.of("--", "--", "||", "||"),
                Arguments.of(StringSeparator.PIPE, StringSeparator.PIPE, "{", "}"),
                Arguments.of("||", "||", "ABCD", "truthy"),
                Arguments.of(StringSeparator.COMMA, StringSeparator.COMMA, StringSeparator.COMMA, StringSeparator.COMMA),
                Arguments.of(",,", ",,", "-", "-"),
                Arguments.of(StringSeparator.EMPTY_STRING, StringSeparator.EMPTY_STRING, "--", "--"),
                Arguments.of(StringSeparator.EMPTY_SPACE, StringSeparator.EMPTY_SPACE, "01", "-1")
        );
    }

    private static Stream<Arguments> getEightIncorrectParameters() {
        return getFourIncorrectParameters()
                .map(arg -> {
                    Object[] params = arg.get();
                    Object[] newParams = new Object[params.length * 2];
                    for (int i = 0; i < newParams.length; i++) {
                        newParams[i] = params[i / 2];
                    }
                    return Arguments.of(newParams);
                });
    }

    private static Stream<Arguments> getThreeParametersTrueAndFalse() {
        String trueVal = "true";
        String falseVal = "false";
        return Stream.of(
                Arguments.of(trueVal, trueVal, falseVal),
                Arguments.of(trueVal, trueVal, falseVal),
                Arguments.of(trueVal, falseVal, trueVal),
                Arguments.of(falseVal, trueVal, trueVal),
                Arguments.of(trueVal, falseVal, falseVal),
                Arguments.of(falseVal, falseVal, trueVal),
                Arguments.of(falseVal, falseVal, falseVal)
        );
    }

    private static Stream<Arguments> getThreeIncorrectParameters() {
        return Stream.of(
                Arguments.of("abcd", "falsy", StringSeparator.EMPTY_SPACE),
                Arguments.of("ABCD", "truthy", StringSeparator.EMPTY_STRING),
                Arguments.of("null", "null", "null"),
                Arguments.of(null, null, null),
                Arguments.of("-1", "1", StringSeparator.PIPE),
                Arguments.of("-", "-", ",,"),
                Arguments.of("--", "--", "||"),
                Arguments.of(StringSeparator.PIPE, StringSeparator.PIPE, "{"),
                Arguments.of("||", "||", "ABCD", "truthy"),
                Arguments.of(StringSeparator.COMMA, StringSeparator.COMMA, StringSeparator.COMMA),
                Arguments.of(",,", ",,", "-"),
                Arguments.of(StringSeparator.EMPTY_STRING, StringSeparator.EMPTY_STRING, "--"),
                Arguments.of(StringSeparator.EMPTY_SPACE, StringSeparator.EMPTY_SPACE, "01")
        );
    }

    private static String[] generateArrayOfLetters() {
        int amount = 'z' - 'a' + 1;
        char[] arr = new char[amount];
        for (int i = 0; i < amount; i++) {
            arr[i] = (char) ('a' + i);
        }
        return IntStream.range(0, arr.length)
                .mapToObj(i -> arr[i])
                .map(String::valueOf)
                .filter(str -> !str.equals("x") && !str.equals("y") && !str.equals("q"))
                .toArray(String[]::new);
    }

}

