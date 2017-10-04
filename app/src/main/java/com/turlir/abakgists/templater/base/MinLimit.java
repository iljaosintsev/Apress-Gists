package com.turlir.abakgists.templater.base;

public class MinLimit implements Checker<String> {

    private final int minimum;

    public MinLimit(int minimum) {
        this.minimum = minimum - 1;
    }

    @Override
    public boolean check(String actual) {
        return actual != null && actual.trim().length() > minimum;
    }

    @Override
    public String error() {
        return "Minimum " + (minimum + 1) + " symbol(s)";
    }
}
