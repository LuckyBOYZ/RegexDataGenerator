package com.lukaszsuma.regexdatagenerator.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum SpecialInputData {

    NAME(List.of("startAt", "female")) {
        @Override
        public Function<String, Optional<String>> generateData() {
            return generateNameOrSurname(true);
        }
    },
    SURNAME(List.of("startAt", "female")) {
        @Override
        public Function<String, Optional<String>> generateData() {
            return generateNameOrSurname(false);
        }
    },
    PESEL(List.of("bornAfter2000", "female", "onlyAdults")) {
        @Override
        public Function<String, Optional<String>> generateData() {
            return (rawValue) -> {
                String[] conditions = EMPTY_ARRAY;
                if (rawValue.contains(StringSeparator.PIPE)) {
                    conditions = getConditionsFromRawValue(rawValue);
                    if (conditions.length == 0) {
                        return Optional.empty();
                    }
                }
                Map<String, String> mapOfPassedParams = getMapOfParamsFromConditions(conditions, PESEL.conditions);
                boolean bornAfter2000 = Boolean.parseBoolean(mapOfPassedParams.get("bornAfter2000"));
                boolean female = Boolean.parseBoolean(mapOfPassedParams.get("female"));
                boolean onlyAdults = Boolean.parseBoolean(mapOfPassedParams.get("onlyAdults"));
                return Optional.of(PESELUtils.generateRandomPESEL(bornAfter2000, female, onlyAdults));
            };
        }
    },
    IBAN(List.of("country", "bankName", "formatted", "withLetters")) {
        @Override
        public Function<String, Optional<String>> generateData() {
            return (rawValue) -> {
                String[] conditions = EMPTY_ARRAY;
                if (rawValue.contains(StringSeparator.PIPE)) {
                    conditions = getConditionsFromRawValue(rawValue);
                    if (conditions.length == 0) {
                        return Optional.empty();
                    }
                }
                Map<String, String> mapOfPassedParams = getMapOfParamsFromConditions(conditions, IBAN.conditions);
                String country = "PL";
                String bankName = mapOfPassedParams.get("bankName");
                boolean formatted = Boolean.parseBoolean(mapOfPassedParams.get("formatted"));
                boolean withLetters = Boolean.parseBoolean(mapOfPassedParams.get("withLetters"));
                int defaultIndexForBankName = RANDOM.nextInt(0, PolandBankId.values().length);
                return Optional.of(BankAccountNumberUtils.generateBankAccountNumber(country, bankName, formatted,
                        withLetters, defaultIndexForBankName));
            };
        }
    },
    ID(Collections.emptyList()) {
        @Override
        public Function<String, Optional<String>> generateData() {
            return (rawVal) -> Optional.of(StringSeparator.EMPTY_STRING);
        }
    },
    POSTCODE(Collections.emptyList()) {
        @Override
        public Function<String, Optional<String>> generateData() {
            return (rawValue) -> getValueFromAddress(rawValue, POSTCODE);
        }
    },
    STREET(Collections.emptyList()) {
        @Override
        public Function<String, Optional<String>> generateData() {
            return (rawValue) -> getValueFromAddress(rawValue, STREET);
        }
    },
    CITY(List.of("startAt")) {
        @Override
        public Function<String, Optional<String>> generateData() {
            return (rawValue) -> getValueFromAddress(rawValue, CITY);
        }
    },
    VOIVODESHIP(Collections.emptyList()) {
        @Override
        public Function<String, Optional<String>> generateData() {
            return (rawValue) -> getValueFromAddress(rawValue, VOIVODESHIP);
        }
    },
    COUNTY(Collections.emptyList()) {
        @Override
        public Function<String, Optional<String>> generateData() {
            return (rawValue) -> getValueFromAddress(rawValue, VOIVODESHIP);
        }
    },
    ADDRESS(Arrays.asList("cityPropName", "streetPropName", "postcodePropName", "voivodeshipPropName", "countyPropName")) {
        @Override
        public Function<String, Optional<String>> generateData() {
            return (rawValue) -> getValueFromAddress(rawValue, ADDRESS);
        }
    };

    private static final Random RANDOM = new Random();
    private static final String[] EMPTY_ARRAY = new String[]{};
    private static final String DEFAULT_MAN_NAME = "Jan";
    private static final String DEFAULT_WOMAN_NAME = "Janina";
    private static final String DEFAULT_MAN_SURNAME = "Kowalski";
    private static final String DEFAULT_WOMAN_SURNAME = "Kowalska";
    private static final String DEFAULT_ADDRESS_ROW = "00-001;Świętokrzyska 31/33;Warszawa;Mazowieckie;Warszawa";
    private final List<String> conditions;

    SpecialInputData(List<String> conditions) {
        this.conditions = conditions;
    }

    public abstract Function<String, Optional<String>> generateData();

    private static Function<String, Optional<String>> generateNameOrSurname(boolean isName) {
        return (rawValue) -> {
            String[] conditions = EMPTY_ARRAY;
            if (rawValue.contains(StringSeparator.PIPE)) {
                conditions = getConditionsFromRawValue(rawValue);
                if (conditions.length == 0) {
                    return Optional.empty();
                }
            }
            SpecialInputData specialInputData = isName ? NAME : SURNAME;
            Map<String, String> mapOfPassedParams = getMapOfParamsFromConditions(conditions, specialInputData.conditions);
            boolean isFemale = Boolean.parseBoolean(mapOfPassedParams.get("female"));
            String path = isName ? getPathBySpecialInputType(NAME) : getPathForSurnameSpecialInputData(isFemale);
            try (Stream<String> stream = Files.lines(Path.of(path))) {
                return Optional.of(stream
                        .filter(el -> !el.isBlank())
                        .filter(el -> {
                            String start = mapOfPassedParams.get("startAt");
                            return start == null || el.startsWith(start.toUpperCase());
                        })
                        .filter(el -> !isName || isFemale == el.endsWith("a"))
                        .collect(Collectors.collectingAndThen(
                                Collectors.toCollection(ArrayList::new),
                                list -> {
                                    if (list.isEmpty()) {
                                        return getDefaultNameOrSurnameByParams(isName, isFemale);
                                    }
                                    Collections.shuffle(list);
                                    return list.get(RANDOM.nextInt(0, list.size()));
                                }
                        )));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private static Map<String, String> getMapOfParamsFromConditions(String[] conditionsFromUser, List<String> conditionsList) {
        return Stream.of(conditionsFromUser)
                .map(el -> el.split(StringSeparator.EQUALS))
                .collect(Collectors.toMap(el -> el[0], el -> el[1], (oldOne, newOne) -> oldOne))
                .entrySet()
                .stream()
                .filter(entry -> conditionsList.contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static String[] getConditionsFromRawValue(String rawValue) {
        String[] split = rawValue.split(StringSeparator.PIPE_REGEX, 2);
        String[] result = split[1].split(StringSeparator.COMMA);
        for (String el : result) {
            if (!el.contains(StringSeparator.EQUALS) || el.endsWith(StringSeparator.EQUALS)) {
                return EMPTY_ARRAY;
            }
        }
        return result;
    }

    private static Optional<String> getValueFromAddress(String rawValue, SpecialInputData specialInputData) {
        String[] conditions = EMPTY_ARRAY;
        if (rawValue.contains(StringSeparator.PIPE)) {
            conditions = getConditionsFromRawValue(rawValue);
            if (conditions.length == 0) {
                return Optional.empty();
            }
        }
        Map<String, String> mapOfPassedParams = getMapOfParamsFromConditions(conditions, specialInputData.conditions);
        String path = getPathBySpecialInputType(specialInputData);
        try (Stream<String> stream = Files.lines(Path.of(path))) {
            String randomAddressRow;
            if (specialInputData == CITY) {
                randomAddressRow = stream
                        .filter(row -> !row.isBlank())
                        .filter(row -> {
                            String start = mapOfPassedParams.get("startAt");
                            String cityFromRow = row
                                    .split(StringSeparator.SEMICOLON)[getIndexOfAddressElement(specialInputData)];
                            return start == null || cityFromRow.startsWith(start.toUpperCase());
                        })
                        .collect(Collectors.collectingAndThen(
                                Collectors.toCollection(ArrayList::new),
                                list -> {
                                    if (list.isEmpty()) {
                                        return DEFAULT_ADDRESS_ROW;
                                    }
                                    Collections.shuffle(list);
                                    return list.get(RANDOM.nextInt(0, list.size()));
                                }
                        ));
            } else {
                randomAddressRow = stream
                        .collect(Collectors.collectingAndThen(
                                Collectors.toCollection(ArrayList::new),
                                list -> {
                                    Collections.shuffle(list);
                                    return list.get(RANDOM.nextInt(0, list.size()));
                                }
                        ));
            }
            if (specialInputData == COUNTY || specialInputData == VOIVODESHIP) {
                return Optional.of(randomAddressRow);
            }

            String[] split = randomAddressRow.split(StringSeparator.SEMICOLON);
            int index = getIndexOfAddressElement(specialInputData);
            if (index < 0) {
                return Optional.of(generateFullAddressString(randomAddressRow, mapOfPassedParams));
            } else {
                return Optional.of(split[index]);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getPathBySpecialInputType(SpecialInputData specialInputData) {
        return switch (specialInputData) {
            case VOIVODESHIP -> "src/main/resources/voivodeships.csv";
            case COUNTY -> "src/main/resources/counties.csv";
            case ADDRESS, POSTCODE, STREET, CITY -> "src/main/resources/addresses.csv";
            case NAME -> "src/main/resources/names.csv";
            default -> throw new RuntimeException(String.format("No file for '%s' special input",
                    specialInputData.name()));
        };
    }

    private static String getPathForSurnameSpecialInputData(boolean isFemale) {
        return isFemale ? "src/main/resources/womenSurnames.csv" : "src/main/resources/menSurnames.csv";
    }

    private static String generateFullAddressString(String randomAddressRow, Map<String, String> params) {
        // POSTCODE 0, STREET 1, CITY 2, VOIVODESHIP 3, COUNTY 4
        String[] split = randomAddressRow.split(StringSeparator.SEMICOLON);
        ArrayList<String> valuesForAddressObject = new ArrayList<>(10);
        ADDRESS.conditions.forEach(cond -> {
            String propName = params.get(cond);
            if (propName == null || propName.isBlank()) {
                propName = getDefaultPropertyNameForAddress(cond);
            }
            SpecialInputData sid = getSpecialInputDataForAddressByNameInProp(cond);
            String value = split[getIndexOfAddressElement(sid)];
            valuesForAddressObject.add(propName);
            valuesForAddressObject.add(value);
        });
        return """
                {
                    "%s": "%s",
                    "%s": "%s",
                    "%s": "%s",
                    "%s": "%s",
                    "%s": "%s"
                }
                """.formatted(valuesForAddressObject.get(0),
                valuesForAddressObject.get(1),
                valuesForAddressObject.get(2),
                valuesForAddressObject.get(3),
                valuesForAddressObject.get(4),
                valuesForAddressObject.get(5),
                valuesForAddressObject.get(6),
                valuesForAddressObject.get(7),
                valuesForAddressObject.get(8),
                valuesForAddressObject.get(9));
    }

    private static String getDefaultPropertyNameForAddress(String propName) {
        return switch (propName) {
            case "cityPropName" -> "city";
            case "streetPropName" -> "street";
            case "postcodePropName" -> "postcode";
            case "voivodeshipPropName" -> "voivodeship";
            case "countyPropName" -> "county";
            default -> throw new NoSuchElementException(String.format("No value for address property '%s'", propName));
        };
    }

    private static int getIndexOfAddressElement(SpecialInputData specialInputData) {
        return switch (specialInputData) {
            case POSTCODE -> 0;
            case STREET -> 1;
            case CITY -> 2;
            case VOIVODESHIP -> 3;
            case COUNTY -> 4;
            default -> -1;
        };
    }

    private static SpecialInputData getSpecialInputDataForAddressByNameInProp(String propName) {
        if (propName.startsWith(POSTCODE.name().toLowerCase())) {
            return POSTCODE;
        } else if (propName.startsWith(STREET.name().toLowerCase())) {
            return STREET;
        } else if (propName.startsWith(CITY.name().toLowerCase())) {
            return CITY;
        } else if (propName.startsWith(VOIVODESHIP.name().toLowerCase())) {
            return VOIVODESHIP;
        } else if (propName.startsWith(COUNTY.name().toLowerCase())) {
            return COUNTY;
        } else {
            throw new RuntimeException(String.format("No special input for '%s' property", propName));
        }
    }

    private static String getDefaultNameOrSurnameByParams(boolean isName, boolean isFemale) {
        if (isName) {
            return isFemale ? DEFAULT_WOMAN_NAME : DEFAULT_MAN_NAME;
        } else {
            return isFemale ? DEFAULT_WOMAN_SURNAME : DEFAULT_MAN_SURNAME;
        }
    }
}
