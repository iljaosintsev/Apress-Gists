package com.turlir.abakgists.templater.base;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GroupTest {

    private Group mGroup;

    @Before
    public void setup() {
        mGroup = new Group(0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void closeBeforeStart() {
        mGroup.close(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void closeEqualsStart() {
        mGroup.close(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void closeAgain() {
        mGroup.close(3);
        mGroup.close(3);
    }

    @Test
    public void closeGreatStart() {
        mGroup.close(3);
    }

    @Test
    public void shiftTest() {
        mGroup.close(3);

        assertFalse(mGroup.does(0));
        assertTrue(mGroup.does(1));
        assertTrue(mGroup.does(2));
        assertTrue(mGroup.does(3));

        mGroup.shift();

        assertTrue(mGroup.does(0));
        assertTrue(mGroup.does(1));
        assertTrue(mGroup.does(2));
        assertFalse(mGroup.does(3));
    }
}