package com.lukaszsuma.regexdatagenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lukaszsuma.regexdatagenerator.utils.CountryLettersToNumber;
import com.lukaszsuma.regexdatagenerator.utils.IBANValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings({"ConstantConditions", "unchecked"})
public class RegexDataGeneratorTest {

    private static final Path TEST_RESOURCE_DIRECTORY = Paths.get("src", "test", "resources");
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @BeforeAll
    static void removeAllResultFiles() {
        File testResourcesDirectory = new File(TEST_RESOURCE_DIRECTORY.toString());
        File[] listOfFiles = Arrays.stream(testResourcesDirectory.listFiles())
                .filter(fileName -> fileName.getName().contains("_result"))
                .toArray(File[]::new);
        for (File resultFile : listOfFiles) {
            try {
                Files.delete(resultFile.getAbsoluteFile().toPath());
            } catch (IOException e) {
                System.out.println("Cannot remove result file: " + e.getMessage());
            }
        }
    }

    @Test
    void aabc() throws IOException {
        //given
        String inputFileName = "abc.json";
        // when
        Map<String, String> resultFromFile = (Map<String, String>) generateResultByFileName(inputFileName);
        // then
    }

    @Test
    void shouldCreateResultFileInTheSameDirectoryWithDefaultValuesForEverySpecialInputData() throws IOException {
        //given
        String inputFileName = "allSpecialInputDataTest.json";
        // when
        List<Map<String, Object>> resultFromFile = (List<Map<String, Object>>) generateResultByFileName(inputFileName);
        // then
        String address = "address";
        String city = "city";
        String surname = "surname";
        String street = "street";
        String iban = "iban";
        String name = "name";
        String postcode = "postcode";
        String county = "county";
        String voivodeship = "voivodeship";
        String id = "id";
        String pesel = "pesel";
        assertEquals(5, resultFromFile.size());
        for (Map<String, Object> object : resultFromFile) {
            Object surnameValue = object.get(surname);
            assertNotNull(surnameValue);
            assertTrue(surnameValue instanceof String);
            assertNotNull(iban);
            assertNotNull(name);
            assertNotNull(id);
            assertNotNull(pesel);
            Object shouldBeNotNull = object.get(address);
            assertNotNull(shouldBeNotNull);
            Map<String, String> addressValue = (Map<String, String>) shouldBeNotNull;
            assertNotNull(addressValue.get(city));
            assertNotNull(addressValue.get(street));
            assertNotNull(addressValue.get(postcode));
            assertNotNull(addressValue.get(voivodeship));
            assertNotNull(addressValue.get(county));
        }
    }

