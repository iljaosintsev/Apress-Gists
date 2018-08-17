package com.turlir.abakgists.allgists;

import android.support.annotation.VisibleForTesting;

import java.util.Objects;

public class Range {

    private static final int MAX_EL = 105;

    public final int absStart, absStop, addition;

    @VisibleForTesting
    public Range(int start, int stop) {
        this(start, stop, 15);
    }

    public Range(int start, int stop, int addition) {
        if (start >= stop) throw new IllegalArgumentException();
        absStart = start;
        absStop = stop;
        this.addition = addition;
    }

    public LoadablePage page() {
        return page(count());
    }

    private LoadablePage page(int perPage) {
        return new LoadablePage(absStart, absStart + perPage);
    }

    public Range cut(int size) {
        if (size > count()) throw new IllegalArgumentException();
        return new Range(absStart, absStart + size, addition);
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
            int center = absStart + count() / 2;
            if (at > center) {
                return downScale(2);
            } else {
                return this;
            }
        }
    }

    public int count() {
        return absStop - absStart;
    }

    public Range next() {
        return new Range(absStart + addition, absStop + addition, addition);
    }

    public Range prev() {
        int start = Math.max(0, absStart - addition);
        return new Range(start, absStop - addition, addition);
    }

    public boolean hasNext() {
        return absStop + addition <= MAX_EL;
    }

    public boolean hasPrevious() {
        return absStart >= addition;
    }

    private Range downScale(int coefficient) {
        if (coefficient < 2 || count() % coefficient != 0) throw new IllegalArgumentException();
        int newStart = absStart + count() / coefficient;
        return new Range(newStart, absStop, addition);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Range range = (Range) o;
        return absStart == range.absStart &&
               absStop == range.absStop &&
               addition == range.addition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(absStart, absStop, addition);
    }

    @Override
    public String toString() {
        return "Range{" +
               "absStart=" + absStart +
               ", absStop=" + absStop +
               ", addition=" + addition +
               '}';
    }
}
