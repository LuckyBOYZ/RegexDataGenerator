package com.lukaszsuma.regexdatagenerator;

import com.lukaszsuma.regexdatagenerator.utils.StringSeparator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

@SuppressWarnings("rawtypes")
class Configuration {

    private static final Logger logger = LogManager.getLogger(Configuration.class);
    private final Map<String, Object> configMap;
    private String prefixValue = ConfigurationPropertiesNames.PREFIX_AND_SUFFIX_FOR_SPECIAL_NAMES.getDefaultValue();

    public Configuration(String... appArgs) {
        logger.debug("constructor");
        this.configMap = new HashMap<>(ConfigurationPropertiesNames.values().length);
        for (ConfigurationPropertiesNames property : ConfigurationPropertiesNames.values()) {
            Class valueType = property.getTypeOfValue();
            if (valueType == Integer.class) {
                this.configMap.put(property.getPropertyName(), Integer.parseInt(property.getDefaultValue()));
            } else if (valueType == Boolean.class) {
                this.configMap.put(property.getPropertyName(), Boolean.parseBoolean(property.getDefaultValue()));
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
                    } else if (valueType == Boolean.class) {
                        entry.setValue(Boolean.parseBoolean(value));
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

        logger.info("Configuration is done. Current values:");
        this.configMap.forEach((k,v) -> logger.info("Property '{}' with value {} ", k, v));
    }

    private Object getValueByPropertyName(String propertyName) {
        logger.debug("getValueByPropertyName");
        return this.configMap.entrySet().stream()
                .filter(e -> e.getKey().equals(propertyName))
                .findFirst()
                .orElseThrow(() ->
                        new NoSuchElementException(String.format("No value present for '%s' property", propertyName)))
                .getValue();
    }

    public String getStringValueByPropertyName(String propertyName) {
        logger.debug("getStringValueByPropertyName");
        Object value = getValueByPropertyName(propertyName);
        return (String) value;
    }

    public int getIntegerValueByPropertyName(String propertyName) {
        logger.debug("getIntegerValueByPropertyName");
        Object value = getValueByPropertyName(propertyName);
        return (int) value;
    }

    public boolean getBooleanValueByPropertyName(String propertyName) {
        logger.debug("getBooleanValueByPropertyName");
        Object value = getValueByPropertyName(propertyName);
        return (boolean) value;
    }

}
