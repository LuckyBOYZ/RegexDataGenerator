package com.lukaszsuma.regexdatagenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.curiousoddman.rgxgen.RgxGen;
import com.lukaszsuma.regexdatagenerator.config.Configuration;
import com.lukaszsuma.regexdatagenerator.config.ConfigurationPropertiesNames;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unchecked", "rawtypes"})
public class RegexDataGenerator {

    private static final Path JAR_START_DIR = FileSystems.getDefault().getPath("").toAbsolutePath();
    private static final int ITERATION_DEFAULT_NUMBER = 10;

    private final Configuration configuration;
    private final ObjectMapper objectMapper;
    private final Map<String, Object> mapOfRegExs = new HashMap<>();
    private final List<Map<String, Object>> result = new ArrayList<>();

    public RegexDataGenerator(Configuration configuration, ObjectMapper objectMapper) {
        this.configuration = configuration;
        this.objectMapper = objectMapper;
    }

    public void generateData() throws IOException {
        String str = Files.readString(JAR_START_DIR.resolve(getFileName()));
        List<Map<String, Object>> parsedJson = objectMapper.readValue(str, List.class);
        Map<String, Object> parsedObjectMap = parsedJson.get(0);
        int iterationNumber = getIterationNumberFromParsedObject(parsedObjectMap);
        for (int i = 0; i < iterationNumber; i++) {
            Map<String, Object> generatedValues = generateValuesForObjectInstance(parsedObjectMap, this.mapOfRegExs);
            this.result.add(generatedValues);
        }
    }

    private Map<String, Object> generateValuesForObjectInstance(Map<String, Object> parsedObjectMap, Map<String, Object> regexMap) {
        Map<String, Object> result = new HashMap<>();
        RgxGen rgxGen;
        for (Map.Entry<String, Object> entry : parsedObjectMap.entrySet()) {
            if (entry.getValue() instanceof String value) {
                rgxGen = (RgxGen) regexMap.get(entry.getKey());
                if (rgxGen == null) {
                    rgxGen = new RgxGen(value);
                    regexMap.put(entry.getKey(), rgxGen);
                }
                result.put(entry.getKey(), rgxGen.generate());
            } else if (entry.getValue() instanceof List value) {
                if (!value.isEmpty()) {
                    Object firstEl = value.get(0);
                    if (firstEl instanceof String el) {
                        List<String> arr = new ArrayList<>();
                        result.put(entry.getKey(), arr);
                        rgxGen = (RgxGen) regexMap.get(entry.getKey());
                        if (rgxGen == null) {
                            rgxGen = new RgxGen(el);
                            regexMap.put(entry.getKey(), rgxGen);
                        }
                        int numberOfElements = ITERATION_DEFAULT_NUMBER;
                        if (value.size() > 1) {
                            try {
                                numberOfElements = (int) value.get(1);
                            } catch (NumberFormatException ignore) {
                            }
                        }
                        for (int j = 0; j < numberOfElements; j++) {
                            arr.add(rgxGen.generate());
                        }
                    } else if (firstEl instanceof Map el) {
                        List< Map<String, Object>> arr = new ArrayList<>();
                        result.put(entry.getKey(), arr);
                        Map<String, Object> innerRegexMap = (Map<String, Object>) regexMap.get(entry.getKey());
                        if (innerRegexMap == null) {
                            innerRegexMap = new HashMap<>();
                            regexMap.put(entry.getKey(), innerRegexMap);
                        }
                        int iterationNumber = getIterationNumberFromParsedObject(parsedObjectMap);
                        for (int j = 0; j < iterationNumber; j++) {
                            arr.add(generateValuesForObjectInstance(el, innerRegexMap));
                        }
                    }
                }
            } else if(entry.getValue() instanceof Map<?, ?> value) {
                Map<String, Object> innerRegexMap = (Map<String, Object>) regexMap.get(entry.getKey());
                if (innerRegexMap == null) {
                    innerRegexMap = new HashMap<>();
                    regexMap.put(entry.getKey(), innerRegexMap);
                }
                result.put(entry.getKey(), generateValuesForObjectInstance((Map<String, Object>) value, innerRegexMap));
            }
        }
        return result;
    }

    private int getIterationNumberFromParsedObject(Map<String, Object> objectMap) {
        return (int) objectMap.get(this.configuration
                .getStringValueByPropertyName(ConfigurationPropertiesNames.ITERATION_FIELD_NAME.getPropertyName()));
    }

    public void createResult() throws IOException {
        String fileName = getFileName();
        fileName = fileName.replace(".json", "_result.json");
        this.objectMapper.writeValue(JAR_START_DIR.resolve(fileName).toFile(), this.result);
        System.out.printf("Result file '%s' was created under path %s", fileName, JAR_START_DIR);
    }

    private String getFileName() {
        return this.configuration.getStringValueByPropertyName(ConfigurationPropertiesNames.JSON_FILE_NAME.getPropertyName());
    }
}
