package com.turlir.abakgists.allgists;

public class LoadablePage {

    private static final int MAX_PAGE = 20;

    public final int number, size;

    LoadablePage(int start, int stop) {
        if (start >= stop) throw new IllegalArgumentException();
        size = stop - start;
        if (start % size != 0) throw new IllegalArgumentException();
        number = stop / size;
    }

    public boolean hasNext() {
        return number < MAX_PAGE;
    }

    public boolean hasPrevious() {
        return number > 1;
    }
}
