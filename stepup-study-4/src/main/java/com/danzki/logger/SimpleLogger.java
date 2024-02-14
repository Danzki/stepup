package com.danzki.logger;

import com.danzki.annotation.LogTransformation;
import com.danzki.services.LogFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class SimpleLogger {
    private static int lineNumber = 0;
    private static String path = "logs";
    private static String fileName = "";
    private static Queue<String> logMessages = new ConcurrentLinkedQueue<>();
    private static boolean isEnable = false;
    private static String className = "";
    private static String methodName = "";

    public static void log(String message) {
        if (isEnable) {
            Date currentDate = new Date();
            logMessages.add(currentDate.toString() + " " + className + "." + methodName + " : " + message + "\n");
        }
    }

    public static void openLog(Class clazz, String initialMethodName) {
        if (clazz.isAnnotationPresent(LogTransformation.class)) {
            isEnable = true;
            LogTransformation annotation = (LogTransformation) clazz.getAnnotation(LogTransformation.class);
            path = new File("").getAbsolutePath() + File.separator + path;
            fileName = path + File.separator + annotation.fileName();
            className = clazz.getName();
            methodName = initialMethodName;
        }
    }

    public static void closeLog() {
        try (var writer = new BufferedWriter(new FileWriter(fileName, true))) {
            for (String m : logMessages) {
                writer.append(m);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        isEnable = false;
        logMessages.clear();
        fileName = "";
        path = "";
    }


}
