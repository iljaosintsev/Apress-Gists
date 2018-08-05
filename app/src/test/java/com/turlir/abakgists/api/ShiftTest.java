package com.turlir.abakgists.api;

import com.turlir.abakgists.allgists.Range;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
        Range start = new Range(0, 30);
        Range second = start.next(); // 15;45
        Range defect = second.cut(15); // 15;30
        Range required = second.diff(defect); // 30;45
        int page = required.page; // 3
        int perPage = required.perPage(); // 15

        AbsolutePoint abs = new AbsolutePoint();
        Range shifted = abs.shift(required);
        assertEquals(255, shifted.absStart);
        assertEquals(270, shifted.absStop);
        assertEquals(15, shifted.perPage());
        assertEquals(18, shifted.page);
    }

    @Test
    public void absolutePointTwo() {
        Range start = new Range(0, 30, 30);
        int page = start.page; // 1
        int perPage = start.perPage(); // 30

        AbsolutePoint abs = new AbsolutePoint();
        Range shifted = abs.shift(start);
        assertEquals(270, shifted.absStart);
        assertEquals(300, shifted.absStop);
        assertEquals(30, shifted.perPage());
        assertEquals(10, shifted.page);
    }

    static class AbsolutePoint {
        private final static int PAGE = 20;
        private final static int PER_PAGE = 15;

        Range shift(Range r) {
            int perPage = r.perPage();
            int page = r.page;
            if (perPage == PER_PAGE) {
                page = PAGE - page + 1;
                return new Range((page - 1) * PER_PAGE, page * PER_PAGE, perPage);
            } else {
                int scaled = perPage / 15;
                int demo = MAX_PAGE / scaled;
                page = demo - page + 1;
                return new Range((page - 1) * perPage, page * perPage, perPage);
            }
        }
    }

}