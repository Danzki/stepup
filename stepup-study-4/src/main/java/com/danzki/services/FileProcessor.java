package com.danzki.services;

import com.danzki.model.FileData;
import com.danzki.model.Login;
import com.danzki.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileProcessor {
    private static Logger logger = LoggerFactory.getLogger(FileProcessor.class);
    private File file;
    private List<String> fileRows;
    private String delim;
    private String format;
    private UserService userService;
    private LoginsService loginsService;
    private LogFileService logFileService;
    public FileProcessor(File file) {
        this.file = file;
        this.fileRows = getFileRows();
    }

    public void setDelim(String delim) {
        this.delim = delim;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @Autowired
    public void setLoginsService(LoginsService loginsService) {
        this.loginsService = loginsService;
    }
    @Autowired
    public void setLogFileService(LogFileService logFileService) {
        this.logFileService = logFileService;
    }

    private List<String> getFileRows() {
        List<String> fileRows = new ArrayList<>();
        try (var lineReader = new BufferedReader(new FileReader(file))) {
            String lineText = null;
            int count = 0;
//            lineReader.readLine();  //skip header
            while ((lineText = lineReader.readLine()) != null) {
                fileRows.add(lineText);
                count++;
            }
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return fileRows;
    }

    public void process() {
        var fileData = getFileData(fileRows);
        fileData.stream()
                .filter((row) -> row.isValid() == true)
                .forEach(this::saveData);
        fileData.stream()
                .filter((row) -> row.isValid() == false)
                .forEach(this::saveLog);
    }

    private List<FileData> getFileData(List<String> fileRows) {
        List<FileData> fileData = new ArrayList<>();
        for(String fileRow : fileRows) {
            String data[] = fileRow.split(delim);
            var fileDataObj = new FileData
                    .FileDataBuilder()
                    .login(data[0])
                    .familyName(data[1])
                    .firstName(data[2])
                    .surName(data[3])
                    .entranceDateString(data[4])
                    .application(data[5])
                    .format(format)
                    .build();
            fileData.add(fileDataObj);
        }
        return fileData;
    }

    private void saveData(FileData fileData) {
        var user = new User();
        if (userService.findByUsername(fileData.getLogin()) == null) {
            user.setUsername(fileData.getLogin());
            user.setFio(fileData.getFamilyName() + " " + fileData.getFirstName() + " " + fileData.getSurName());
            userService.save(user);
        } else {
            user = userService.findByUsername(fileData.getLogin());
        }

        var login = new Login();
        login.setApplication(fileData.getApplication());
        login.setAccessDate(fileData.getEntranceDate());
        login.setUser(user);
        loginsService.save(login);
    }

    private void saveLog(FileData fileData) {
        logFileService.save(fileData);
    }

    public boolean isRightExtention(File file, String ext) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return false; // empty extension
        }
        if (!ext.equals(name.substring(lastIndexOf))) {
            return false;
        }

        return true;
    }
}
