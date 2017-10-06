package com.turlir.abakgists.templater.base;

public class PhoneChecker extends MinLimit {

    public PhoneChecker() {
        super(18);
    }

    @Override
    public String error() {
        return "Номер должен быть в формате +7 (123) 456-78-98";
    }
}
