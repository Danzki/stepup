package com.danzki;

import com.danzki.model.FileData;
import com.danzki.model.Login;
import com.danzki.model.User;
import com.danzki.repo.LoginRepo;
import com.danzki.repo.UsersRepo;
import com.danzki.services.FileProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FileProcessorTest {
    @Mock UsersRepo userRepo;
    @Mock LoginRepo loginRepo;
    User actualUser;
    Login actualLogin;
    String format;
    FileData fileData;
    String login;
    String familyName;
    String firstName;
    String surName;
    Date accessDate;
    String path;
    String delim;

    @BeforeEach
    void initVariables() {
        path = "in/testing_data.log";
        delim = " ";
        format = "yyyy-MM-dd";
        login = "ivanovii";
        familyName = "Иванов";
        firstName = "Иван";
        surName = "Иванович";
        var dateInString = "2024-02-13";
        try {
            var formatter = new SimpleDateFormat(format, Locale.ENGLISH);
            accessDate = formatter.parse(dateInString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        fileData = new FileData.FileDataBuilder()
                .login(login)
                .familyName(familyName)
                .firstName(firstName)
                .surName(surName)
                .entranceDateString(dateInString)
                .application("game")
                .format(format)
                .build();

        actualUser = new User();
        actualUser.setId(1L);
        actualUser.setUsername(login);
        actualUser.setFio(familyName + " " + firstName + " " + surName);

        actualLogin = new Login();
        actualLogin.setId(1L);
        actualLogin.setUser(actualUser);
        actualLogin.setApplication("other game");
        actualLogin.setAccessDate(accessDate);

    }

    @Test
    @ExtendWith(MockitoExtension.class)
    @MockitoSettings(strictness = Strictness.LENIENT)
    void processUserTest() {
        when(userRepo.save(any(User.class))).thenReturn(actualUser);

        var file = new File("");
        var fileProcessor = new FileProcessor(file);
        fileProcessor.process();

        Assertions.assertEquals(actualUser.getId(), 1L);
        Assertions.assertEquals(actualUser.getUsername(), login);
        Assertions.assertEquals(actualUser.getFio(), familyName + " " + firstName + " " + surName);

    }

    @Test
    @ExtendWith(MockitoExtension.class)
    @MockitoSettings(strictness = Strictness.LENIENT)
    void processLoginTest() {
        when(loginRepo.save(any(Login.class))).thenReturn(actualLogin);

        var file = new File("");
        var fileProcessor = new FileProcessor(file);
        fileProcessor.process();

        Assertions.assertEquals(actualLogin.getId(), 1L);
        Assertions.assertEquals(actualLogin.getApplication(), "other game");
        Assertions.assertEquals(actualLogin.getAccessDate(), accessDate);

    }

}
