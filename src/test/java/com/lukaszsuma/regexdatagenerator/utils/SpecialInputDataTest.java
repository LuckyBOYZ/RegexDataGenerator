package com.lukaszsuma.regexdatagenerator.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SpecialInputDataTest {

    @DisplayName("Testing correct NAME SpecialInputData")
    @ParameterizedTest(name = "startAt: {0}, female: {1}")
    @MethodSource("getCorrectArgumentsForNameAndSurnameSpecialInputData")
    void shouldGetCorrectNameBasedOnParameters(String letter, String isFemale) {
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

    @DisplayName("Testing incorrect data for NAME SpecialInputData")
    @ParameterizedTest(name = "startAt: {0}, female: {1}")
    @MethodSource("getIncorrectArgumentsForNameAndSurnameSpecialInputData")
    void shouldGetEmptyNameOrJanBasedOnParameters(String letter, String isFemale) {
        // given
        String parameter = String.format("NAME|startAt=%s,female=%s", letter, isFemale);
        // when
        Optional<String> optionalName = SpecialInputData.NAME.generateData().apply(parameter);
        // then
        assertTrue(optionalName.isEmpty() || optionalName.get().equals("Jan"));
    }

    @DisplayName("Testing incorrect startAt value but female always as true for NAME SpecialInputData")
    @ParameterizedTest(name = "startAt: {0}, female: {1}")
    @MethodSource("getIncorrectStartAtValueAndFemaleOnTrueForNameAndSurnameSpecialInputData")
    void shouldGetEmptyNameOrJaninaBasedOnParameters(String letter, String isFemale) {
        // given
        String parameter = String.format("NAME|startAt=%s,female=%s", letter, isFemale);
        // when
        Optional<String> optionalName = SpecialInputData.NAME.generateData().apply(parameter);
        // then
        assertTrue(optionalName.isEmpty() || optionalName.get().equals("Janina"));
    }

    @DisplayName("Testing incorrect parameter names or no parameter for NAME SpecialInputData")
    @ParameterizedTest(name = "startAt: {0}, female: {1}, startAtVal: {2}, femaleVal: {3}")
    @MethodSource("getIncorrectArgumentsOrParametersForNameAndSurnameSpecialInputData")
    void shouldNotThrowAnyExceptionIfAllArgsAndParamsAreSetWronglyForNameSpecialInputData(
            String startAt, String female, String letter, String isFemale) {
        // given
        String parameter = String.format("NAME|%s=%s,%s=%s", startAt, female, letter, isFemale);
        // then
        assertDoesNotThrow(() -> SpecialInputData.NAME.generateData().apply(parameter));
    }

    @DisplayName("Testing SURNAME SpecialInputData")
    @ParameterizedTest(name = "startAt: {0}, female: {1}")
    @MethodSource("getCorrectArgumentsForNameAndSurnameSpecialInputData")
    void shouldGetCorrectSurnameBasedOnParameters(String letter, String isFemale) {
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
    @MethodSource("getIncorrectStartAtValueAndFemaleOnTrueForNameAndSurnameSpecialInputData")
    void shouldGetEmptyNameOrKowalskaBasedOnParameters(String letter, String isFemale) {
        // given
        String parameter = String.format("SURNAME|startAt=%s,female=%s", letter, isFemale);
        // when
        Optional<String> optionalName = SpecialInputData.SURNAME.generateData().apply(parameter);
        // then
        assertTrue(optionalName.isEmpty() || optionalName.get().equals("Kowalska"));
    }

    @DisplayName("Testing incorrect data for SURNAME SpecialInputData")
    @ParameterizedTest(name = "startAt: {0}, female: {1}")
    @MethodSource("getIncorrectArgumentsForNameAndSurnameSpecialInputData")
    void shouldGetIncorrectNameBasedOnParameters(String letter, String isFemale) {
        // given
        String parameter = String.format("SURNAME|startAt=%s,female=%s", letter, isFemale);
        // when
        Optional<String> optionalName = SpecialInputData.SURNAME.generateData().apply(parameter);
        // then
        assertTrue(optionalName.isEmpty() || optionalName.get().equals("Kowalski"));
    }

    @DisplayName("Testing incorrect parameter names or no parameter for SURNAME SpecialInputData")
    @ParameterizedTest(name = "startAt: {0}, female: {1}, startAtVal: {2}, femaleVal: {3}")
    @MethodSource("getIncorrectArgumentsOrParametersForNameAndSurnameSpecialInputData")
    void shouldNotThrowAnyExceptionIfAllArgsAndParamsAreSetWronglyForSurnameSpecialInputData(
            String startAt, String female, String letter, String isFemale) {
        // given
        String parameter = String.format("SURNAME|%s=%s,%s=%s", startAt, female, letter, isFemale);
        // then
        assertDoesNotThrow(() -> SpecialInputData.SURNAME.generateData().apply(parameter));
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

    private static Stream<Arguments> getIncorrectArgumentsForNameAndSurnameSpecialInputData() {
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

    private static Stream<Arguments> getIncorrectStartAtValueAndFemaleOnTrueForNameAndSurnameSpecialInputData() {
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

    private static Stream<Arguments> getIncorrectArgumentsOrParametersForNameAndSurnameSpecialInputData() {
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

