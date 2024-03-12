package com.danzki.config;

import com.danzki.services.FileProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.File;

@Configuration
public class FileFactory {
    private static final Logger logger = LoggerFactory.getLogger(FileFactory.class);

    public FileFactory() {
    }

    @Bean
    @Scope(value = "prototype")
    public FileProcessor fileDataSaver(File file) {
        logger.info("Create new FileProcessor");
        return new FileProcessor(file);
    }
}
