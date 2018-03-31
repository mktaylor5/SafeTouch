package com.safetouch.domain;

// Enum for Mode value in database
public enum Mode {
    PERSONAL("Personal", 0),
    PARENT("Parent", 1),
    MEDICAL("Medical", 2);

    private String stringValue;
    private int intValue;
    private Mode(String toString, int value) {
        stringValue = toString;
        intValue = value;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}
