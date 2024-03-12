package com.danzki.services;

public class ValidatorApplication implements Validatable {
    private String original;
    private String value;

    public ValidatorApplication(String original) {
        this.original = original;
        this.value = getValidApplicationType(this.original);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean validate() {
        if (!getValidApplicationType(this.original).equals(this.value)) {
            return false;
        }
        return true;
    }

    private String getValidApplicationType(String original) {
        String value;
        if (original.toLowerCase().trim().equals(ApplicationType.WEB.toString())) {
            return ApplicationType.WEB.toString();
        }
        if (original.toLowerCase().trim().equals(ApplicationType.MOBILE.toString()) ) {
            return ApplicationType.MOBILE.toString();
        }
        value = ApplicationType.OTHER.toString() + " " + original;

        return value;
    }
}
