package com.turlir.abakgists.api;

import com.turlir.abakgists.gistsloader.LoadablePage;
import com.turlir.abakgists.gistsloader.Range;
import com.turlir.abakgists.base.loader.Window;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ShiftTest {

    private static final int MAX_PAGE = 20; // with 15 items

    private final ApiClient.Shift mShift = new ApiClient.Shift();

    @Test
    public void easyShift() {
        int page = mShift.shift(1, 15);
        assertEquals(20, page);

        page = mShift.shift(2, 15);
        assertEquals(19, page);

        page = mShift.shift(3, 15);
        assertEquals(18, page);
    }

    @Test
    public void scaleShift() {
        int page = mShift.shift(1, 30);
        assertEquals(10, page);
    }

    // other way

    private final WindowDiffer differ = new WindowDiffer();

    @Test
    public void absolutePointOne() {
        Window start = new Range(0, 30);
        Window second = start.next();
        assertTrue(new Range(15, 45, 15).equals(second));

        Window defect = differ.cut(second, 15);
        assertTrue(new Range(15, 30, 15).equals(defect));

        Window required = differ.diff(second, defect);
        assertTrue(new Range(30, 45, 15).equals(required));
        assertEquals(3, differ.page(required).number);

        AbsolutePoint abs = new AbsolutePoint();
        Range shifted = abs.shift(required);
        assertTrue(new Range(255, 270, 15).equals(shifted));
        assertEquals(18, differ.page(shifted).number);
    }

    @Test
    public void absolutePointTwo() {
        Window start = new Range(0, 30, 30);
        assertEquals(1, differ.page(start).number);
        assertEquals(30, start.addition());

        AbsolutePoint abs = new AbsolutePoint();
        Range shifted = abs.shift(start);
        assertTrue(new Range(270, 300, 30).equals(shifted));
        assertEquals(10, differ.page(shifted).number);
    }

    static class AbsolutePoint {
        private final static int PAGE = 20;
        private final static int PER_PAGE = 15;
        private final WindowDiffer differ = new WindowDiffer();

        Range shift(Window r) {
            int size = r.addition();
            int number = differ.page(r).number;
            if (size == PER_PAGE) {
                number = PAGE - number + 1;
                return new Range((number - 1) * PER_PAGE, number * PER_PAGE, size);
            } else {
                int scaled = size / 15;
                int demo = MAX_PAGE / scaled;
                number = demo - number + 1;
                return new Range((number - 1) * size, number * size, size);
            }
        }
    }

    static class WindowDiffer {

        public LoadablePage page(Window w) {
            return page(w, w.count());
        }

        private LoadablePage page(Window w, int perPage) {
            return new LoadablePage(w.start(), w.start() + perPage);
        }

        Window cut(Window w, int size) {
            if (size > w.count()) throw new IllegalArgumentException();
            return new Range(w.start(), w.start() + size, w.addition());
        }

        Window diff(Window a, Window o) {
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
                    return downScale(a, 2);
                } else {
                    return a;
                }
            }
        }

        private Window downScale(Window context, int coefficient) {
            if (coefficient < 2 || context.count() % coefficient != 0) throw new IllegalArgumentException();
            int newStart = context.start() + context.count() / coefficient;
            return new Range(newStart, context.stop(), context.addition());
        }

    }

}