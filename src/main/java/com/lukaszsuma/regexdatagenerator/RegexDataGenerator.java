package com.lukaszsuma.regexdatagenerator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.curiousoddman.rgxgen.RgxGen;
import com.lukaszsuma.regexdatagenerator.config.Configuration;
import com.lukaszsuma.regexdatagenerator.config.ConfigurationPropertiesNames;
import com.lukaszsuma.regexdatagenerator.utils.SpecialInputData;
import com.lukaszsuma.regexdatagenerator.utils.StringSeparator;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings({"unchecked", "rawtypes"})
public class RegexDataGenerator {

    private static final Path JAR_START_DIR = FileSystems.getDefault()
            .getPath(StringSeparator.EMPTY_STRING).toAbsolutePath();
    private final Configuration configuration;
    private final ObjectMapper objectMapper;
    private final Map<String, Object> mapOfRegExs = new HashMap<>();
    private final List<Map<String, Object>> result = new ArrayList<>();
    private boolean isObjectPassed = false;

    public RegexDataGenerator(Configuration configuration, ObjectMapper objectMapper) {
        this.configuration = configuration;
        this.objectMapper = objectMapper;
    }

    public void generateData() throws IOException {
        String str = Files.readString(JAR_START_DIR.resolve(getFileName()));
        Object parsedJson = objectMapper.readValue(str, Object.class);
        if (parsedJson instanceof List<?> list) {
            if (list.isEmpty()) {
                return;
            }
            Map<String, Object> parsedObjectMap = (Map<String, Object>) list.get(0);
            int iterationNumber = getIterationNumberFromParsedObject(parsedObjectMap);
            AtomicInteger id = new AtomicInteger(1);
            for (int i = 0; i < iterationNumber; i++) {
                Map<String, Object> generatedValues = generateObjectBasedOnRegexMap(parsedObjectMap, this.mapOfRegExs, id);
                if (!generatedValues.isEmpty()) {
                    this.result.add(generatedValues);
                }
            }
        } else if (parsedJson instanceof Map<?, ?> map) {
            this.isObjectPassed = true;
            Map<String, Object> generatedValues = generateObjectBasedOnRegexMap((Map<String, Object>) map, this.mapOfRegExs, null);
            this.result.add(generatedValues);
        }
    }

    public void createResult() throws IOException {
        String fileName = getFileName();
        fileName = fileName.replace(".json", "_result.json");
        File file = JAR_START_DIR.resolve(fileName).toFile();
        if (file.exists()) {
            int lastDotIndex = file.getPath().lastIndexOf(StringSeparator.DOT);
            String pathWithoutExtension = file.getPath().substring(0, lastDotIndex);
            file = new File(generateNewResultFilename(pathWithoutExtension));
        }
        this.objectMapper.writerWithDefaultPrettyPrinter().writeValue(file,
                this.isObjectPassed ? this.result.get(0) : this.result);
        System.out.printf("Result file '%s' was created under path %s", fileName, JAR_START_DIR);
    }