    @Test
    void shouldCreateResultFileWithSomeRandomValuesGeneratedFromRegex() throws IOException {
        //given
        String inputFileName = "randomPropertiesAndRegexValues.json";
        // when
        List<Map<String, Object>> resultFromFile = (List<Map<String, Object>>) generateResultByFileName(inputFileName);
        // then
        String threeDigits = "threeDigits";
        Pattern threeDigitsPattern = Pattern.compile("\\d{3}");
        String sixLettersLowercase = "sixLettersLowercase";
        Pattern sixLettersPattern = Pattern.compile("[a-z]{6}");
        String arrayWithThreeStrings = "arrayWithThreeStrings";
        Pattern stringFromArrayWithThreeStringsPattern = Pattern.compile("\\w{1,5}");
        String objectWithTwoProperties = "objectWithTwoProperties";
        String someUppercaseTwoLetters = "someUppercaseTwoLetters";
        Pattern someUppercaseTwoLettersPattern = Pattern.compile("[A-Z]{2}");
        String arrayWithLowercaseThreeLettersAndOneNumber = "arrayWithLowercaseThreeLettersAndOneNumber";
        Pattern arrayWithLowercaseThreeLettersAndOneNumberPattern = Pattern.compile("[a-z]{3}\\d");
        String arrayOfTwoObjects = "arrayOfTwoObjects";
        String name = "name";
        Pattern namePattern = Pattern.compile("name[A-C]{3}");
        String surname = "surname";
        Pattern surnamePattern = Pattern.compile("surname[D-F]{2}");
        assertEquals(7, resultFromFile.size());
        for (Map<String, Object> object : resultFromFile) {
            String threeDigitsVal = (String) object.get(threeDigits);
            assertNotNull(threeDigitsVal);
            assertEquals(3, threeDigitsVal.length());
            assertTrue(threeDigitsPattern.matcher(threeDigitsVal).matches());
            String sixLettersLowercaseVal = (String) object.get(sixLettersLowercase);
            assertNotNull(sixLettersLowercaseVal);
            assertEquals(6, sixLettersLowercaseVal.length());
            assertTrue(sixLettersPattern.matcher(sixLettersLowercaseVal).matches());
            List<String> shouldBeThreeStrings = (List<String>) object.get(arrayWithThreeStrings);
            assertNotNull(shouldBeThreeStrings);
            assertEquals(3, shouldBeThreeStrings.size());
            for (String generatedString : shouldBeThreeStrings) {
                assertTrue(stringFromArrayWithThreeStringsPattern.matcher(generatedString).matches());
            }
            Map<String, Object> shouldBeTwoElements = (Map<String, Object>) object.get(objectWithTwoProperties);
            assertNotNull(shouldBeTwoElements);
            String someUppercaseTwoLettersVal = (String) shouldBeTwoElements.get(someUppercaseTwoLetters);
            assertNotNull(someUppercaseTwoLettersVal);
            assertTrue(someUppercaseTwoLettersPattern.matcher(someUppercaseTwoLettersVal).matches());
            List<String> someLowercaseThreeLettersWithOneNumberVal = (List<String>) shouldBeTwoElements
                    .get(arrayWithLowercaseThreeLettersAndOneNumber);
            assertNotNull(someLowercaseThreeLettersWithOneNumberVal);
            assertEquals(2, someLowercaseThreeLettersWithOneNumberVal.size());
            for (String generatedString : someLowercaseThreeLettersWithOneNumberVal) {
                assertTrue(arrayWithLowercaseThreeLettersAndOneNumberPattern.matcher(generatedString).matches());
            }
            List<Map<String, String>> innerList = (List<Map<String, String>>) object.get(arrayOfTwoObjects);
            assertNotNull(innerList);
            assertEquals(2, innerList.size());
            for (Map<String, String> innerMap : innerList) {
                String nameVal = innerMap.get(name);
                assertNotNull(nameVal);
                assertTrue(namePattern.matcher(nameVal).matches());
                String surnameVal = innerMap.get(surname);
                assertNotNull(surnameVal);
                assertTrue(surnamePattern.matcher(surnameVal).matches());
            }
        }
    }

    @Test
    void shouldCreateResultFileInTheSameDirectoryWithObject() throws IOException {
        //given
        String inputFileName = "onlyOneObjectWithProperties.json";
        // when
        Map<String, Object> resultFromFile = (Map<String, Object>) generateResultByFileName(inputFileName);
        // then
        String random = "random";
        Pattern randomPattern = Pattern.compile("random\\d{2}value");
        String id = "id";
        Pattern idPattern = Pattern.compile("some \\d id");
        String array = "array";
        Pattern arrayValuePattern = Pattern.compile("test \\w{2} value");
        String object = "object";
        String name = "name";
        Pattern namePattern = Pattern.compile("someName\\d{2}");
        String surname = "surname";
        Pattern surnamePattern = Pattern.compile("someSurname\\d{2}");
        assertEquals(4, resultFromFile.size());
        String randomVal = (String) resultFromFile.get(random);
        assertNotNull(randomVal);
        assertTrue(randomPattern.matcher(randomVal).matches());
        String idVal = (String) resultFromFile.get(id);
        assertNotNull(idVal);
        assertTrue(idPattern.matcher(idVal).matches());
        List<String> arrayVal = (List<String>) resultFromFile.get(array);
        assertNotNull(arrayVal);
        for (String generatedValue : arrayVal) {
            assertNotNull(generatedValue);
            assertTrue(arrayValuePattern.matcher(generatedValue).matches());
        }
        Map<String, String> objectVal = (Map<String, String>) resultFromFile.get(object);
        assertNotNull(objectVal);
        String nameVal = objectVal.get(name);
        assertNotNull(nameVal);
        assertTrue(namePattern.matcher(nameVal).matches());
        String surnameVal = objectVal.get(surname);
        assertNotNull(surnameVal);
        assertTrue(surnamePattern.matcher(surnameVal).matches());
    }

    @Test
    void shouldCreateResultFileWithEmptyArray() throws IOException {
        // given
        String inputFileName = "arrayOfObjectsWithNullValues.json";
        // when
        List<Map<String, Object>> resultFromFile = (List<Map<String, Object>>) generateResultByFileName(inputFileName);
        // then
        assertEquals(0, resultFromFile.size());
    }

