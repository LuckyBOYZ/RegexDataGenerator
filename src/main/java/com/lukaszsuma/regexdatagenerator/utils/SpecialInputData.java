package com.lukaszsuma.regexdatagenerator.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum SpecialInputData {
    NAME(List.of("start", "female"), null) {
        @Override
        public Function<String, Optional<String>> generateData() {
            return generateNameOrSurname(true);
        }
    },
    SURNAME(List.of("start", "female"), null) {
        @Override
        public Function<String, Optional<String>> generateData() {
            return generateNameOrSurname(false);
        }
    },
    PESEL(List.of("bornAfter2000", "female", "onlyAdults"), null) {
        @Override
        public Function<String, Optional<String>> generateData() {
            return (rawValue) -> {
                String[] split = rawValue.split(StringSeparator.PIPE);
                if (split.length < 2) {
                    return Optional.empty();
                }
                String[] conditions = split[1].split(StringSeparator.COMMA);
                Map<String, String> mapOfPassedParams = getMapOfParamsFromConditions(conditions, PESEL.conditions);
                boolean bornAfter2000 = Boolean.parseBoolean(mapOfPassedParams.get("bornAfter2000"));
                boolean female = Boolean.parseBoolean(mapOfPassedParams.get("female"));
                boolean onlyAdults = Boolean.parseBoolean(mapOfPassedParams.get("onlyAdults"));
                return Optional.of(PESELUtils.generateRandomPESEL(bornAfter2000, female, onlyAdults));
            };
        }
    },
    IBAN(List.of("country", "bankName", "formatted", "withLetters"), null) {
        @Override
        public Function<String, Optional<String>> generateData() {
            return (rawValue) -> {
                String[] split = rawValue.split(StringSeparator.PIPE);
                if (split.length < 2) {
                    return Optional.empty();
                }
                String[] conditions = split[1].split(StringSeparator.COMMA);
                Map<String, String> mapOfPassedParams = getMapOfParamsFromConditions(conditions, IBAN.conditions);
                String country = "PL";
                String bankName = mapOfPassedParams.get("bankName");
                boolean formatted = Boolean.parseBoolean(mapOfPassedParams.get("formatted"));
                boolean withLetters = Boolean.parseBoolean(mapOfPassedParams.get("withLetters"));
                return Optional.of(BankAccountNumberUtils.generateBankAccountNumber(country, bankName, formatted, withLetters));
            };
        }
    };
//    ADDRESS(Collections.emptyList(), 0, null),
//    ID(Collections.emptyList(), 0, regex);

    private final List<String> conditions;
    private final String regex;

    SpecialInputData(List<String> conditions, String regex) {
        this.conditions = conditions;
        this.regex = regex;
    }

    public abstract Function<String, Optional<String>> generateData();

    private static Function<String, Optional<String>> generateNameOrSurname(boolean isName) {
        return (rawValue) -> {
            String[] split = rawValue.split(StringSeparator.PIPE);
            if (split.length < 2) {
                return Optional.empty();
            }
            String[] conditions = split[1].split(StringSeparator.COMMA);
            SpecialInputData specialInputData = isName ? NAME : SURNAME;
            Map<String, String> mapOfPassedParams = getMapOfParamsFromConditions(conditions, specialInputData.conditions);
            String path = isName ? "src/main/resources/names.txt" : "src/main/resources/surnames.txt";
            try (Stream<String> stream = Files.lines(Path.of(path), Charset.forName("WINDOWS-1250"))) {
                Random random = new Random(System.currentTimeMillis());
                return Optional.ofNullable(stream
                        .filter(el -> {
                            String start = mapOfPassedParams.get("start");
                            return start == null || el.startsWith(start.toUpperCase());
                        })
                        .filter(el -> {
                            String female = mapOfPassedParams.get("female");
                            boolean isFemale = Boolean.parseBoolean(female);
                            return !isFemale || el.endsWith("a");
                        })
                        .collect(Collectors.collectingAndThen(
                                Collectors.toCollection(ArrayList::new),
                                list -> {
                                    Collections.shuffle(list);
                                    return list.get(random.nextInt(0, list.size()));
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
                .collect(Collectors.toMap(el -> el[0], el -> el[1]))
                .entrySet()
                .stream()
                .filter(entry -> {
                    return conditionsList.contains(entry.getKey());
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
