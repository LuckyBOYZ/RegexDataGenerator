package com.lukaszsuma.regexdatagenerator.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PESELUtilsTest {

    @DisplayName("Testing PESEL generating with every configuration of parameters")
    @ParameterizedTest(name = "is born after 2000: {0}, is female: {1}, only adults: {2}")
    @MethodSource("provideBooleansForGeneratingPESEL")
    void shouldCreatePESELWithSpecificValues(boolean canBeAsBornAfter2000, boolean isFemale, boolean onlyAdults) {
        // when
        String pesel = PESELUtils.generateRandomPESEL(canBeAsBornAfter2000, isFemale, onlyAdults);
        // then
        assertDoesNotThrow(() -> Long.parseLong(pesel));
        assertEquals(11, pesel.length());
        if (canBeAsBornAfter2000) {
            int year = Integer.parseInt(pesel.substring(0, 2));
            int month = Integer.parseInt(pesel.substring(2, 4));
            int day = Integer.parseInt(pesel.substring(4, 6));
            int twoDigitsOfCurrentYear = getTwoDigitsOfCurrentYear();
            assertTrue(year >= 0 && year <= twoDigitsOfCurrentYear);
            assertTrue(month > 20);
            assertTrue(day > 0 && day < 32);
            if (onlyAdults) {
                assertTrue(year <= twoDigitsOfCurrentYear - 18);
            }
        }

        int sex = Integer.parseInt(pesel.substring(9, 10));
        if (isFemale) {
            assertEquals(sex % 2, 0);
        } else {
            assertEquals(sex % 2, 1);
        }
    }

    private int getTwoDigitsOfCurrentYear() {
        return Integer.parseInt(String.valueOf(LocalDateTime.now().getYear()).substring(2));
    }

    private static Stream<Arguments> provideBooleansForGeneratingPESEL() {
        return Stream.of(
                Arguments.of(true, true, true),
                Arguments.of(true, true, false),
                Arguments.of(true, false, true),
                Arguments.of(false, true, true),
                Arguments.of(true, false, false),
                Arguments.of(false, false, true),
                Arguments.of(false, false, false)
        );
    }
}
