package com.turlir.abakgists.gistsloader;

import android.support.annotation.VisibleForTesting;

import com.turlir.abakgists.base.loader.Window;
import com.turlir.abakgists.base.loader.server.LoadableItem;

import java.util.Objects;

public class Range implements Window {

    private static final int MAX_EL = 105;

    private final int absStart, absStop, addition;

    @VisibleForTesting
    public Range(int start, int stop) {
        this(start, stop, 15);
    }

    public Range(int start, int stop, int addition) {
        if (start > stop) throw new IllegalArgumentException();
        if (start == stop && start != 0) throw new IllegalArgumentException();
        absStart = start;
        absStop = stop;
        this.addition = addition;
    }

    @Override
    public int start() {
        return absStart;
    }

    @Override
    public int addition() {
        return addition;
    }

    @Override
    public int stop() {
        return absStop;
    }

    @Override
    public int count() {
        return absStop - absStart;
    }

    @Override
    public Window next() {
        return new Range(absStart + addition, absStop + addition, addition);
    }

    @Override
    public Window prev() {
        int start = Math.max(0, absStart - addition);
        return new Range(start, absStop - addition, addition);
    }

    @Override
    public boolean hasNext() {
        return absStop + addition <= MAX_EL;
    }

    @Override
    public boolean hasPrevious() {
        return absStart >= addition;
    }

    @Override
    public LoadableItem constraint(int count) {
        Window already = cut(this, count);
        Window required = diff(this, already);
        LoadableItem page = page(required);
        return page;
    }

    private Window cut(Window w, int size) {
        if (size > w.count()) throw new IllegalArgumentException();
        return new Range(w.start(), w.start() + size, w.addition());
    }

    private Window diff(Window a, Window o) {
        int required = a.count() - o.count();
        if (required < 1 || a.start() != o.start()) {
            throw new IllegalArgumentException();
        }
        int at = o.stop();
        if (at % required == 0) {
            return new Range(at, a.stop(), required);
        } else {
            int center = a.start() + a.count() / 2;
            if (at > center) {
                return downScale(a);
            } else {
                return a;
            }
        }
    }

    private Window downScale(Window context) {
        if (context.count() % 2 != 0) throw new IllegalArgumentException();
        int newStart = context.start() + context.count() / 2;
        return new Range(newStart, context.stop(), context.addition());
    }

    private LoadableItem page(Window w) {
        return page(w, w.count());
    }

    private LoadableItem page(Window w, int perPage) {
        return new LoadablePage(w.start(), w.start() + perPage);
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