    private String generateNewResultFilename(String currentNameWithoutExtension) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String format = simpleDateFormat.format(new Date());
        return currentNameWithoutExtension.concat(StringSeparator.UNDERSCORE.concat(format).concat(".json"));
    }

    private Map<String, Object> generateObjectBasedOnRegexMap(
            Map<String, Object> parsedObjectMap, Map<String, Object> regexMap, AtomicInteger id)
            throws JsonProcessingException {
        Map<String, Object> result = new HashMap<>();
        RgxGen rgxGen;
        for (Map.Entry<String, Object> entry : parsedObjectMap.entrySet()) {
            if (entry.getValue() instanceof String value) {
                boolean isAdded = handleSpecialInput(entry.getKey(), value, result, id);
                if (!isAdded) {
                    rgxGen = getRgxGenFromRegexMap(regexMap, entry.getKey(), value);
                    result.put(entry.getKey(), rgxGen.generate());
                }
            } else if (entry.getValue() instanceof List value) {
                if (value.isEmpty()) {
                    result.put(entry.getKey(), Collections.emptyList());
                    continue;
                }
                Object firstEl = value.get(0);
                if (firstEl instanceof String el) {
                    List<String> arr = generateListOfStringsBasedOnRegexMap(value, regexMap, entry.getKey(), el);
                    result.put(entry.getKey(), arr);
                } else if (firstEl instanceof Map el) {
                    List<Map<String, Object>> arr = generateListOfObjectsBasedOnRegexMap(regexMap, el, entry.getKey());
                    result.put(entry.getKey(), arr);
                }

            } else if (entry.getValue() instanceof Map<?, ?> value) {
                Map<String, Object> innerRegexMap = getInnerRegexMapFromRegexMap(regexMap, entry.getKey());
                result.put(entry.getKey(), generateObjectBasedOnRegexMap((Map<String, Object>) value, innerRegexMap, null));
            }
        }
        return result;
    }

    private boolean isSpecialInputData(String value) {
        boolean isSpecialInputData = false;
        List<String> listOfAvailableInputData = getListOfAvailableInputData();
        for (String element : listOfAvailableInputData) {
            if (value.startsWith(element)) {
                isSpecialInputData = true;
                break;
            }
        }
        return isSpecialInputData;
    }

    private String getFileName() {
        return this.configuration.getStringValueByPropertyName(ConfigurationPropertiesNames.JSON_FILE_NAME.getPropertyName());
    }

    private int getIterationNumberFromParsedList(List<?> list) {
        int number = getDefaultIterationNumberFromConfiguration();
        if (list.size() > 1) {
            try {
                number = (int) list.get(1);
            } catch (ClassCastException ignore) {
            }
        }
        return number;
    }

    private int getIterationNumberFromParsedObject(Map<String, Object> objectMap) {
        Object value = objectMap.get(this.configuration
                .getStringValueByPropertyName(ConfigurationPropertiesNames.ITERATION_FIELD_NAME.getPropertyName()));
        if (value == null) {
            return getDefaultIterationNumberFromConfiguration();
        } else {
            try {
                return (int) value;
            } catch (ClassCastException ignore) {
                return getDefaultIterationNumberFromConfiguration();
            }
        }
    }

    private List<String> generateListOfStringsBasedOnRegexMap(List<?> list, Map<String, Object> regexMap, String key, String value) {
        List<String> arr = new ArrayList<>();
        RgxGen rgxGen = getRgxGenFromRegexMap(regexMap, key, value);
        int numberOfElements = getIterationNumberFromParsedList(list);
        for (int i = 0; i < numberOfElements; i++) {
            arr.add(rgxGen.generate());
        }
        return arr;
    }

    private RgxGen getRgxGenFromRegexMap(Map<String, Object> regexMap, String key, String value) {
        RgxGen rgxGen = (RgxGen) regexMap.get(key);
        if (rgxGen == null) {
            rgxGen = new RgxGen(value);
            regexMap.put(key, rgxGen);
        }
        return rgxGen;
    }

    private Map<String, Object> getInnerRegexMapFromRegexMap(Map<String, Object> regexMap, String key) {
        Map<String, Object> innerRegexMap = (Map<String, Object>) regexMap.get(key);
        if (innerRegexMap == null) {
            innerRegexMap = new HashMap<>();
            regexMap.put(key, innerRegexMap);
        }
        return innerRegexMap;
    }

    private List<Map<String, Object>> generateListOfObjectsBasedOnRegexMap(
            Map<String, Object> regexMap, Map<String, Object> parsedObject, String key) throws JsonProcessingException {
        List<Map<String, Object>> arr = new ArrayList<>();
        Map<String, Object> innerRegexMap = getInnerRegexMapFromRegexMap(regexMap, key);
        int iterationNumber = getIterationNumberFromParsedObject(parsedObject);
        AtomicInteger id = new AtomicInteger(1);
        for (int j = 0; j < iterationNumber; j++) {
            arr.add(generateObjectBasedOnRegexMap(parsedObject, innerRegexMap, id));
        }
        return arr;
    }

    private Integer getDefaultIterationNumberFromConfiguration() {
        return this.configuration.getIntegerValueByPropertyName(ConfigurationPropertiesNames.DEFAULT_ITERATION_NUMBER.getPropertyName());
    }

    private List<String> getListOfAvailableInputData() {
        return Arrays.stream(SpecialInputData.values())
                .map(SpecialInputData::name)
                .toList();
    }

    private boolean handleSpecialInput(String key, String rawValue, Map<String, Object> result, AtomicInteger id)
            throws JsonProcessingException {
        boolean isSpecialInputData = isSpecialInputData(rawValue);
        if (!isSpecialInputData) {
            return false;
        }
        SpecialInputData specialInputData;
        try {
            String specialInputName = rawValue.split(StringSeparator.PIPE_REGEX, 2)[0].toUpperCase();
            specialInputData = SpecialInputData.valueOf(specialInputName);
        } catch (IllegalArgumentException ignore) {
            return false;
        }
        switch (specialInputData) {
            case NAME, SURNAME, PESEL, IBAN, POSTCODE, STREET, CITY, VOIVODESHIP, COUNTY -> {
                Optional<String> generatedData = specialInputData.generateData().apply(rawValue);
                if (generatedData.isPresent()) {
                    String val = generatedData.get();
                    result.put(key, val);
                }
            }
            case ADDRESS -> {
                Optional<String> generatedData = specialInputData.generateData().apply(rawValue);
                if (generatedData.isPresent()) {
                    String val = generatedData.get();
                    Map<String, String> parsedStringAddress = this.objectMapper.readValue(val, Map.class);
                    result.put(key, parsedStringAddress);
                }
            }
            case ID -> {
                if (id != null) {
                    result.putIfAbsent(key, id.getAndIncrement());
                }
            }
        }
        return true;
    }
}
