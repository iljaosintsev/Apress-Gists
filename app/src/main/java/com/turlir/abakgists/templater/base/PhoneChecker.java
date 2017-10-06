package com.turlir.abakgists.templater.base;

class PhoneChecker extends MinLimit {

    PhoneChecker() {
        super(18);
    }

    @Override
    public String error() {
        return "Номер должен быть в формате +7 (123) 456-78-98";
    }
}
