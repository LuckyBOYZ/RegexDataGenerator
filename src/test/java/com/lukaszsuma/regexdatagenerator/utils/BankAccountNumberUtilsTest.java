package com.lukaszsuma.regexdatagenerator.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountNumberUtilsTest {

    private final Random random = new Random(System.currentTimeMillis());

    @DisplayName("Only values in PolandBankId.java enum are correct")
    @ParameterizedTest(name = "Current bank name: {0}")
    @ValueSource(strings = {"NBP", "PKOBP", "CITI", "ING", "SANTANDER_PL", "BGK", "MBANK", "MILLENNIUM", "PKOSA",
            "POCZTOWY", "BOS", "MERCEDES", "SGB", "PLUS", "SOCIETE", "NEST", "BPS", "AGRICOLE", "BNP", "SANTANDER_CS",
            "TOYOTA", "DNB", "ALIOR", "FCE", "INBANK", "VOLKSWAGEN", "HSBC", "BFF", "AION", "VELO"
    })
    void shouldContainsBankIdWhenCorrectBankNameIsPassed(String bankName) {
        // given
        int index = random.nextInt(0, PolandBankId.values().length);
        // when
        String accountNumber = BankAccountNumberUtils.generateBankAccountNumber(AvailableCountries.PL.name(), bankName,
                false, false, index);
        String bankId = PolandBankId.valueOf(bankName).getId();
        // then
        String shouldBeBankId = accountNumber.substring(2, 6);
        assertEquals(bankId, shouldBeBankId);
    }

    @DisplayName("Testing incorrect values for bank name")
    @ParameterizedTest(name = "Current incorrect bank name: {0}")
    @ValueSource(strings = {StringSeparator.EMPTY_STRING, StringSeparator.EMPTY_SPACE, "wrong", "name", "-1"})
    void shouldContainsRandomBankIdWhenIncorrectBankNameIsPassed(String fakeBankName) {
        // given
        int index = random.nextInt(0, PolandBankId.values().length);
        PolandBankId defaultBankId = PolandBankId.values()[index];
        //when
        String accountNumber = BankAccountNumberUtils.generateBankAccountNumber(AvailableCountries.PL.name(), fakeBankName,
                false, false, index);
        // then
        String shouldBeDefaultBankId = accountNumber.substring(2, 6);
        assertEquals(defaultBankId.getId(), shouldBeDefaultBankId);
    }

    @DisplayName("Only values in AvailableCountries.java enum are correct")
    @ParameterizedTest(name = "Current country letters: {0}")
    @ValueSource(strings = {"PL"})
    void shouldContainsTwoLettersAtTheBeginningWhenWithLettersParameterIsTrue(String countryLetters) {
        //when
        String accountNumber = BankAccountNumberUtils.generateBankAccountNumber(countryLetters,
                PolandBankId.ING.name(), false, true, 0);
        // then
        String shouldBePL = accountNumber.substring(0, 2);
        assertEquals(countryLetters, shouldBePL);
    }

    @DisplayName("Testing incorrect values for country letters")
    @ParameterizedTest(name = "Current country letters: {0}")
    @ValueSource(strings = {StringSeparator.EMPTY_STRING, StringSeparator.EMPTY_SPACE, "test", "AA", "123"})
    void shouldContainsPLAtTheBeginningWhenUnavailableCountryLettersArePassed(String incorrectCountryLetters) {
        //when
        String accountNumber = BankAccountNumberUtils.generateBankAccountNumber(incorrectCountryLetters,
                PolandBankId.ING.name(), false, true, 0);
        // then
        String shouldBePL = accountNumber.substring(0, 2);
        assertEquals(AvailableCountries.PL.name(), shouldBePL);
    }

    @Test
    @DisplayName("Testing 'formatted' property set on true and 'withLetters' property set on false")
    void shouldBeFormattedWithSpacesWhenFormattedPropertyIsTrue() {
        //when
        String accountNumber = BankAccountNumberUtils.generateBankAccountNumber(AvailableCountries.PL.name(),
                PolandBankId.ING.name(), true, false, 0);
        // then
        String[] split = accountNumber.split(StringSeparator.EMPTY_SPACE);
        assertEquals(7, split.length);
        assertFalse(split[0].contains(AvailableCountries.PL.name()));
    }

    @Test
    @DisplayName("Testing 'formatted' property set on true and 'withLetters' property set on true")
    void shouldBeFormattedWithSpacesAndContainsCountryLettersWhenFormattedPropertyIsTrueAndWithLettersIsTrue() {
        //when
        String accountNumber = BankAccountNumberUtils.generateBankAccountNumber(AvailableCountries.PL.name(),
                PolandBankId.ING.name(), true, true, 0);
        System.out.println(accountNumber);
        // then
        String[] split = accountNumber.split(StringSeparator.EMPTY_SPACE);
        assertEquals(7, split.length);
        assertEquals(AvailableCountries.PL.name(), split[0].substring(0, 2));
    }

}