    @Test
    void shouldCreateResultFileWithEmptyObject() throws IOException {
        //given
        String inputFileName = "objectWithNullValues.json";
        // when
        Map<String, Object> resultFromFile = (Map<String, Object>) generateResultByFileName(inputFileName);
        // then
        assertEquals(0, resultFromFile.size());
    }

    @Test
    void shouldCreateEmptyArrayWhenManyArraysArePassedInOneArray() throws IOException {
        //given
        String inputFileName = "emptyArraysInArray.json";
        // when
        List<Object> shouldBeEmptyArray = (List<Object>) generateResultByFileName(inputFileName);
        // then
        assertTrue(shouldBeEmptyArray.isEmpty());
    }

    @Test
    void shouldCreateArrayWithFourStringForGivenPattern() throws IOException {
        //given
        String inputFileName = "arrayWithStringsOnly.json";
        String pattern = "someNum\\d";
        // when
        List<String> shouldBeArrayWithStrings = (List<String>) generateResultByFileName(inputFileName);
        // then
        assertFalse(shouldBeArrayWithStrings.isEmpty());
        for (String string : shouldBeArrayWithStrings) {
            assertTrue(Pattern.compile(pattern).matcher(string).matches());
        }
    }

    @Test
    void shouldCreateSpecialInputDataWhenSeparatorWillBeChanged() throws IOException {
        //given
        String inputFileName = "specialInputDataWithChangedSeparatorBetweenParameters.json";
        String separator = "specialInputDataSeparator=$";
        // when
        List<Map<String, Object>> resultFromFile = (List<Map<String, Object>>) generateResultByFileName(inputFileName,
                separator);
        // then
        assertFalse(resultFromFile.isEmpty());
        assertEquals(2, resultFromFile.size());
        String address = "address";
        String street = "street";
        String city = "city";
        String cityStartAt = "D";
        String postcode = "postcode";
        String voivodeship = "voivodeship";
        String county = "county";
        String cityKeyName = "cityKeyName";
        String streetKeyName = "streetKeyName";
        String postcodeKeyName = "postcodeKeyName";
        Pattern postcodePattern = Pattern.compile("\\d{2}-\\d{3}");
        String voivodeshipKeyName = "voivodeshipKeyName";
        String countyKeyName = "countyKeyName";
        String nameStartAt = "B";
        String surnameStartAt = "C";
        String surname = "surname";
        String iban = "iban";
        Pattern ibanPattern = Pattern.compile("PL\\d{2} 2850 \\d{4} \\d{4} \\d{4} \\d{4} \\d{4}");
        String name = "name";
        String id = "id";
        String pesel = "pesel";
        int yearToCheck = Integer.parseInt(String.valueOf(LocalDateTime.now().getYear()).substring(2, 4)) - 18;
        int monthToCheck = LocalDateTime.now().getMonthValue();

        int idIteration = 1;
        for (Map<String, Object> object : resultFromFile) {
            Object shouldBeNotNull = object.get(address);
            assertNotNull(shouldBeNotNull);
            Map<String, String> addressValue = (Map<String, String>) shouldBeNotNull;
            assertNotNull(addressValue.get(cityKeyName));
            assertNotNull(addressValue.get(streetKeyName));
            String postcodeVal = addressValue.get(postcodeKeyName);
            assertNotNull(postcodeVal);
            assertTrue(postcodePattern.matcher(postcodeVal).matches());
            assertNotNull(addressValue.get(voivodeshipKeyName));
            assertNotNull(addressValue.get(countyKeyName));
            shouldBeNotNull = object.get(city);
            assertNotNull(shouldBeNotNull);
            String cityVal = (String) shouldBeNotNull;
            assertTrue(cityVal.startsWith(cityStartAt));
            assertNotNull(object.get(street));
            assertNotNull(object.get(voivodeship));
            assertNotNull(object.get(postcode));
            assertNotNull(object.get(county));
            shouldBeNotNull = object.get(iban);
            assertNotNull(shouldBeNotNull);
            String ibanVal = (String) shouldBeNotNull;
            assertTrue(ibanPattern.matcher(ibanVal).matches());
            StringBuilder sb = new StringBuilder(ibanVal);
            String numberForCountry = CountryLettersToNumber.convertCountryLettersToNumber("PL");
            int ibanControlNumber = IBANValidator.getIbanControlNumber(sb, numberForCountry, true, true);
            int controlNumberFromValue = Integer.parseInt(ibanVal.substring(2, 4));
            assertEquals(controlNumberFromValue, ibanControlNumber);
            shouldBeNotNull = object.get(name);
            assertNotNull(shouldBeNotNull);
            String nameVal = (String) shouldBeNotNull;
            assertTrue(nameVal.startsWith(nameStartAt));
            shouldBeNotNull = object.get(surname);
            assertNotNull(shouldBeNotNull);
            String surnameVal = (String) shouldBeNotNull;
            assertTrue(surnameVal.startsWith(surnameStartAt));
            shouldBeNotNull = object.get(pesel);
            assertNotNull(shouldBeNotNull);
            String peselVal = (String) shouldBeNotNull;
            int peselYear = Integer.parseInt(peselVal.substring(0, 2));
            assertTrue(peselYear <= yearToCheck);
            int peselMonth = Integer.parseInt(peselVal.substring(2, 4));
            assertTrue(peselMonth >= monthToCheck + 20);
            shouldBeNotNull = object.get(id);
            assertNotNull(shouldBeNotNull);
            int idVal = (int) shouldBeNotNull;
            assertEquals(idIteration, idVal);
            assertNotNull(addressValue.get(cityKeyName));
            idIteration++;
        }
    }

