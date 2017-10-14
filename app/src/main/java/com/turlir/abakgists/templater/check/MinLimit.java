package com.turlir.abakgists.templater.check;

public class MinLimit implements Checker {

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
        return "Минимум " + (minimum + 1) + " символ(ов)";
    }
}
