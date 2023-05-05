package com.lukaszsuma.regexdatagenerator.config;

import java.util.Arrays;
import java.util.Optional;

public enum ConfigurationPropertiesNames {
    ITERATION_FIELD_NAME("iterationFieldName", "iteration", true, String.class),
    DEFAULT_ITERATION_NUMBER("iterationNumber", "10", false, Integer.class),
    JSON_FILE_NAME("jsonFileName", "file.json", false, String.class),
    PREFIX_AND_SUFFIX_FOR_SPECIAL_NAMES("prefixAndSuffix", "$", false, String.class);

    private final String propertyName;
    private final String defaultValue;
    private final boolean propertyWithPrefix;
    private final Class<?> typeOfValue;

    ConfigurationPropertiesNames(String propertyName, String defaultValue, boolean propertyWithPrefix, Class<?> typeOfValue) {
        this.propertyName = propertyName;
        this.defaultValue = defaultValue;
        this.propertyWithPrefix = propertyWithPrefix;
        this.typeOfValue = typeOfValue;
    }

    public static Optional<ConfigurationPropertiesNames> getPropertyByPropertyName(String propertyName) {
        return Arrays.stream(values())
                .filter(el -> el.getPropertyName().equals(propertyName))
                .findFirst();
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public boolean isPropertyWithPrefix() {
        return propertyWithPrefix;
    }

    public Class<?> getTypeOfValue() {
        return typeOfValue;
    }
}
