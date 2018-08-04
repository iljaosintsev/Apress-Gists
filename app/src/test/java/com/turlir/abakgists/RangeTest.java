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

        range = range.next();
        check(range, 15, 45, 3);

        range = range.next();
        check(range, 30, 60, 4);
    }

    @Test
    public void atFirstPage() {
        Range range = new Range(0, 15, 1);
        check(range, 0, 15, 1);

        range = range.next();
        check(range, 15, 30, 2);
    }

    @Test
    public void atSpecificPosition() {
        Range range = new Range(30, 40);
        assertEquals(3, range.page);
        assertFalse(range.isWhole());

        range = range.next();
        check(range, 45, 55, 4);
        assertFalse(range.isWhole());
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

    // specRequiredItems

    @Test
    public void aliquotRequiredItems() {
        Range range = new Range(15, 45);
        int[] spec = range.specRequiredItems(15);
        int page = spec[0];
        int perPage = spec[1];
        assertEquals(3, page);
        assertEquals(15, perPage);
    }

    @Test
    public void incompleteRequiredItems() {
        Range range = new Range(15, 45);
        int[] spec = range.specRequiredItems(20);
        int page = spec[0];
        int perPage = spec[1];
        assertEquals(4, page);
        assertEquals(10, perPage);
    }

    private void check(Range r, int s, int e, int p) {
        assertEquals(s, r.absStart);
        assertEquals(e, r.absStop);
        assertEquals(p, r.page);
    }

}
