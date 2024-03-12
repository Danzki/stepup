package com.danzki;

import com.danzki.exception.InvalidSetup;
import com.danzki.services.FileListener;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;

public class FileListenerTest {
    @Test
    @DisplayName("FileListener.getValidExt test - with leading dot")
    void testGetValidExtWithLeadingDot() {
        String ext = ".log";
        FileListener fl = new FileListener();
        fl.setExt(ext);
        String actual = fl.getExt();
        Assertions.assertEquals(".log", actual);
    }

    @Test
    @DisplayName("FileListener.getValidExt test - withOUT leading dot")
    void testGetValidExtWithOUTLeadingDot() {
        String ext = "log";
        FileListener fl = new FileListener();
        fl.setExt(ext);
        String actual = fl.getExt();
        Assertions.assertEquals(".log", actual);
    }

    @Test
    @DisplayName("FileListener.getValidExt test: long ext with leading dot")
    void testGetValidExtLongWithLeadingDot() {
        String ext = ".properties";
        FileListener fl = new FileListener();
        fl.setExt(ext);
        String actual = fl.getExt();
        Assertions.assertEquals(".properties", actual);
    }

    @Test
    @DisplayName("FileListener.getValidExt test: long ext withOUT leading dot")
    void testGetValidExtLongWithOUTLeadingDot() {
        String ext = "properties";
        FileListener fl = new FileListener();
        fl.setExt(ext);
        String actual = fl.getExt();
        Assertions.assertEquals(".properties", actual);
    }

    @Test
    @DisplayName("FileListener.getValidExt test: invalid extention")
    void testGetValidExtInvalidExt() {
        String ext = "This is Invalid Extention";
        FileListener fl = new FileListener();

        Exception exception = Assertions.assertThrows(InvalidSetup.class, () -> {fl.setExt(ext);});
        String expectedMessage = "Invalid ext setting. Please check your setup file.";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
    }
}
