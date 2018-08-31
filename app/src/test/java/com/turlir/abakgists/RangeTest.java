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

    private void check(Window r, int s, int e) {
        assertEquals(new Range(s, e), r);
    }

}
