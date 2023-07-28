package com.lukaszsuma.regexdatagenerator;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;
import java.util.Map;

@SuppressWarnings("UnusedReturnValue")
public class RegexDataGenerator {

    private final DataGenerator dataGenerator;

    public RegexDataGenerator(String... args) {
        this.dataGenerator = new DataGenerator(new Configuration(args), new ObjectMapper());
    }

    public static void main(String[] args) {
        RegexDataGenerator regexDataGenerator = new RegexDataGenerator(args);
        try {
            regexDataGenerator.generateData();
            regexDataGenerator.createResult();
        } catch (Exception ex) {
            if (ex instanceof NoSuchFileException obj) {
                System.err.printf("No such file %s", obj.getFile());
            } else if (ex instanceof JsonParseException obj) {
                System.err.printf("Cannot parse json file: %s", obj.getOriginalMessage());
            }
            ex.printStackTrace();
        }
    }

    public List<Map<String, Object>> generateData() throws IOException {
        return this.dataGenerator.generateData();
    }

    public void createResult() throws IOException {
        this.dataGenerator.createResult();
    }

}


