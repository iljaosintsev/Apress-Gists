package com.turlir.abakgists.templater.check;

public class NotEmpty implements Checker<String> {

    @Override
    public boolean check(String actual) {
        return actual != null && actual.trim().length() > 0;
    }

    @Override
    public String error() {
        return "Поле не может быть пустым";
    }
}
