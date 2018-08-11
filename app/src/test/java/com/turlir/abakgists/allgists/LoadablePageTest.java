package com.turlir.abakgists.allgists;

import org.junit.Test;

import static org.junit.Assert.*;

public class LoadablePageTest {

    @Test
    public void simplePage() {
        LoadablePage page = new LoadablePage(0, 30);
        assertEquals(1, page.number);
        assertEquals(30, page.size);

        page = new LoadablePage(30, 60);
        assertEquals(2, page.number);
        assertEquals(30, page.size);

        page = new LoadablePage(30, 45);
        assertEquals(3, page.number);
        assertEquals(15, page.size);
    }

    @Test(expected = IllegalArgumentException.class)
    public void unbreakable() {
        new LoadablePage(35, 45);
    }

    @Test
    public void forbiddenNext() {
        int maxPage = 20;
        int limit = maxPage * 15;
        int pageSize = 15;
        LoadablePage range = new LoadablePage(limit - pageSize, limit);
        assertFalse(range.hasNext());
    }

    @Test
    public void forbiddenPrevious() {
        LoadablePage page = new LoadablePage(0, 30);
        assertFalse(page.hasPrevious());
    }

}