package com.danzki.config;

import com.danzki.services.FileListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.devtools.filewatch.FileSystemWatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.io.File;
import java.time.Duration;

@Configuration
public class FileConfig {
    private static Logger logger = LoggerFactory.getLogger(FileConfig.class);
    @Value("${filescanner.scanner.directory}")
    private String path;

    @Value("${filescanner.scanner.daemon}")
    private boolean daemon;

    @Value("${filescanner.scanner.pollInterval}")
    private Long pollInterval;

    @Value("${filescanner.scanner.quietPeriod}")
    private Long quietPeriod;

    @Value("${filescanner.file.ext}")
    private String ext;

    @Value("${filescanner.file.delim}")
    private String delim;

    @Value("${filescanner.file.dateFormat}")
    private String dateFormat;

    @Autowired
    public FileFactory fileFactory;

    public String getPath() {
        return path;
    }

    public FileConfig() {
        logger.info("Create FileConfig...");
    }

    @Bean
    public FileSystemWatcher fileSystemWatcher() {
        FileSystemWatcher fileSystemWatcher = new FileSystemWatcher(daemon, Duration.ofMillis(pollInterval), Duration.ofMillis(quietPeriod));
        File parentFolder = new File(new File("").getAbsolutePath());
        File logsFolder = new File(parentFolder, path);
        fileSystemWatcher.addSourceDirectory(new File(logsFolder.getAbsolutePath()));
        var fl = new FileListener();
        fl.setDelim(delim);
        fl.setExt(ext);
        fl.setFormat(dateFormat);
        fl.setFileFactory(fileFactory);
        fileSystemWatcher.addListener(fl);
        fileSystemWatcher.start();
        return fileSystemWatcher;
    }

    @Bean
    public FileFactory getFileFactory() {
        return new FileFactory();
    }

    @PreDestroy
    public void onDestroy() throws Exception {
        fileSystemWatcher().stop();
    }
}
