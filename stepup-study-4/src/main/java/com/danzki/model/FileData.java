package com.danzki.model;

import com.danzki.services.ValidatorApplication;
import com.danzki.services.ValidatorDate;
import com.danzki.services.ValidatorName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class FileData {
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

        boolean resultName;
        resultName = validateName(fileDataBuilder.familyName, fileDataBuilder.firstName, fileDataBuilder.surName);
        if (resultName) {
            this.familyName = new ValidatorName(fileDataBuilder.familyName).getValue();
            this.firstName = new ValidatorName(fileDataBuilder.firstName).getValue();
            this.surName = new ValidatorName(fileDataBuilder.surName).getValue();
        }

        boolean resultDate;
        resultDate = validateDate(fileDataBuilder.entranceDateString, fileDataBuilder.format);
        if (resultDate) {
            this.entranceDate = new ValidatorDate(fileDataBuilder.entranceDateString, fileDataBuilder.format).getValue();
        }

        boolean resultApplication;
        resultApplication = validateApplication(fileDataBuilder.application);
        if (resultApplication) {
            this.application = new ValidatorApplication(fileDataBuilder.application).getValue();
        }
        this.isValid = resultName && resultDate && resultApplication;
    }

    private boolean validateName(String familyName, String firstName, String surName) {
        return new ValidatorName(familyName).validate() &&
                new ValidatorName(firstName).validate() &&
                new ValidatorName(surName).validate();
    }

    private boolean validateDate(String entranceDateString, String format) {
        return new ValidatorDate(entranceDateString, format).validate();
    }

    private boolean validateApplication(String application) {
        return new ValidatorApplication(application).validate();
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