    @Test
    void shouldCreateResultFileWithSomeRandomValuesGeneratedFromRegexWithDifferentPrefixAndSuffix() throws IOException {
        //given
        String inputFileName = "randomPropertiesAndRegexValuesWithNewPrefixAndSuffix.json";
        String prefixAndSuffix = "prefixAndSuffix=^";
        // when
        List<Map<String, Object>> resultFromFile = (List<Map<String, Object>>) generateResultByFileName(inputFileName,
                prefixAndSuffix);
        // then
        String threeDigits = "threeDigits";
        Pattern threeDigitsPattern = Pattern.compile("\\d{3}");
        String sixLettersLowercase = "sixLettersLowercase";
        Pattern sixLettersPattern = Pattern.compile("[a-z]{6}");
        String arrayWithThreeStrings = "arrayWithThreeStrings";
        Pattern stringFromArrayWithThreeStringsPattern = Pattern.compile("\\w{1,5}");
        String objectWithTwoProperties = "objectWithTwoProperties";
        String someUppercaseTwoLetters = "someUppercaseTwoLetters";
        Pattern someUppercaseTwoLettersPattern = Pattern.compile("[A-Z]{2}");
        String arrayWithLowercaseThreeLettersAndOneNumber = "arrayWithLowercaseThreeLettersAndOneNumber";
        Pattern arrayWithLowercaseThreeLettersAndOneNumberPattern = Pattern.compile("[a-z]{3}\\d");
        String arrayOfTwoObjects = "arrayOfTwoObjects";
        String name = "name";
        Pattern namePattern = Pattern.compile("name[A-C]{3}");
        String surname = "surname";
        Pattern surnamePattern = Pattern.compile("surname[D-F]{2}");
        assertEquals(7, resultFromFile.size());
        for (Map<String, Object> object : resultFromFile) {
            String threeDigitsVal = (String) object.get(threeDigits);
            assertNotNull(threeDigitsVal);
            assertEquals(3, threeDigitsVal.length());
            assertTrue(threeDigitsPattern.matcher(threeDigitsVal).matches());
            String sixLettersLowercaseVal = (String) object.get(sixLettersLowercase);
            assertNotNull(sixLettersLowercaseVal);
            assertEquals(6, sixLettersLowercaseVal.length());
            assertTrue(sixLettersPattern.matcher(sixLettersLowercaseVal).matches());
            List<String> shouldBeThreeStrings = (List<String>) object.get(arrayWithThreeStrings);
            assertNotNull(shouldBeThreeStrings);
            assertEquals(3, shouldBeThreeStrings.size());
            for (String generatedString : shouldBeThreeStrings) {
                assertTrue(stringFromArrayWithThreeStringsPattern.matcher(generatedString).matches());
            }
            Map<String, Object> shouldBeTwoElements = (Map<String, Object>) object.get(objectWithTwoProperties);
            assertNotNull(shouldBeTwoElements);
            String someUppercaseTwoLettersVal = (String) shouldBeTwoElements.get(someUppercaseTwoLetters);
            assertNotNull(someUppercaseTwoLettersVal);
            assertTrue(someUppercaseTwoLettersPattern.matcher(someUppercaseTwoLettersVal).matches());
            List<String> someLowercaseThreeLettersWithOneNumberVal = (List<String>) shouldBeTwoElements
                    .get(arrayWithLowercaseThreeLettersAndOneNumber);
            assertNotNull(someLowercaseThreeLettersWithOneNumberVal);
            assertEquals(2, someLowercaseThreeLettersWithOneNumberVal.size());
            for (String generatedString : someLowercaseThreeLettersWithOneNumberVal) {
                assertTrue(arrayWithLowercaseThreeLettersAndOneNumberPattern.matcher(generatedString).matches());
            }
            List<Map<String, String>> innerList = (List<Map<String, String>>) object.get(arrayOfTwoObjects);
            assertNotNull(innerList);
            assertEquals(2, innerList.size());
            for (Map<String, String> innerMap : innerList) {
                String nameVal = innerMap.get(name);
                assertNotNull(nameVal);
                assertTrue(namePattern.matcher(nameVal).matches());
                String surnameVal = innerMap.get(surname);
                assertNotNull(surnameVal);
                assertTrue(surnamePattern.matcher(surnameVal).matches());
            }
        }
    }

