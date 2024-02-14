package com.danzki.services;

import com.danzki.annotation.LogTransformation;
import com.danzki.config.Constant;
import com.danzki.logger.SimpleLogger;
import com.danzki.model.FileData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.task.TaskSchedulingProperties;
import org.springframework.stereotype.Service;

@Service
@LogTransformation(fileName = "logfileservice.log")
public class LogFileService {
    private static Logger logger = LoggerFactory.getLogger(LogFileService.class);
    public void save(FileData fileData) {
        SimpleLogger.openLog(LogFileService.class, new Object(){}.getClass().getEnclosingMethod().getName());
        SimpleLogger.log(fileData.toString());

        String error = Constant.CN_EMPTY_DATE + "(username=" + fileData.getLogin() + ", application=" + fileData.getApplication() + ").";
        logger.info(error);

        SimpleLogger.log("return nothing");
        SimpleLogger.closeLog();
    }
}
