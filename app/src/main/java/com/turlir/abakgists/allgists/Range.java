package com.turlir.abakgists.allgists;

public class Range {

    private final int PAGE_SIZE = 15;

    public final int absStart, absStop;
    public final int page;

    public Range(int start, int stop) {
        if (start >= stop) throw new IllegalArgumentException();
        absStart = start;
        absStop = stop;
        page = (int) Math.ceil(absStop / (float) PAGE_SIZE);
    }

    public Range(int relStart, int relStop, int page) {
        int offset = (int) ((page - 1) * (float) PAGE_SIZE);
        absStart = offset + relStart;
        absStop = absStart + relStop;
        this.page = page;
    }

    public int[] specRequiredItems(int c) {
        int required = count() - c;
        if (required < 1) throw new IllegalArgumentException();
        int at = absStop - required;
        if (at % required == 0) {
            int page = (at / required) + 1;
            return new int[] {page, required};
        } else {
            double floor = Math.floor(at / (float) required);
            int page = (int) floor + 1;
            return new int[] {page, required};
        }
    }

    public int count() {
        return absStop - absStart;
    }

    public boolean isWhole() {
        return absStop % PAGE_SIZE == 0;
    }

    public boolean isFull(int count) {
        return count == count();
    }

    public Range next() {
        return new Range(absStart + PAGE_SIZE, absStop + PAGE_SIZE);
    }

    public Range prev() {
        int start = Math.max(0, absStart - PAGE_SIZE);
        return new Range(start, absStop - PAGE_SIZE);
    }

    public boolean hasNext() {
        return true;
    }

    public boolean hasPrevious() {
        return absStart - PAGE_SIZE >= 0;
    }
}
