package com.lukaszsuma.regexdatagenerator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.curiousoddman.rgxgen.RgxGen;
import com.lukaszsuma.regexdatagenerator.utils.StringSeparator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

@SuppressWarnings({"unchecked", "rawtypes", "UnusedReturnValue"})
class DataGenerator {

    private static final Logger logger = LogManager.getLogger(DataGenerator.class);
    private static final Path JAR_START_DIR = Path.of(System.getProperty("user.dir"));
    public static final String DEFAULT_KEY_NAME_FOR_ONLY_ARRAY_OF_STRINGS = "array";
    private final Configuration configuration;
    private final ObjectMapper objectMapper;
    private final Map<String, Object> mapOfRegExs = new HashMap<>();
    private final List<Map<String, Object>> result = new ArrayList<>();
    private final String specialInputDateSeparator;
    private boolean isObjectPassed;
    private boolean isArrayPassed;
    private final boolean isFormattedResult;

    public DataGenerator(Configuration configuration, ObjectMapper objectMapper) {
        logger.debug("constructor");
        this.configuration = configuration;
        String separator = configuration.getStringValueByPropertyName(
                ConfigurationPropertiesNames.SPECIAL_INPUT_DATA_SEPARATOR.getPropertyName());
        this.specialInputDateSeparator = Pattern.quote(separator);
        SpecialInputData.setSpecialInputDataSeparator(separator);
        this.isFormattedResult = this.configuration.getBooleanValueByPropertyName(
                ConfigurationPropertiesNames.IS_FORMATTED_RESULT.getPropertyName());
        this.objectMapper = objectMapper;
    }

    public List<Map<String, Object>> generateData() throws IOException {
        logger.debug("generateData");
        logger.debug("The application is ran under {} path", JAR_START_DIR);
        String str = Files.readString(JAR_START_DIR.resolve(getFileName()));
        Object parsedJson = objectMapper.readValue(str, Object.class);
        if (parsedJson instanceof List<?> list) {
            if (list.isEmpty()) {
                return this.result;
            }
            Object object = list.get(0);

            if (object instanceof String string) {
                List<String> listOfStrings = generateListOfStringsBasedOnRegexMap(list, this.mapOfRegExs, string, string);
                this.isArrayPassed = true;
                if (!listOfStrings.isEmpty()) {
                    this.result.add(Map.of(DEFAULT_KEY_NAME_FOR_ONLY_ARRAY_OF_STRINGS, listOfStrings));
                }
            } else if (object instanceof Map parsedObjectMap) {
                int iterationNumber = getIterationNumberFromParsedObject(parsedObjectMap);
                AtomicInteger id = new AtomicInteger(1);
                for (int i = 0; i < iterationNumber; i++) {
                    Map<String, Object> generatedValues = generateObjectBasedOnRegexMap(parsedObjectMap, this.mapOfRegExs, id);
                    if (!generatedValues.isEmpty()) {
                        this.result.add(generatedValues);
                    }
                }
            }

        } else if (parsedJson instanceof Map<?, ?> map) {
            this.isObjectPassed = true;
            Map<String, Object> generatedValues = generateObjectBasedOnRegexMap((Map<String, Object>) map, this.mapOfRegExs, null);
            this.result.add(generatedValues);
        }
        return this.result;
    }

