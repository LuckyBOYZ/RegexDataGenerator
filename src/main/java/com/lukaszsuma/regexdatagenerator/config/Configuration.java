package com.lukaszsuma.regexdatagenerator.config;

import com.lukaszsuma.regexdatagenerator.utils.StringSeparator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("rawtypes")
public class Configuration {

    private final Map<String, Object> configMap;
    private String prefixValue = ConfigurationPropertiesNames.PREFIX_AND_SUFFIX_FOR_SPECIAL_NAMES.getDefaultValue();

    public Configuration(String... appArgs) {
        this.configMap = new HashMap<>(ConfigurationPropertiesNames.values().length);
        for (ConfigurationPropertiesNames property : ConfigurationPropertiesNames.values()) {
            Class valueType = property.getTypeOfValue();
            if (valueType == Integer.class) {
                this.configMap.put(property.getPropertyName(), Integer.parseInt(property.getDefaultValue()));
            } else {
                this.configMap.put(property.getPropertyName(), property.getDefaultValue());
            }
        }
        String propName = ConfigurationPropertiesNames.PREFIX_AND_SUFFIX_FOR_SPECIAL_NAMES.getPropertyName();
        Object[] filteredArgs = Arrays.stream(appArgs)
                .filter(el -> {
                    String[] split = el.split(StringSeparator.EQUALS);
                    if (split.length < 2) {
                        return true;
                    }
                    boolean isPrefixSuffixArg = propName.equals(split[0]);
                    if (isPrefixSuffixArg) {
                        String newPrefixValue = split[1];
                        this.prefixValue = newPrefixValue;
                        this.configMap.computeIfPresent(propName, (k, v) -> newPrefixValue);
                    }
                    return !isPrefixSuffixArg;
                })
                .filter(el -> ConfigurationPropertiesNames.getPropertyByPropertyName(el.split(StringSeparator.EQUALS)[0]).isPresent())
                .toList().toArray();
        String[] args = Arrays.copyOf(filteredArgs, filteredArgs.length, String[].class);

        Arrays.stream(args).forEach(arg -> {
            String[] split = arg.split(StringSeparator.EQUALS);
            if (split.length < 2) {
                return;
            }
            for (Map.Entry<String, Object> entry : this.configMap.entrySet()) {
                String value = split[1];
                String propertyName = entry.getKey();
                Optional<ConfigurationPropertiesNames> prop = ConfigurationPropertiesNames.getPropertyByPropertyName(propertyName);
                if (propertyName.equals(split[0]) && !value.isBlank()) {
                    ConfigurationPropertiesNames configProp = prop.orElseThrow();
                    Class valueType = configProp.getTypeOfValue();
                    if (valueType == Integer.class) {
                        try {
                            entry.setValue(Integer.valueOf(value));
                        } catch (NumberFormatException ignore) {
                        }
                    } else {
                        entry.setValue(value);
                    }
                }
            }
        });

        for (ConfigurationPropertiesNames property : ConfigurationPropertiesNames.values()) {
            Class valueType = property.getTypeOfValue();
            if (valueType == Integer.class) {
                continue;
            }
            if (property.isPropertyWithPrefix()) {
                Object value = this.configMap.get(property.getPropertyName());
                this.configMap.computeIfPresent(property.getPropertyName(), (k, v) -> this.prefixValue + value + this.prefixValue);
            }
        }
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

    public Integer getIntegerValueByPropertyName(String propertyName) {
        Object value = getValueByPropertyName(propertyName);
        return (Integer) value;
    }

    public Map<String, Object> getConfigMap() {
        return configMap;
    }
}
