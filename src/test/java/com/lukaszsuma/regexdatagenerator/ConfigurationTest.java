package com.lukaszsuma.regexdatagenerator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationTest {

    @Test
    @DisplayName("Testing configuration without any property")
    void shouldCreateDefaultConfigurationWhenNoConfigIsPassed() {
        // given
        Configuration configuration = new Configuration();
        // then
        assertNotNull(configuration);
        String defaultValue = configuration.getStringValueByPropertyName(
                ConfigurationPropertiesNames.ITERATION_FIELD_NAME.getPropertyName());
        String defaultValueForIterationProp =
                ConfigurationPropertiesNames.PREFIX_AND_SUFFIX_FOR_SPECIAL_NAMES.getDefaultValue() +
                        ConfigurationPropertiesNames.ITERATION_FIELD_NAME.getDefaultValue() +
                        ConfigurationPropertiesNames.PREFIX_AND_SUFFIX_FOR_SPECIAL_NAMES.getDefaultValue();
        assertEquals(defaultValue, defaultValueForIterationProp);
        defaultValue = configuration.getStringValueByPropertyName(
                ConfigurationPropertiesNames.SPECIAL_INPUT_DATA_SEPARATOR.getPropertyName());
        assertEquals(defaultValue, ConfigurationPropertiesNames.SPECIAL_INPUT_DATA_SEPARATOR.getDefaultValue());
        defaultValue = configuration.getStringValueByPropertyName(
                ConfigurationPropertiesNames.JSON_FILE_NAME.getPropertyName());
        assertEquals(defaultValue, ConfigurationPropertiesNames.JSON_FILE_NAME.getDefaultValue());
        int defaultIntegerValue = configuration.getIntegerValueByPropertyName(
                ConfigurationPropertiesNames.DEFAULT_ITERATION_NUMBER.getPropertyName());
        assertEquals(defaultIntegerValue, Integer.parseInt(
                ConfigurationPropertiesNames.DEFAULT_ITERATION_NUMBER.getDefaultValue()));
        defaultValue = configuration.getStringValueByPropertyName(
                ConfigurationPropertiesNames.PREFIX_AND_SUFFIX_FOR_SPECIAL_NAMES.getPropertyName());
        assertEquals(defaultValue, ConfigurationPropertiesNames.PREFIX_AND_SUFFIX_FOR_SPECIAL_NAMES.getDefaultValue());
        boolean defaultBooleanValue = configuration.getBooleanValueByPropertyName(
                ConfigurationPropertiesNames.IS_FORMATTED_RESULT.getPropertyName());
        assertEquals(defaultBooleanValue, Boolean.parseBoolean(
                ConfigurationPropertiesNames.IS_FORMATTED_RESULT.getDefaultValue()));
    }

    @Test
    @DisplayName("Testing configuration with all valid properties")
    void shouldCreateConfigurationWhenConfigIsPassedWithCustomValues() {
        //given
        String[] customConfig = {"iterationFieldName=it", "iterationNumber=5", "jsonFileName=emptyArraysInArray.json",
                "prefixAndSuffix=%", "specialInputDataSeparator=_", "isFormattedResult=true"};
        Configuration configuration = new Configuration(customConfig);
        //then
        String iterationFieldName = "%it%";
        int iterationNumber = 5;
        String jsonFileName = "emptyArraysInArray.json";
        String prefixAndSuffix = "%";
        String specialInputDataSeparator = "_";
        boolean isFormattedResult = true;
        String customValue = configuration.getStringValueByPropertyName(
                ConfigurationPropertiesNames.ITERATION_FIELD_NAME.getPropertyName());
        assertEquals(iterationFieldName, customValue);
        int defaultIntegerCustomValue = configuration.getIntegerValueByPropertyName(
                ConfigurationPropertiesNames.DEFAULT_ITERATION_NUMBER.getPropertyName());
        assertEquals(iterationNumber, defaultIntegerCustomValue);
        customValue = configuration.getStringValueByPropertyName(
                ConfigurationPropertiesNames.JSON_FILE_NAME.getPropertyName());
        assertEquals(jsonFileName, customValue);
        customValue = configuration.getStringValueByPropertyName(
                ConfigurationPropertiesNames.PREFIX_AND_SUFFIX_FOR_SPECIAL_NAMES.getPropertyName());
        assertEquals(prefixAndSuffix, customValue);
        customValue = configuration.getStringValueByPropertyName(
                ConfigurationPropertiesNames.SPECIAL_INPUT_DATA_SEPARATOR.getPropertyName());
        assertEquals(specialInputDataSeparator, customValue);
        boolean defaultBooleanCustomValue = configuration.getBooleanValueByPropertyName(
                ConfigurationPropertiesNames.IS_FORMATTED_RESULT.getPropertyName());
        assertEquals(isFormattedResult, defaultBooleanCustomValue);
    }

    @Test
    @DisplayName("Testing configuration with not parsed value if property must be a number")
    void shouldContainsDefaultIterationNumberValueWhenValueIsNotANumber() {
        //given
        String[] customConfig = {"iterationNumber=abc"};
        Configuration configuration = new Configuration(customConfig);
        //then
        int defaultIntegerCustomValue = configuration.getIntegerValueByPropertyName(
                ConfigurationPropertiesNames.DEFAULT_ITERATION_NUMBER.getPropertyName());
        assertEquals(10, defaultIntegerCustomValue);
    }

    @Test
    @DisplayName("Testing configuration with not parsed value if property must be a boolean")
    void shouldContainsDefaultIterationNumberValueWhenValueIsNotABoolean() {
        //given
        String[] customConfig = {"isFormattedResult=abc"};
        Configuration configuration = new Configuration(customConfig);
        //then
        boolean defaultBooleanCustomValue = configuration.getBooleanValueByPropertyName(
                ConfigurationPropertiesNames.IS_FORMATTED_RESULT.getPropertyName());
        assertFalse(defaultBooleanCustomValue);
    }

    @Test
    @DisplayName("Testing configuration with not parsed value if property must be number")
    void shouldContainsDefaultValuesWhenValuesAreNotSeperatedByEqualSign() {
        //given
        String[] customConfig = {"jsonFileName,emptyArraysInArray.json"};
        Configuration configuration = new Configuration(customConfig);
        //then
        String shouldBeDefaultValue = configuration.getStringValueByPropertyName(
                ConfigurationPropertiesNames.JSON_FILE_NAME.getPropertyName());
        assertEquals("file.json", shouldBeDefaultValue);
    }

    @Test
    @DisplayName("Testing configuration with not parsed values but with wrong names")
    void shouldContainsOnlyPropertiesFromConfigurationPropertiesNamesClassWhenAdditionalPropertiesArePassed() {
        //given
        String[] customConfig = {"test=abc", "dummy=dummy"};
        Configuration configuration = new Configuration(customConfig);
        //then
        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () ->
                configuration.getStringValueByPropertyName("test"));
        assertEquals("No value present for 'test' property", ex.getMessage());
        ex = assertThrows(NoSuchElementException.class, () ->
                configuration.getStringValueByPropertyName("dummy"));
        assertEquals("No value present for 'dummy' property", ex.getMessage());
    }

}
