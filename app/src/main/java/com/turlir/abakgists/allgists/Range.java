package com.turlir.abakgists.allgists;

import java.util.Objects;

public class Range {

    private final int PAGE_SIZE = 15,
            MAX_PAGE = 20;

    public final int absStart, absStop;
    public final int page;

    public Range(int start, int stop) {
        if (start >= stop) throw new IllegalArgumentException();
        absStart = start;
        absStop = stop;
        page = (int) Math.ceil(absStop / (float) PAGE_SIZE);
    }

    private Range(int start, int stop, int perPage) {
        absStart = start;
        absStop = stop;
        page = (int) Math.ceil(absStop / (float) perPage);
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

    public Range diff(Range o) {
        int required = count() - o.count();
        if (required < 1 || absStart != o.absStart) {
            throw new IllegalArgumentException();
        }
        int at = o.absStop;
        if (at % required == 0) {
            return new Range(at, absStop, required);
        } else {
            int round = (int) Math.floor(at / (float) required);
            return new Range(required * round, absStop, absStop - required * round);
        }
    }

    public int perPage() {
        return absStop / page;
    }

    public int count() {
        return absStop - absStart;
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
        return page < MAX_PAGE;
    }

    public boolean hasPrevious() {
        return absStart - PAGE_SIZE >= 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Range range = (Range) o;
        return absStart == range.absStart &&
               absStop == range.absStop &&
               page == range.page;
    }

    @Override
    public int hashCode() {
        return Objects.hash(absStart, absStop, page);
    }

    @Override
    public String toString() {
        return "Range{" +
               "absStart=" + absStart +
               ", absStop=" + absStop +
               ", page=" + page +
               '}';
    }
}
