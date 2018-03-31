package com.safetouch.domain;

public enum PatternAction {
    WARNING("Earning", 0),
    EMERGENCY("Emergency", 1);

    private String stringValue;
    private int intValue;
    PatternAction(String toString, int value) {
        stringValue = toString;
        intValue = value;
    }

    public int getIntValue() {
        return intValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}
