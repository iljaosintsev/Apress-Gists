package com.turlir.abakgists;

import com.turlir.abakgists.allgists.Range;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RangeTest {

    // next

    @Test
    public void simpleStart() {
        Range range = new Range(0, 30);
        check(range, 0, 30, 2);
        assertEquals(15, range.perPage());

        range = range.next();
        check(range, 15, 45, 3);

        range = range.next();
        check(range, 30, 60, 4);
    }

    @Test
    public void forbiddenNext() {
        int maxPage = 20;
        int limit = maxPage * 15;
        int pageSize = 15;
        Range range = new Range(limit - pageSize, limit);
        assertFalse(range.hasNext());
    }

    // previous

    @Test
    public void previousAtFirst() {
        Range range = new Range(0, 30);
        assertFalse(range.hasPrevious());
    }

    @Test
    public void pagedPrevious() {
        Range range = new Range(30, 40);
        assertTrue(range.hasPrevious());
        range = range.prev();
        check(range, 15, 25, 2);
        assertTrue(range.hasPrevious());

        range = range.prev();
        check(range, 0, 10, 1);
        assertFalse(range.hasPrevious());
    }

    // diff

    @Test
    public void aliquotDiff() {
        Range demand = new Range(15, 45);
        Range actual = new Range(15, 30);
        Range diff = demand.diff(actual);
        assertEquals(3, diff.page);
        assertEquals(15, diff.perPage());
        assertEquals(30, diff.absStart);
        assertEquals(45, diff.absStop);
    }

    @Test
    public void incompleteDiff() {
        Range demand = new Range(15, 45);
        Range actual = new Range(15, 15 + 20);
        Range diff = demand.diff(actual);
        assertEquals(3, diff.page);
        assertEquals(15, diff.perPage());
        assertEquals(30, diff.absStart);
        assertEquals(45, diff.absStop);

        demand = new Range(15, 45);
        actual = new Range(15, 37);
        diff = demand.diff(actual);
        assertEquals(4, diff.page);
        assertEquals(11, diff.perPage());
        assertEquals(32, diff.absStart);
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
        check(actual, 15, 30, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void lessStartCut() {
        Range range = new Range(15, 45);
        range.cut(1);
    }

    private void check(Range r, int s, int e, int p) {
        assertEquals(new Range(s, e), r);
        assertEquals(p, r.page);
    }

}
