package com.lukaszsuma.regexdatagenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lukaszsuma.regexdatagenerator.utils.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SpecialInputDataTest {

    @DisplayName("Testing returning empty optional for every SpecialInputDate beyond ID if = or empty String is passed")
    @ParameterizedTest(name = "specialInputData: {0}")
    @EnumSource(value = SpecialInputData.class, names = "ID", mode = EnumSource.Mode.EXCLUDE)
    void shouldAlwaysReturnEmptyOptionalWhenEqualsInParamsOrOnlyNameWithPipeIsPassed(SpecialInputData specialInputData) {
        // when
        Optional<String> shouldBeEmpty = specialInputData.generateData().apply(
                specialInputData + StringSeparator.PIPE + StringSeparator.EQUALS);
        Optional<String> shouldAlsoBeEmpty = specialInputData.generateData().apply(
                specialInputData + StringSeparator.PIPE + StringSeparator.EMPTY_STRING);
        // then
        assertTrue(shouldBeEmpty.isEmpty());
        assertTrue(shouldAlsoBeEmpty.isEmpty());
    }

    @DisplayName("Testing correct parameters for NAME SpecialInputData")
    @ParameterizedTest(name = "startAt: {0}, female: {1}")
    @MethodSource("getCorrectArgumentsForNameAndSurnameSpecialInputData")
    void shouldReturnRandomNameBasedOnCorrectParametersForNAME(String letter, String isFemale) {
        // given
        String parameter = String.format("NAME|startAt=%s,female=%s", letter, isFemale);
        // when
        Optional<String> shouldBeName = SpecialInputData.NAME.generateData().apply(parameter);
        // then
        assertTrue(shouldBeName.isPresent());
        String name = shouldBeName.get();
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

    @DisplayName("Testing NAME SpecialInputData without any parameter")
    @Test
    void shouldReturnRandomNameWhenArgumentIsWithoutPipeForNAME() {
        // given
        String parameter = "NAME";
        // when
        Optional<String> shouldBeName = SpecialInputData.NAME.generateData().apply(parameter);
        // then
        assertTrue(shouldBeName.isPresent());
        String name = shouldBeName.get();
        assertFalse(name.isBlank());
    }

    @DisplayName("Testing incorrect startAt value but female always as false for NAME SpecialInputData")
    @ParameterizedTest(name = "startAt: {0}, female: {1}")
    @MethodSource("getTwoIncorrectParameters")
    void shouldReturnEmptyOptionalOrDefaultNameForMaleBasedOnIncorrectParamsForNAME(String letter, String isFemale) {
        // given
        String parameter = String.format("NAME|startAt=%s,female=%s", letter, isFemale);
        // when
        Optional<String> shouldBeEmptyOrWithJan = SpecialInputData.NAME.generateData().apply(parameter);
        // then
        assertTrue(shouldBeEmptyOrWithJan.isEmpty() || shouldBeEmptyOrWithJan.get().equals("Jan"));
    }

    @DisplayName("Testing incorrect startAt value but female always as true for NAME SpecialInputData")
    @ParameterizedTest(name = "startAt: {0}, female: {1}")
    @MethodSource("getIncorrectFirstAndCorrectSecondOneParametersForNameAndSurnameSpecialInputData")
    void shouldReturnEmptyOptionalOrDefaultNameForFemaleBasedOnIncorrectParamsForNAME(String letter, String isFemale) {
        // given
        String parameter = String.format("NAME|startAt=%s,female=%s", letter, isFemale);
        // when
        Optional<String> shouldBeEmptyOrWithJanina = SpecialInputData.NAME.generateData().apply(parameter);
        // then
        assertTrue(shouldBeEmptyOrWithJanina.isEmpty() || shouldBeEmptyOrWithJanina.get().equals("Janina"));
    }

    @DisplayName("Testing incorrect parameters and arguments for NAME SpecialInputData")
    @ParameterizedTest(name = "startAt: {0}, female: {1}, startAtVal: {2}, femaleVal: {3}")
    @MethodSource("getFourIncorrectParameters")
    void shouldNotThrowAnyExceptionWhenAllArgsAndParamsAreSetWronglyForNameSpecialInputData(
            String startAt, String female, String letter, String isFemale) {
        // given
        String parameter = String.format("NAME|%s=%s,%s=%s", startAt, female, letter, isFemale);
        // then
        assertDoesNotThrow(() -> SpecialInputData.NAME.generateData().apply(parameter));
    }

    @DisplayName("Testing correct parameters for SURNAME SpecialInputData")
    @ParameterizedTest(name = "startAt: {0}, female: {1}")
    @MethodSource("getCorrectArgumentsForNameAndSurnameSpecialInputData")
    void shouldReturnRandomSurnameWhenParamsAreCorrectForSURNAME(String letter, String isFemale) {
        // given
        String parameter = String.format("SURNAME|startAt=%s,female=%s", letter, isFemale);
        // when
        Optional<String> shouldBeSurname = SpecialInputData.SURNAME.generateData().apply(parameter);
        // then
        assertTrue(shouldBeSurname.isPresent());
        String surname = shouldBeSurname.get();
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

    @DisplayName("Testing SURNAME SpecialInputData without any parameter")
    @Test
    void shouldReturnRandomSurnameWhenArgumentIsWithoutPipeForSURNAME() {
        // given
        String parameter = "SURNAME";
        // when
        Optional<String> shouldBeSurname = SpecialInputData.SURNAME.generateData().apply(parameter);
        // then
        assertTrue(shouldBeSurname.isPresent());
        String surname = shouldBeSurname.get();
        assertFalse(surname.isBlank());
    }

    @DisplayName("Testing incorrect startAt value but female always as true for SURNAME SpecialInputData")
    @ParameterizedTest(name = "startAt: {0}, female: {1}")
    @MethodSource("getIncorrectFirstAndCorrectSecondOneParametersForNameAndSurnameSpecialInputData")
    void shouldReturnEmptyOptionalOrDefaultSurnameForFemaleWhenWrongParamsArePassedForSURNAME(
            String letter, String isFemale) {
        // given
        String parameter = String.format("SURNAME|startAt=%s,female=%s", letter, isFemale);
        // when
        Optional<String> shouldBeEmptyOrWithKowalska = SpecialInputData.SURNAME.generateData().apply(parameter);
        // then
        assertTrue(shouldBeEmptyOrWithKowalska.isEmpty() || shouldBeEmptyOrWithKowalska.get().equals("Kowalska"));
    }

    @DisplayName("Testing incorrect data for SURNAME SpecialInputData")
    @ParameterizedTest(name = "startAt: {0}, female: {1}")
    @MethodSource("getTwoIncorrectParameters")
    void shouldReturnEmptyOptionalOrDefaultSurnameForMaleWhenWrongParamsArePassedForSURNAME(
            String letter, String isFemale) {
        // given
        String parameter = String.format("SURNAME|startAt=%s,female=%s", letter, isFemale);
        // when
        Optional<String> shouldBeEmptyOrWithKowalski = SpecialInputData.SURNAME.generateData().apply(parameter);
        // then
        assertTrue(shouldBeEmptyOrWithKowalski.isEmpty() || shouldBeEmptyOrWithKowalski.get().equals("Kowalski"));
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

    @DisplayName("Testing PESEL SpecialInputData without any parameter")
    @Test
    void shouldReturnRandomPeselForMaleAndPersonBornBefore2000WhenArgumentIsWithoutPipeForPESEL() {
        // given
        String parameter = "PESEL";
        // when
        Optional<String> shouldBePesel = SpecialInputData.PESEL.generateData().apply(parameter);
        // then
        assertTrue(shouldBePesel.isPresent());
        String pesel = shouldBePesel.get();
        assertFalse(pesel.isBlank());
        String shouldBeMale = pesel.substring(pesel.length() - 2, pesel.length() - 1);
        assertEquals(1, Integer.parseInt(shouldBeMale) % 2);
        String shouldBeLowerThan2 = pesel.substring(2, 3);
        assertTrue(Integer.parseInt(shouldBeLowerThan2) < 2);
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
        // when
        Optional<String> shouldBeIBAN = SpecialInputData.IBAN.generateData().apply(parameter);
        // then
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
        int ibanControlNumber = IBANValidator.getIbanControlNumber(sb, numberForCountry, false, false);
        assertEquals(ibanControlNumber, ibanControlNumberFromGeneratedIBAN);
    }

    @DisplayName("Testing IBAN SpecialInputData without any parameter")
    @Test
    void shouldReturnIbanForPolandCountryAndINGBankNameWhenArgumentIsWithoutPipeForIBAN() {
        // given
        String parameter = "IBAN";
        // when
        Optional<String> optionalIban = SpecialInputData.IBAN.generateData().apply(parameter);
        // then
        assertTrue(optionalIban.isPresent());
        String iban = optionalIban.get();
        assertDoesNotThrow(() -> {
            String sub1 = iban.substring(0, iban.length() / 2);
            String sub2 = iban.substring(iban.length() / 2);
            Long.parseLong(sub1);
            Long.parseLong(sub2);
        });
        int ibanControlNumberFromGenerateIban = Integer.parseInt(iban.substring(0, 2));
        String number = CountryLettersToNumber.convertCountryLettersToNumber("PL");
        StringBuilder ibanVal = new StringBuilder(iban.substring(2));
        int ibanControlNumber = IBANValidator.getIbanControlNumber(ibanVal, number, false, false);
        assertEquals(ibanControlNumber, ibanControlNumberFromGenerateIban);
    }

    @DisplayName("Testing incorrect parameters for IBAN SpecialInputData")
    @ParameterizedTest(name = "country: {0}, bankName: {1} formatted: {2}, withLetters: {3}")
    @MethodSource("getFourIncorrectParameters")
    void shouldReturnEmptyOptionalOrIBANWithDefaultCountryAndWithNoLettersAndNoFormattingWhenWrongParamsArePassedForIBAN(
            String country, String bankName, String formatted, String withLetters) {
        // given
        String parameter = String.format("IBAN|country=%s,bankName=%s,formatted=%s,withLetters=%s",
                country, bankName, formatted, withLetters);
        // when
        Optional<String> shouldBeIBAN = SpecialInputData.IBAN.generateData().apply(parameter);
        // then
        if (shouldBeIBAN.isPresent()) {
            String iban = shouldBeIBAN.get();
            assertDoesNotThrow(() -> {
                String sub1 = iban.substring(0, iban.length() / 2);
                String sub2 = iban.substring(iban.length() / 2);
                Long.parseLong(sub1);
                Long.parseLong(sub2);
            });
            int ibanControlNumberFromGenerateIban = Integer.parseInt(iban.substring(0, 2));
            String number = CountryLettersToNumber.convertCountryLettersToNumber("PL");
            StringBuilder ibanVal = new StringBuilder(iban.substring(2));
            int ibanControlNumber = IBANValidator.getIbanControlNumber(ibanVal, number, false, false);
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
        // when
        Optional<String> shouldBeIBAN = SpecialInputData.IBAN.generateData().apply(parameter);
        // then
        if (shouldBeIBAN.isPresent()) {
            String iban = shouldBeIBAN.get();
            assertDoesNotThrow(() -> {
                String sub1 = iban.substring(0, iban.length() / 2);
                String sub2 = iban.substring(iban.length() / 2);
                Long.parseLong(sub1);
                Long.parseLong(sub2);
            });
            int ibanControlNumberFromGenerateIban = Integer.parseInt(iban.substring(0, 2));
            String number = CountryLettersToNumber.convertCountryLettersToNumber("PL");
            StringBuilder ibanVal = new StringBuilder(iban.substring(2));
            int ibanControlNumber = IBANValidator.getIbanControlNumber(ibanVal, number, false, false);
            assertEquals(ibanControlNumber, ibanControlNumberFromGenerateIban);
        }
    }

    @DisplayName("Testing of creating random post code for POSTCODE SpecialInputData")
    @RepeatedTest(value = 20, name = "execution {currentRepetition}/{totalRepetitions}")
    void shouldReturnRandomPostCodeWithSpecificFormatForPOSTCODE() {
        // given
        Pattern pattern = Pattern.compile("\\d{2}-\\d{3}");
        String parameter = "POSTCODE";
        // when
        Optional<String> shouldBePostcode = SpecialInputData.POSTCODE.generateData().apply(parameter);
        // then
        assertTrue(shouldBePostcode.isPresent());
        String postcode = shouldBePostcode.get();
        assertTrue(pattern.matcher(postcode).matches());
    }

    @DisplayName("Testing incorrect parameter without pipe for POSTCODE SpecialInputData")
    @ParameterizedTest(name = "redundant parameter value: {0}")
    @MethodSource("getOneIncorrectParameter")
    void shouldReturnRandomPostcodeWithSpecificFormatWhenRedundantParametersIsPassedAndIsWithoutPipeForPOSTCODE(
            String redundantValue) {
        // given
        Pattern pattern = Pattern.compile("\\d{2}-\\d{3}");
        String incorrectParameter = "POSTCODE" + redundantValue;
        // when
        Optional<String> optionalPostcode = SpecialInputData.POSTCODE.generateData().apply(incorrectParameter);
        // then
        if (optionalPostcode.isPresent()) {
            String postcode = optionalPostcode.get();
            assertTrue(pattern.matcher(postcode).matches());
        } else {
            assertTrue(redundantValue.contains(StringSeparator.PIPE_REGEX) ||
                    redundantValue.contains(StringSeparator.PIPE));
        }
    }

    @DisplayName("Testing incorrect parameter with pipe for POSTCODE SpecialInputData")
    @ParameterizedTest(name = "redundant parameter value: {0}")
    @MethodSource("getOneIncorrectParameter")
    void shouldNotReturnAnyPostcodeWithSpecificFormatOrEmptyOptionalWhenRedundantParametersIsPassedAndIsWithPipeForPOSTCODE(
            String redundantValue) {
        // given
        String incorrectParameter = "POSTCODE|" + redundantValue;
        // when
        Optional<String> shouldBeEmpty = SpecialInputData.POSTCODE.generateData().apply(incorrectParameter);
        // then
        assertTrue(shouldBeEmpty.isEmpty());
    }

    @DisplayName("Testing of creating random street for STREET SpecialInputData")
    @Test
    void shouldReturnRandomStreetForSTREET() {
        // given
        String parameter = "STREET";
        // when
        Optional<String> shouldBeStreet = SpecialInputData.STREET.generateData().apply(parameter);
        // then
        assertTrue(shouldBeStreet.isPresent());
        String street = shouldBeStreet.get();
        assertFalse(street.isBlank());
    }

    @DisplayName("Testing incorrect parameter and without pipe for STREET SpecialInputData")
    @ParameterizedTest(name = "redundant parameter value: {0}")
    @MethodSource("getOneIncorrectParameter")
    void shouldReturnRandomStreetOrEmptyOptionalWhenRedundantParametersIsPassedAndIsWithoutPipeForSTREET(String redundantValue) {
        // given
        String incorrectParameter = "STREET" + redundantValue;
        // then
        assertDoesNotThrow(() -> SpecialInputData.STREET.generateData().apply(incorrectParameter));
    }

    @DisplayName("Testing incorrect parameter and with pipe for STREET SpecialInputData")
    @ParameterizedTest(name = "redundant parameter value: {0}")
    @MethodSource("getOneIncorrectParameter")
    void shouldReturnEmptyOptionalWhenRedundantParametersIsPassedAndIsWithPipeForSTREET(String redundantValue) {
        // given
        String incorrectParameter = "STREET|" + redundantValue;
        // when
        Optional<String> shouldBeEmpty = SpecialInputData.STREET.generateData().apply(incorrectParameter);
        // then
        assertTrue(shouldBeEmpty.isEmpty());
    }

    @DisplayName("Testing correct parameters for CITY SpecialInputData")
    @ParameterizedTest(name = "startAt: {0}")
    @MethodSource("getArgumentsWithLettersOnly")
    void shouldGetRandomCityBasedOnParameterForCITY(String letter) {
        // given
        String parameter = String.format("CITY|startAt=%s", letter);
        // when
        Optional<String> optionalCity = SpecialInputData.CITY.generateData().apply(parameter);
        // then
        assertTrue(optionalCity.isPresent());
        String city = optionalCity.get();
        if (letter.equals("v")) {
            assertEquals("Warszawa", city);
        } else {
            String firstCityLetter = city.substring(0, 1);
            assertEquals(letter.toUpperCase(), firstCityLetter);
        }
    }

    @DisplayName("Testing of creating random city for CITY SpecialInputData")
    @Test
    void shouldReturnRandomCityForCITY() {
        // given
        String parameter = "CITY";
        // when
        Optional<String> shouldBeCity = SpecialInputData.CITY.generateData().apply(parameter);
        // then
        assertTrue(shouldBeCity.isPresent());
        String city = shouldBeCity.get();
        assertFalse(city.isBlank());
    }

    @DisplayName("Testing incorrect parameter and without pipe for CITY SpecialInputData")
    @ParameterizedTest(name = "redundant parameter value: {0}")
    @MethodSource("getOneIncorrectParameter")
    void shouldReturnRandomCityOrEmptyOptionalWhenRedundantParametersIsPassedAndIsWithoutPipeForCITY(String redundantValue) {
        // given
        String incorrectParameter = "CITY" + redundantValue;
        // then
        assertDoesNotThrow(() -> SpecialInputData.CITY.generateData().apply(incorrectParameter));
    }

    @DisplayName("Testing incorrect parameter and with pipe for CITY SpecialInputData")
    @ParameterizedTest(name = "redundant parameter value: {0}")
    @MethodSource("getOneIncorrectParameter")
    void shouldNotReturnCityWhenRedundantParametersIsPassedAndIsWithPipeForCITY(String redundantValue) {
        // given
        String incorrectParameter = "CITY|" + redundantValue;
        // when
        Optional<String> shouldBeCity = SpecialInputData.CITY.generateData().apply(incorrectParameter);
        // then
        assertFalse(shouldBeCity.isPresent());
    }

    @DisplayName("Testing incorrect parameters and arguments for CITY SpecialInputData")
    @ParameterizedTest(name = "startAt: {0}, startAtVal: {1}")
    @MethodSource("getTwoIncorrectParameters")
    void shouldNotThrowAnyExceptionWhenArgsAndParamsAreWrongForCITY(String startAt, String startAtVal) {
        // given
        String parameter = String.format("CITY|%s=%s", startAt, startAtVal);
        // then
        assertDoesNotThrow(() -> SpecialInputData.CITY.generateData().apply(parameter));
    }

    @DisplayName("Testing of creating random voivodeship for VOIVODESHIP SpecialInputData")
    @Test
    void shouldReturnRandomStreetForVOIVODESHIP() {
        // given
        String parameter = "VOIVODESHIP";
        // when
        Optional<String> shouldBeVoivodeship = SpecialInputData.VOIVODESHIP.generateData().apply(parameter);
        // then
        assertTrue(shouldBeVoivodeship.isPresent());
        String voivodeship = shouldBeVoivodeship.get();
        assertFalse(voivodeship.isBlank());
    }

    @DisplayName("Testing incorrect parameter and without pipe for VOIVODESHIP SpecialInputData")
    @ParameterizedTest(name = "redundant parameter value: {0}")
    @MethodSource("getOneIncorrectParameter")
    void shouldNotThrowAnyExceptionWhenRedundantParametersIsPassedAndIsWithoutPipeForVOIVODESHIP(String redundantValue) {
        // given
        String incorrectParameter = "VOIVODESHIP" + redundantValue;
        // then
        assertDoesNotThrow(() -> SpecialInputData.VOIVODESHIP.generateData().apply(incorrectParameter));
    }

    @DisplayName("Testing incorrect parameter and with pipe for VOIVODESHIP SpecialInputData")
    @ParameterizedTest(name = "redundant parameter value: {0}")
    @MethodSource("getOneIncorrectParameter")
    void shouldReturnEmptyOptionalWhenRedundantParametersIsPassedAndIsWithPipeForVOIVODESHIP(String redundantValue) {
        // given
        String incorrectParameter = "VOIVODESHIP|" + redundantValue;
        // when
        Optional<String> shouldBeEmpty = SpecialInputData.VOIVODESHIP.generateData().apply(incorrectParameter);
        // then
        assertTrue(shouldBeEmpty.isEmpty());
    }

    @DisplayName("Testing of creating random county for COUNTY SpecialInputData")
    @Test
    void shouldReturnRandomStreetForCOUNTY() {
        // given
        String parameter = "COUNTY";
        // when
        Optional<String> shouldBeCounty = SpecialInputData.COUNTY.generateData().apply(parameter);
        // then
        assertTrue(shouldBeCounty.isPresent());
        String county = shouldBeCounty.get();
        assertFalse(county.isBlank());
    }

    @DisplayName("Testing incorrect parameter and without pipe for COUNTY SpecialInputData")
    @ParameterizedTest(name = "redundant parameter value: {0}")
    @MethodSource("getOneIncorrectParameter")
    void shouldNotThrowAnyExceptionWhenRedundantParametersIsPassedAndIsWithoutPipeForCOUNTY(String redundantValue) {
        // given
        String incorrectParameter = "COUNTY" + redundantValue;
        // then
        assertDoesNotThrow(() -> SpecialInputData.COUNTY.generateData().apply(incorrectParameter));
    }

    @DisplayName("Testing incorrect parameter and with pipe for COUNTY SpecialInputData")
    @ParameterizedTest(name = "redundant parameter value: {0}")
    @MethodSource("getOneIncorrectParameter")
    void shouldReturnEmptyOptionalWhenRedundantParametersIsPassedAndIsWithPipeForCOUNTY(String redundantValue) {
        // given
        String incorrectParameter = "COUNTY|" + redundantValue;
        // when
        Optional<String> shouldBeEmpty = SpecialInputData.COUNTY.generateData().apply(incorrectParameter);
        // then
        assertTrue(shouldBeEmpty.isEmpty());
    }

    @DisplayName("Testing correct parameters for ADDRESS SpecialInputData")
    @Test
    @SuppressWarnings("unchecked")
    void shouldReturnRandomAddressBasedOnCorrectParametersForADDRESS() {
        // given
        String beforeFormat =
                "ADDRESS|cityPropName=%s,streetPropName=%s,postcodePropName=%s,voivodeshipPropName=%s,countyPropName=%s";
        String cityName = "cityTest";
        String streetName = "streetTest";
        String postcodeName = "postcodeTest";
        String voivodeshipName = "voivodeshipTest";
        String countyName = "countyTest";
        String parameter = String.format(beforeFormat, cityName, streetName, postcodeName,
                voivodeshipName, countyName);
        // when
        Optional<String> shouldBeAddress = SpecialInputData.ADDRESS.generateData().apply(parameter);
        // then
        assertTrue(shouldBeAddress.isPresent());
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> parsedAddress = new HashMap<>();
        assertDoesNotThrow(() -> parsedAddress.putAll(objectMapper.readValue(shouldBeAddress.get(), Map.class)));
        assertFalse(parsedAddress.get(cityName).isBlank());
        assertFalse(parsedAddress.get(streetName).isBlank());
        assertFalse(parsedAddress.get(postcodeName).isBlank());
        assertFalse(parsedAddress.get(voivodeshipName).isBlank());
        assertFalse(parsedAddress.get(countyName).isBlank());
    }

    @DisplayName("Testing ADDRESS SpecialInputData without any parameter")
    @Test
    @SuppressWarnings("unchecked")
    void shouldReturnRandomAddressWithDefaultPropNamesWhenNoParametersArePassedForADDRESS() {
        // given
        String parameter = "ADDRESS";
        String cityKey = "city";
        String streetKey = "street";
        String postcodeKey = "postcode";
        String voivodeshipKey = "voivodeship";
        String countyKey = "county";
        // when
        Optional<String> shouldBeAddress = SpecialInputData.ADDRESS.generateData().apply(parameter);
        // then
        assertTrue(shouldBeAddress.isPresent());
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> parsedAddress = new HashMap<>();
        assertDoesNotThrow(() -> parsedAddress.putAll(objectMapper.readValue(shouldBeAddress.get(), Map.class)));
        assertNotNull(parsedAddress.get(cityKey));
        assertNotNull(parsedAddress.get(streetKey));
        assertNotNull(parsedAddress.get(postcodeKey));
        assertNotNull(parsedAddress.get(voivodeshipKey));
        assertNotNull(parsedAddress.get(countyKey));
    }

    @DisplayName("Testing incorrect parameters for ADDRESS SpecialInputData")
    @ParameterizedTest(name =
            "cityPropName: {0}, streetPropName: {1}, postcodePropName: {2}, voivodeshipPropName: {3}, countyPropName:{4}")
    @MethodSource("getFiveIncorrectParameters")
    void shouldNotThrowAnyExceptionWhenWrongParametersArePassedForADDRESS(
            String cityVal, String streetVal, String postcodeVal, String voivodeshipVal, String countyVal) {
        // given
        String beforeFormat =
                "ADDRESS|cityPropName=%s,streetPropName=%s,postcodePropName=%s,voivodeshipPropName=%s,countyPropName=%s";
        String parameter = String.format(beforeFormat, cityVal, streetVal, postcodeVal,
                voivodeshipVal, countyVal);
        // then
        assertDoesNotThrow(() -> SpecialInputData.ADDRESS.generateData().apply(parameter));
    }

    @DisplayName("Testing incorrect parameters for ADDRESS SpecialInputData")
    @ParameterizedTest(name =
            "cityProp: {0}, cityVal: {1}, streetProp: {2}, streetVal: {3}, postcodeProp:{4}, postcodeVal: {5}, " +
                    "voivodeshipProp: {6}, voivodeshipVal: {7}, countyProp: {8}, countyVal:{9}")
    @MethodSource("getTenIncorrectParameters")
    @SuppressWarnings("unchecked")
    void shouldReturnEmptyOptionalRandomAddressWithDefaultPropNamesWhenWrongArgsAndParamsArePassedForADDRESS(
            String cityProp, String cityVal, String streetProp, String streetVal, String postcodeProp,
            String postcodeVal, String voivodeshipProp, String voivodeshipVal, String countyProp, String countyVal) {
        // given
        String beforeFormat =
                "ADDRESS|%s=%s,%s=%s,%s=%s,%s=%s,%s=%s";
        String parameter = String.format(beforeFormat, cityProp, cityVal, streetProp, streetVal, postcodeProp,
                postcodeVal, voivodeshipProp, voivodeshipVal, countyProp, countyVal);
        String cityKey = "city";
        String streetKey = "street";
        String postcodeKey = "postcode";
        String voivodeshipKey = "voivodeship";
        String countyKey = "county";
        // when
        Optional<String> shouldBeAddress = SpecialInputData.ADDRESS.generateData().apply(parameter);
        // then
        if (shouldBeAddress.isPresent()) {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> parsedAddress = new HashMap<>();
            assertDoesNotThrow(() -> parsedAddress.putAll(objectMapper.readValue(shouldBeAddress.get(), Map.class)));
            assertNotNull(parsedAddress.get(cityKey));
            assertNotNull(parsedAddress.get(streetKey));
            assertNotNull(parsedAddress.get(postcodeKey));
            assertNotNull(parsedAddress.get(voivodeshipKey));
            assertNotNull(parsedAddress.get(countyKey));
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
                case 0 -> stream = Stream.concat(stream, Stream.of(
                        Arguments.of(
                                AvailableCountries.values()[availableCountryIndex],
                                PolandBankId.values()[polandBankIdIndex], true, true)));
                case 1 -> stream = Stream.concat(stream, Stream.of(
                        Arguments.of(
                                AvailableCountries.values()[availableCountryIndex],
                                PolandBankId.values()[polandBankIdIndex], true, false)));
                case 2 -> stream = Stream.concat(stream, Stream.of(
                        Arguments.of(
                                AvailableCountries.values()[availableCountryIndex],
                                PolandBankId.values()[polandBankIdIndex], false, true)));
                case 3 -> stream = Stream.concat(stream, Stream.of(
                        Arguments.of(
                                AvailableCountries.values()[availableCountryIndex],
                                PolandBankId.values()[polandBankIdIndex], false, false)));
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

    private static Stream<Arguments> getArgumentsWithLettersOnly() {
        return Arrays.stream(generateArrayOfLetters())
                .map(Arguments::of);
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
                Arguments.of("-1", "1"),
                Arguments.of("-", "-"),
                Arguments.of("--", "--"),
                Arguments.of(".*", ".*"),
                Arguments.of(StringSeparator.PIPE, StringSeparator.PIPE),
                Arguments.of(StringSeparator.PIPE_REGEX, StringSeparator.PIPE_REGEX),
                Arguments.of("||", "||"),
                Arguments.of(StringSeparator.COMMA, StringSeparator.COMMA),
                Arguments.of(",,", ",,"),
                Arguments.of(StringSeparator.EMPTY_STRING, StringSeparator.EMPTY_STRING),
                Arguments.of(StringSeparator.EMPTY_SPACE, StringSeparator.EMPTY_SPACE)
        );
    }

    private static Stream<Arguments> getOneIncorrectParameter() {
        return Stream.of(
                Arguments.of("abcd"),
                Arguments.of("ABCD"),
                Arguments.of("null"),
                Arguments.of("-1"),
                Arguments.of("-"),
                Arguments.of("--"),
                Arguments.of(".*"),
                Arguments.of(StringSeparator.PIPE),
                Arguments.of(StringSeparator.PIPE_REGEX),
                Arguments.of("||"),
                Arguments.of(StringSeparator.COMMA),
                Arguments.of(",,"),
                Arguments.of(StringSeparator.EMPTY_STRING),
                Arguments.of(StringSeparator.EMPTY_SPACE)
        );
    }

    private static Stream<Arguments> getIncorrectFirstAndCorrectSecondOneParametersForNameAndSurnameSpecialInputData() {
        return Stream.of(
                Arguments.of("abcd", "true"),
                Arguments.of("ABCD", "true"),
                Arguments.of("null", "true"),
                Arguments.of("-1", "true"),
                Arguments.of("-", "true"),
                Arguments.of("--", "true"),
                Arguments.of(".*", "true"),
                Arguments.of(StringSeparator.PIPE, "true"),
                Arguments.of(StringSeparator.PIPE_REGEX, "true"),
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
                Arguments.of("-1", "1", StringSeparator.PIPE, StringSeparator.PIPE),
                Arguments.of("-1", "1", StringSeparator.PIPE_REGEX, StringSeparator.PIPE_REGEX),
                Arguments.of("-", "-", ",,", ",,"),
                Arguments.of(".*", ".*", ".*", ".*"),
                Arguments.of("--", "--", "||", "||"),
                Arguments.of(StringSeparator.PIPE, StringSeparator.PIPE, "{", "}"),
                Arguments.of(StringSeparator.PIPE_REGEX, StringSeparator.PIPE_REGEX, "{", "}"),
                Arguments.of("||", "||", "ABCD", "truthy"),
                Arguments.of(StringSeparator.COMMA, StringSeparator.COMMA, StringSeparator.COMMA, StringSeparator.COMMA),
                Arguments.of(",,", ",,", "-", "-"),
                Arguments.of(StringSeparator.EMPTY_STRING, StringSeparator.EMPTY_STRING, "--", "--"),
                Arguments.of(StringSeparator.EMPTY_SPACE, StringSeparator.EMPTY_SPACE, "01", "-1")
        );
    }

    private static Stream<Arguments> getTenIncorrectParameters() {
        return getFiveIncorrectParameters()
                .map(arg -> {
                    Object[] params = arg.get();
                    Object[] newParams = new Object[params.length * 2];
                    for (int i = 0; i < newParams.length; i++) {
                        newParams[i] = params[i / 2];
                    }
                    return Arguments.of(newParams);
                });
    }

    private static Stream<Arguments> getFiveIncorrectParameters() {
        return Stream.of(
                Arguments.of("abcd", "falsy", StringSeparator.EMPTY_SPACE, StringSeparator.EMPTY_SPACE,
                        "truthy"),
                Arguments.of("ABCD", "truthy", StringSeparator.EMPTY_STRING, StringSeparator.EMPTY_STRING,
                        StringSeparator.PIPE),
                Arguments.of("null", "null", "null", "null", "null"),
                Arguments.of("-1", "1", StringSeparator.PIPE, StringSeparator.PIPE,
                        StringSeparator.EMPTY_STRING),
                Arguments.of("-1", "1", StringSeparator.PIPE_REGEX, StringSeparator.PIPE_REGEX, ".*"),
                Arguments.of("-", "-", ",,", ",,", StringSeparator.PIPE_REGEX),
                Arguments.of(".*", ".*", ".*", ".*", StringSeparator.COMMA),
                Arguments.of("--", "--", "||", "||", "||"),
                Arguments.of(StringSeparator.PIPE, StringSeparator.PIPE, "{", "}", StringSeparator.EQUALS),
                Arguments.of(StringSeparator.PIPE_REGEX, StringSeparator.PIPE_REGEX, "{", "}",
                        StringSeparator.EQUALS),
                Arguments.of("||", "||", "ABCD", "truthy", StringSeparator.EMPTY_SPACE),
                Arguments.of(StringSeparator.COMMA, StringSeparator.COMMA, StringSeparator.COMMA,
                        StringSeparator.COMMA, StringSeparator.COMMA),
                Arguments.of(",,", ",,", "-", "-", StringSeparator.PIPE),
                Arguments.of(StringSeparator.EMPTY_STRING, StringSeparator.EMPTY_STRING, "--", "--",
                        StringSeparator.SEMICOLON),
                Arguments.of(StringSeparator.EQUALS, StringSeparator.EQUALS, StringSeparator.EQUALS,
                        StringSeparator.EQUALS, StringSeparator.EQUALS),
                Arguments.of(StringSeparator.EMPTY_SPACE, StringSeparator.EMPTY_SPACE, "01", "-1",
                        StringSeparator.EMPTY_SPACE)
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
                Arguments.of("-1", "1", StringSeparator.PIPE),
                Arguments.of("-1", "1", StringSeparator.PIPE_REGEX),
                Arguments.of("-", "-", ",,"),
                Arguments.of(".*", ".*", ".*"),
                Arguments.of("--", "--", "||"),
                Arguments.of(StringSeparator.PIPE, StringSeparator.PIPE, "{"),
                Arguments.of(StringSeparator.PIPE_REGEX, StringSeparator.PIPE_REGEX, "{"),
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

