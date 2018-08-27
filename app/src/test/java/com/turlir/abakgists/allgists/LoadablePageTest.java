package com.turlir.abakgists.allgists;

import com.turlir.abakgists.allgists.loader.LoadablePage;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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

    @Test(expected = IllegalArgumentException.class)
    public void failureCombination() {
        new LoadablePage(15, 45);
    }

}