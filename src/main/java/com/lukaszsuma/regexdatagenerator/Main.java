package com.lukaszsuma.regexdatagenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lukaszsuma.regexdatagenerator.config.Configuration;

import java.io.IOException;

public class Main {

    private final RegexDataGenerator dataGenerator;

    public static void main(String[] args) throws IOException {
        Main main = new Main(args);
        main.run();
    }

    public Main(String... args) {
        this.dataGenerator = new RegexDataGenerator(new Configuration(args), new ObjectMapper());
    }

    public void run() throws IOException {
        this.dataGenerator.generateData();
        this.dataGenerator.createResult();
    }

}


