package com.danzki.services;

public enum ApplicationType {
    WEB("web"),
    MOBILE("mobile"),
    OTHER("other");

    private String name;

    ApplicationType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
