package com.turlir.abakgists.templater.base;

public class Group {

    public final int number;
    private int end;

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

    public void setEnd(int value) {
        if (value != -1 &&
            end == -1 &&
            value > mStart) {
            end = value;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public boolean does(int index) {
        return index <= end;
    }

}
