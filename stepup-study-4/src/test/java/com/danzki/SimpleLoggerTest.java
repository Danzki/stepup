package com.danzki;

import com.danzki.annotation.LogTransformation;
import com.danzki.logger.SimpleLogger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;

@LogTransformation(fileName = "testing.log")
public class SimpleLoggerTest {

    String successString = "Successful test!!!";

    @Test
    public void simpleLoggerTest() {
        SimpleLogger.openLog(SimpleLoggerTest.class, "simpleLoggerTest");
        SimpleLogger.log(successString);
        SimpleLogger.closeLog();

        String path = new File("").getAbsolutePath() + File.separator;
        var logFile = path + new File("logs/testing.log");

        try (var reader = new BufferedReader(new FileReader(logFile))) {
            var log = reader.readLine();
            Assertions.assertTrue(log.contains(successString));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
