package com.lukaszsuma.regexdatagenerator;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lukaszsuma.regexdatagenerator.config.Configuration;

import java.io.IOException;
import java.nio.file.NoSuchFileException;

public class Main {

    private final RegexDataGenerator dataGenerator;

    public Main(String... args) {
        this.dataGenerator = new RegexDataGenerator(new Configuration(args), new ObjectMapper());
    }

    public static void main(String[] args) {
        Main main = new Main(args);
        try {
            main.generateData();
            main.createResult();
        } catch (Exception ex) {
            if (ex instanceof NoSuchFileException obj) {
                System.err.printf("No such file %s", obj.getFile());
            } else if (ex instanceof JsonParseException obj) {
                System.err.printf("Cannot parse json file: %s", obj.getOriginalMessage());
            }
            ex.printStackTrace();
        }
    }

    public void generateData() throws IOException {
        this.dataGenerator.generateData();
    }

    public void createResult() throws IOException {
        this.dataGenerator.createResult();
    }

}


