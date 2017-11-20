package com.turlir.abakgists.templater.base;

public class Group {

    public final int number;
    public int end;

    private int mStart;

    public Group(int number, int start) {
        this.number = number;
        end = -1;
        mStart = start;
    }

    public void shift() {
        mStart--;
        end--;
    }



    public boolean does(int index) {
        return index <= end;
    }

    public boolean ending() {
        return end != -1;
    }

    public boolean valid() {
        return end > mStart && diff() > 0;
    }

    public int diff() {
        return end - mStart;
    }
}
