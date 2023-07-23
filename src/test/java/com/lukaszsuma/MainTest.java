package com.lukaszsuma;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lukaszsuma.regexdatagenerator.Main;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings({"ConstantConditions", "unchecked"})
public class MainTest {

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
    void shouldCreateResultFileInTheSameDirectoryWithDefaultValuesForEverySpecialInputData() throws IOException {
        //given
        String inputFileName = "allSpecialInputDataTest.json";
        String resultFileName = "allSpecialInputDataTest_result.json";
        Path allSpecialInputDataTest = TEST_RESOURCE_DIRECTORY.resolve(inputFileName);
        Main main = new Main("jsonFileName=" + allSpecialInputDataTest);
        // when
        main.generateData();
        main.createResult();
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
        InputStream stream = Files.newInputStream(TEST_RESOURCE_DIRECTORY
                .resolve(resultFileName), StandardOpenOption.READ);
        List<Map<String, Object>> resultFromFile = OBJECT_MAPPER.readValue(stream, List.class);
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
        String resultFileName = "randomPropertiesAndRegexValues_result.json";
        Path allSpecialInputDataTest = TEST_RESOURCE_DIRECTORY.resolve(inputFileName);
        Main main = new Main("jsonFileName=" + allSpecialInputDataTest);
        // when
        main.generateData();
        main.createResult();
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
        Pattern arrayWithLowercaseThreeLettersAndOneNumberPattern = Pattern.compile("[a-z]{3}\\d{1}");
        String arrayOfTwoObjects = "arrayOfTwoObjects";
        String name = "name";
        Pattern namePattern = Pattern.compile("name[A-C]{3}");
        String surname = "surname";
        Pattern surnamePattern = Pattern.compile("surname[D-F]{2}");
        InputStream stream = Files.newInputStream(TEST_RESOURCE_DIRECTORY
                .resolve(resultFileName), StandardOpenOption.READ);
        List<Map<String, Object>> resultFromFile = OBJECT_MAPPER.readValue(stream, List.class);
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

}
