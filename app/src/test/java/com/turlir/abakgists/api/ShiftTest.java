package com.turlir.abakgists.api;

import com.turlir.abakgists.allgists.loader.Range;
import com.turlir.abakgists.allgists.loader.Window;

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

    @Test
    public void absolutePointOne() {
        Window start = new Range(0, 30);
        Window second = start.next();
        assertTrue(new Range(15, 45, 15).equals(second));

        Window defect = second.cut(15);
        assertTrue(new Range(15, 30, 15).equals(defect));

        Window required = second.diff(defect);
        assertTrue(new Range(30, 45, 15).equals(required));
        assertEquals(3, required.page().number);

        AbsolutePoint abs = new AbsolutePoint();
        Range shifted = abs.shift(required);
        assertTrue(new Range(255, 270, 15).equals(shifted));
        assertEquals(18, shifted.page().number);
    }

    @Test
    public void absolutePointTwo() {
        Window start = new Range(0, 30, 30);
        assertEquals(1, start.page().number);
        assertEquals(30, start.addition());

        AbsolutePoint abs = new AbsolutePoint();
        Range shifted = abs.shift(start);
        assertTrue(new Range(270, 300, 30).equals(shifted));
        assertEquals(10, shifted.page().number);
    }

    static class AbsolutePoint {
        private final static int PAGE = 20;
        private final static int PER_PAGE = 15;

        Range shift(Window r) {
            int size = r.addition();
            int number = r.page().number;
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

}