    public void createResult() throws IOException {
        logger.debug("createResult");
        String fileName = getFileName();
        fileName = fileName.replace(".json", "_result.json");
        File file = JAR_START_DIR.resolve(fileName).toFile();
        if (file.exists()) {
            int lastDotIndex = file.getPath().lastIndexOf(StringSeparator.DOT);
            String pathWithoutExtension = file.getPath().substring(0, lastDotIndex);
            file = new File(generateNewResultFilename(pathWithoutExtension));
        }

        Object resultToWrite;
        if (this.isArrayPassed) {
            resultToWrite = this.result.get(0).get(DEFAULT_KEY_NAME_FOR_ONLY_ARRAY_OF_STRINGS);
        } else if (this.isObjectPassed) {
            resultToWrite = this.result.get(0);
        } else {
            resultToWrite = this.result;
        }

        if (this.isFormattedResult) {
            this.objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, resultToWrite);
        } else {
            this.objectMapper.writeValue(file, resultToWrite);
        }
        logger.info("The result file has been created under path {}", JAR_START_DIR.resolve(file.getName()));
    }

    private String generateNewResultFilename(String currentNameWithoutExtension) {
        logger.debug("generateNewResultFilename");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String format = simpleDateFormat.format(new Date());
        return currentNameWithoutExtension.concat(StringSeparator.UNDERSCORE.concat(format).concat(".json"));
    }

    private Map<String, Object> generateObjectBasedOnRegexMap(
            Map<String, Object> parsedObjectMap, Map<String, Object> regexMap, AtomicInteger id)
            throws JsonProcessingException {
        logger.debug("generateObjectBasedOnRegexMap");
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
        logger.debug("isSpecialInputData");
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
        logger.debug("getFileName");
        return this.configuration.getStringValueByPropertyName(ConfigurationPropertiesNames.JSON_FILE_NAME.getPropertyName());
    }

    private int getIterationNumberFromParsedList(List<?> list) {
        logger.debug("getIterationNumberFromParsedList");
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
        logger.debug("getIterationNumberFromParsedObject");
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
        logger.debug("generateListOfStringsBasedOnRegexMap");
        List<String> arr = new ArrayList<>();
        RgxGen rgxGen = getRgxGenFromRegexMap(regexMap, key, value);
        int numberOfElements = getIterationNumberFromParsedList(list);
        for (int i = 0; i < numberOfElements; i++) {
            arr.add(rgxGen.generate());
        }
        return arr;
    }

    private RgxGen getRgxGenFromRegexMap(Map<String, Object> regexMap, String key, String value) {
        logger.info("getRgxGenFromRegexMap");
        RgxGen rgxGen = (RgxGen) regexMap.get(key);
        if (rgxGen == null) {
            rgxGen = new RgxGen(value);
            regexMap.put(key, rgxGen);
        }
        return rgxGen;
    }

    private Map<String, Object> getInnerRegexMapFromRegexMap(Map<String, Object> regexMap, String key) {
        logger.debug("getInnerRegexMapFromRegexMap");
        Map<String, Object> innerRegexMap = (Map<String, Object>) regexMap.get(key);
        if (innerRegexMap == null) {
            innerRegexMap = new HashMap<>();
            regexMap.put(key, innerRegexMap);
        }
        return innerRegexMap;
    }

    private List<Map<String, Object>> generateListOfObjectsBasedOnRegexMap(
            Map<String, Object> regexMap, Map<String, Object> parsedObject, String key) throws JsonProcessingException {
        logger.debug("generateListOfObjectsBasedOnRegexMap");
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
        logger.debug("getDefaultIterationNumberFromConfiguration");
        return this.configuration.getIntegerValueByPropertyName(ConfigurationPropertiesNames.DEFAULT_ITERATION_NUMBER.getPropertyName());
    }

    private List<String> getListOfAvailableInputData() {
        logger.debug("getListOfAvailableInputData");
        return Arrays.stream(SpecialInputData.values())
                .map(SpecialInputData::name)
                .toList();
    }

    private boolean handleSpecialInput(String key, String rawValue, Map<String, Object> result, AtomicInteger id)
            throws JsonProcessingException {
        logger.debug("handleSpecialInput");
        boolean isSpecialInputData = isSpecialInputData(rawValue);
        if (!isSpecialInputData) {
            return false;
        }
        SpecialInputData specialInputData;
        try {
            String specialInputName = rawValue.split(specialInputDateSeparator, 2)[0].toUpperCase();
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