    @Test
    void shouldReturnTenElementsWhenNewPrefixAndNameIsAddToFileButNotConfigured() throws IOException {
        //given
        String inputFileName = "arrayWithNewIterationNameAndPrefixButWithoutSettingItByArguments.json";
        // when
        List<Map<String, String>> shouldHaveTenElements = (List<Map<String, String>>) generateResultByFileName(inputFileName);
        // then
        assertEquals(10, shouldHaveTenElements.size());
    }

    @Test
    void shouldReturnTenElementsWhenIterationNameIsSetIncorrectly() throws IOException {
        //given
        String inputFileName = "arrayWithNewIterationNameButNotNumberAsValue.json";
        // when
        List<Map<String, String>> shouldHaveTenElements = (List<Map<String, String>>) generateResultByFileName(inputFileName);
        // then
        assertEquals(10, shouldHaveTenElements.size());
    }

    @Test
    void shouldReturnEmptyObjectsWhenEmptyObjectIsPassedAsValueForProperty() throws IOException {
        //given
        String inputFileName = "arrayOfObjectsWithPropertyWithEmptyObject.json";
        // when
        List<Map<String, Object>> shouldHaveTenElements = (List<Map<String, Object>>) generateResultByFileName(inputFileName);
        // then
        String emptyObject = "emptyObject";
        assertEquals(10, shouldHaveTenElements.size());
        for (Map<String, Object> object : shouldHaveTenElements) {
            Object shouldExist = object.get(emptyObject);
            assertNotNull(shouldExist);
            Map<String, Object> shouldBeEmpty = (Map<String, Object>) shouldExist;
            assertTrue(shouldBeEmpty.isEmpty());
        }
    }

    @Test
    void shouldReturnEmptyArraysWhenEmptyArrayIsPassedAsValueForProperty() throws IOException {
        //given
        String inputFileName = "arrayOfObjectsWithPropertyWithEmptyArray.json";
        // when
        List<Map<String, Object>> shouldHaveTenElements = (List<Map<String, Object>>) generateResultByFileName(inputFileName);
        // then
        String emptyObject = "emptyArray";
        assertEquals(10, shouldHaveTenElements.size());
        for (Map<String, Object> object : shouldHaveTenElements) {
            Object shouldExist = object.get(emptyObject);
            assertNotNull(shouldExist);
            List<String> shouldBeEmpty = (List<String>) shouldExist;
            assertTrue(shouldBeEmpty.isEmpty());
        }
    }

    private Object generateResultByFileName(String... args) throws IOException {
        String inputFileName = args[0];
        String resultFileName = inputFileName.replace(".json", "_result.json");
        Path allSpecialInputDataTest = TEST_RESOURCE_DIRECTORY.resolve(inputFileName);
        String jsonFileNameArg = "jsonFileName=" + allSpecialInputDataTest;
        String[] argsWithoutJsonFileName = Arrays.copyOfRange(args, 1, args.length);
        String[] allArgs = Arrays.copyOf(argsWithoutJsonFileName, argsWithoutJsonFileName.length + 1);
        allArgs[allArgs.length - 1] = jsonFileNameArg;
        RegexDataGenerator regexDataGenerator = new RegexDataGenerator(allArgs);
        regexDataGenerator.generateData();
        regexDataGenerator.createResult();
        InputStream stream = Files.newInputStream(TEST_RESOURCE_DIRECTORY
                .resolve(resultFileName), StandardOpenOption.READ);
        return OBJECT_MAPPER.readValue(stream, Object.class);
    }

}
