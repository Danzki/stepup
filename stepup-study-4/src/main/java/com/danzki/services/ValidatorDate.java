package com.danzki.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ValidatorDate implements Validatable {
    private String original;
    private Date value;
    private String format;

    public ValidatorDate(String original, String format) {
        this.original = original;
        this.format = format;
        this.value = getDateFromString();
    }

    public Date getValue() {
        return value;
    }

    @Override
    public boolean validate() {
        if (original == null || original.equals("")) {
            return false;
        }
        return true;
    }

    private Date getDateFromString() {
        if (this.original.equals("") || this.format.equals("")) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(this.format);
        try {
            return formatter.parse(this.original.trim());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
