package com.lukaszsuma.regexdatagenerator.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("rawtypes")
public class Configuration {

    public static final String PROPERTY_SEPARATOR = "=";
    private final Map<String, Object> configMap;

    public Configuration(String... appArgs) {
        this.configMap = new HashMap<>(ConfigurationPropertiesNames.values().length);
        for (ConfigurationPropertiesNames property : ConfigurationPropertiesNames.values()) {
            this.configMap.put(property.getPropertyName(), property.getDefaultValue());
        }

        Arrays.stream(appArgs).forEach(arg -> {
            String[] split = arg.split(PROPERTY_SEPARATOR);
            if (split.length < 2) {
                return;
            }
            for (Map.Entry<String, Object> entry : this.configMap.entrySet()) {
                String value = split[1];
                String propertyName = entry.getKey();
                if (propertyName.equals(split[0]) && !value.isBlank()) {
                    Optional<ConfigurationPropertiesNames> optional = ConfigurationPropertiesNames.getPropertyByPropertyName(propertyName);
                    if (optional.isPresent()) {
                        ConfigurationPropertiesNames configProp = optional.get();
                        Class valueType = configProp.getValueType();
                        if (valueType == Integer.class) {
                            entry.setValue(Integer.valueOf(value));
                        } else {
                            entry.setValue(value);
                        }
                    }

                }
            }
        });
    }

    private Object getValueByPropertyName(String propertyName) {
        return this.configMap.entrySet().stream()
                .filter(e -> e.getKey().equals(propertyName))
                .findFirst()
                .orElseThrow()
                .getValue();
    }

    public String getStringValueByPropertyName(String propertyName) {
        Object value = getValueByPropertyName(propertyName);
        return (String) value;
    }

    public int getIntValueByPropertyName(String propertyName) {
        Object value = getValueByPropertyName(propertyName);
        return (int) value;
    }
}
