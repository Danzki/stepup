package com.danzki.services;

public class ValidatorName implements Validatable {
    private String original;
    private String value;

    public ValidatorName(String original) {
        this.original = original;
        this.value = getValidName(this.original);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean validate() {
        if (!getValidName(this.original).equals(this.value)) {
            return false;
        }
        return true;
    }

    private String getValidName(String original) {
        if (original == null) {
            return null;
        }
        return original.trim().toLowerCase().substring(0, 1).toUpperCase() +
                original.trim().toLowerCase().substring(1);
    }

}
