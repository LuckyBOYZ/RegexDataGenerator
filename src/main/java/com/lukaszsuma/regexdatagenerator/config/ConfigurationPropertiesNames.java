package com.lukaszsuma.regexdatagenerator.config;

import java.util.Arrays;
import java.util.Optional;

@SuppressWarnings("rawtypes")
public enum ConfigurationPropertiesNames {
    ITERATION_FIELD_NAME("iterationFieldName", "$$iteration$$", Integer.class),
    JSON_FILE_NAME("jsonFileName", "file.json", String.class);

    private final String propertyName;
    private final String defaultValue;
    private final Class valueType;

    ConfigurationPropertiesNames(String propertyName, String defaultValue, Class valueType) {
        this.propertyName = propertyName;
        this.defaultValue = defaultValue;
        this.valueType = valueType;
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

    public Class getValueType() {
        return valueType;
    }
}
