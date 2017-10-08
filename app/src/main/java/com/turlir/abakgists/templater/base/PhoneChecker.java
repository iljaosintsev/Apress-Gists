package com.turlir.abakgists.templater.base;

public class PhoneChecker extends MinLimit {

    private final boolean isRequired;

    public PhoneChecker(boolean required) {
        super(18);
        isRequired = required;
    }

    @Override
    public boolean check(String actual) {
        if (actual.length() < 1) {
            return !isRequired;
        } else {
            return super.check(actual);
        }
    }

    @Override
    public String error() {
        return "Номер должен быть в формате +7 (123) 456-78-98";
    }
}
