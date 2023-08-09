package com.lukaszsuma.regexdatagenerator;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lukaszsuma.regexdatagenerator.utils.StringSeparator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.LoggerConfig;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;
import java.util.Map;

@SuppressWarnings("UnusedReturnValue")
public class RegexDataGenerator {

    private static final Logger logger = LogManager.getLogger(RegexDataGenerator.class);
    private final DataGenerator dataGenerator;

    public RegexDataGenerator(String... args) {
        this.dataGenerator = new DataGenerator(new Configuration(args), new ObjectMapper());
    }

    public static void main(String[] args) {
        logger.debug("All arguments passed to the program: {}", String.join(StringSeparator.COMMA, args));
        logger.info("LOGGER LEVEL: {}", logger.getLevel());
        logger.info("===== APPLICATION IS STARTED =====");

        RegexDataGenerator regexDataGenerator = new RegexDataGenerator(args);
        try {
            regexDataGenerator.generateData();
            regexDataGenerator.createResult();
        } catch (IOException ex) {
            if (ex instanceof NoSuchFileException obj) {
                logger.error("No such file " + obj.getFile(), ex);
            } else if (ex instanceof JsonParseException obj) {
                logger.error("Cannot parse json file: " + obj.getOriginalMessage(), ex);
            }
        }
        logger.info("===== APPLICATION IS FINISHED =====");
    }

    public List<Map<String, Object>> generateData() throws IOException {
        return this.dataGenerator.generateData();
    }

    public void createResult() throws IOException {
        this.dataGenerator.createResult();
    }

}


