package com.turlir.abakgists;

import com.turlir.abakgists.allgists.Range;

import org.junit.Test;

import static org.junit.Assert.*;

public class RangeTest {

    // next

    @Test
    public void simpleStart() {
        Range range = new Range(0, 30);
        check(range, 0, 30);
        assertEquals(15, range.addition);

        range = range.next();
        check(range, 15, 45);

        range = range.next();
        check(range, 30, 60);
    }

    // previous

    @Test
    public void previousAtFirst() {
        Range range = new Range(0, 30);
        assertFalse(range.page().hasPrevious());
    }

    @Test
    public void pagedPrevious() {
        Range range = new Range(30, 40);
        assertTrue(range.page().hasPrevious());
        range = range.prev();
        check(range, 15, 25);

        range = range.prev();
        check(range, 0, 10);
    }

    // diff

    @Test
    public void aliquotDiff() {
        Range demand = new Range(15, 45);
        Range actual = new Range(15, 30);
        Range diff = demand.diff(actual);
        assertEquals(15, diff.addition);
        assertEquals(30, diff.absStart);
        assertEquals(45, diff.absStop);
    }

    @Test
    public void incompleteDiff() {
        Range demand = new Range(15, 45, 15);
        Range actual = new Range(15, 15 + 20, 15);
        Range diff = demand.diff(actual);
        assertEquals(15, diff.addition);
        assertEquals(30, diff.absStart);
        assertEquals(45, diff.absStop);

        demand = new Range(15, 45, 15);
        actual = new Range(15, 37, 15);
        diff = demand.diff(actual);
        assertEquals(15, diff.addition);
        assertEquals(30, diff.absStart);
        assertEquals(45, diff.absStop);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invertDiff() {
        Range demand = new Range(15, 45);
        Range actual = new Range(15, 15 + 15);
        actual.diff(demand);
    }

    @Test(expected = IllegalArgumentException.class)
    public void diffWithEqualsRange() {
        Range demand = new Range(15, 45);
        Range actual = new Range(15, 45);
        actual.diff(demand);
    }

    // cut

    @Test
    public void correctCut() {
        Range range = new Range(15, 45);
        Range actual = range.cut(15);
        check(actual, 15, 30);
    }

    @Test(expected = IllegalArgumentException.class)
    public void lessStartCut() {
        Range range = new Range(15, 45);
        range.cut(1);
    }

    private void check(Range r, int s, int e) {
        assertEquals(new Range(s, e), r);
    }

}
