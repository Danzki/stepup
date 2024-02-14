package com.danzki.model;

import com.danzki.services.FileListener;
import com.danzki.services.ValidatorApplication;
import com.danzki.services.ValidatorDate;
import com.danzki.services.ValidatorName;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class FileData {
    private static Logger logger = LoggerFactory.getLogger(FileData.class);

    private String login;
    private String familyName;
    private String firstName;
    private String surName;
    private String entranceDateString;
    private Date entranceDate;
    private String application;
    private boolean isValid;
    private String format;



    private FileData(FileDataBuilder fileDataBuilder) {
        this.login = fileDataBuilder.login;
        this.familyName = fileDataBuilder.familyName;
        this.firstName = fileDataBuilder.firstName;
        this.surName = fileDataBuilder.surName;
        this.entranceDateString = fileDataBuilder.entranceDateString;
        this.application = fileDataBuilder.application;
        this.format = fileDataBuilder.format;
    }

    private boolean validateName() {
        return new ValidatorName(this.familyName).validate() &&
                new ValidatorName(this.firstName).validate() &&
                new ValidatorName(this.surName).validate();
    }

    private boolean validateDate() {
        return new ValidatorDate(entranceDateString, format).validate();
    }

    private boolean validateApplication() {
        return new ValidatorApplication(this.application).validate();
    }

    public boolean validate() {
        boolean resultName;
        resultName = validateName();
        if (resultName) {
            this.familyName = new ValidatorName(this.familyName).getValue();
            this.firstName = new ValidatorName(this.firstName).getValue();
            this.surName = new ValidatorName(this.surName).getValue();
        }
        boolean resultDate;
        resultDate = validateDate();
        if (resultDate) {
            this.entranceDate = new ValidatorDate(entranceDateString, format).getValue();
        }
        boolean resultApplication;
        resultApplication = validateApplication();
        if (resultApplication) {
            this.application = new ValidatorApplication(this.application).getValue();
        }
        this.isValid = resultName && resultDate && resultApplication;
        logger.debug("validate returns " + (this.isValid?"yes":"no"));

        return resultName && resultDate && resultApplication;
    }

    @NoArgsConstructor
    public static class FileDataBuilder {
        private String login;
        private String familyName;
        private String firstName;
        private String surName;
        private String entranceDateString;
        private String application;
        private String format;

        public FileDataBuilder login(String login) {
            this.login = login;
            return this;
        }

        public FileDataBuilder familyName(String familyName) {
            this.familyName = familyName;
            return this;
        }

        public FileDataBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public FileDataBuilder surName(String surName) {
            this.surName = surName;
            return this;
        }

        public FileDataBuilder entranceDateString(String entranceDateString) {
            this.entranceDateString = entranceDateString;
            return this;
        }

        public FileDataBuilder application(String application) {
            this.application = application;
            return this;
        }

        public FileDataBuilder format(String format) {
            this.format = format;
            return this;
        }

        public FileData build() {
            return new FileData(this);
        }

    }

}
