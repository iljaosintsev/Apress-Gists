package com.turlir.abakgists;

import com.turlir.abakgists.allgists.loader.Range;
import com.turlir.abakgists.allgists.loader.Window;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RangeTest {

    // next

    @Test
    public void simpleStart() {
        Window range = new Range(0, 30);
        check(range, 0, 30);
        assertEquals(15, range.addition());
        assertTrue(range.hasNext());

        range = range.next();
        check(range, 15, 45);
        assertTrue(range.hasNext());

        range = range.next();
        check(range, 30, 60);
        assertTrue(range.hasNext());
    }

    @Test
    public void forbiddenNext() {
        Range page = new Range(90, 105, 15);
        assertFalse(page.hasNext());
    }

    // previous

    @Test
    public void previousAtFirst() {
        Range range = new Range(0, 30);
        assertFalse(range.hasPrevious());
    }

    @Test
    public void pagedPrevious() {
        Window range = new Range(30, 60);
        assertTrue(range.hasPrevious());
        range = range.prev();
        check(range, 15, 45);

        range = range.prev();
        check(range, 0, 30);
    }

    @Test
    public void forbiddenPrevious() {
        Range page = new Range(0, 30, 15);
        assertFalse(page.hasPrevious());
    }

    // diff

    @Test
    public void aliquotDiff() {
        Range demand = new Range(15, 45);
        Range actual = new Range(15, 30);
        Window diff = demand.diff(actual);
        assertTrue(new Range(30, 45, 15).equals(diff));
    }

    @Test
    public void incompleteDiff() {
        Range demand = new Range(15, 45, 15);
        Range actual = new Range(15, 35, 15);
        Window diff = demand.diff(actual);
        assertTrue(new Range(30, 45, 15).equals(diff));

        demand = new Range(15, 45, 15);
        actual = new Range(15, 37, 15);
        diff = demand.diff(actual);
        assertTrue(new Range(30, 45, 15).equals(diff));
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
        Window actual = range.cut(15);
        check(actual, 15, 30);

        actual = range.cut(1);
        check(actual, 15, 16);

        actual = range.cut(30);
        check(actual, 15, 45);
    }

    @Test
    public void cutBeforeStart() {
        Range range = new Range(30, 60);
        Window actual = range.cut(15);
        check(actual, 30, 45);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cutOutOfRange() {
        Range range = new Range(15, 45);
        range.cut(31);
    }

    private void check(Window r, int s, int e) {
        assertEquals(new Range(s, e), r);
    }

}
