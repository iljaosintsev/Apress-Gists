package com.turlir.tokenizelayout;

import org.junit.Test;
import org.mockito.Mockito;

public class ChildDiffBuilderTest {

    @Test
    public void showTest() throws Exception {
        ChildDiff.Builder b = new ChildDiff.Builder();
        b.show(0);
        ChildDiff diff = b.build();

        ChildDiff.ChildManipulator mock = Mockito.mock(ChildDiff.ChildManipulator.class);
        diff.apply(mock);

        Mockito.verify(mock).showChild(0);

        org.junit.Assert.assertFalse(diff.isEmpty());
    }

    @Test
    public void hideTest() throws Exception {
        ChildDiff.Builder b = new ChildDiff.Builder();
        b.hide(0);
        ChildDiff diff = b.build();

        ChildDiff.ChildManipulator mock = Mockito.mock(ChildDiff.ChildManipulator.class);
        diff.apply(mock);

        Mockito.verify(mock).hideChild(0);

        org.junit.Assert.assertFalse(diff.isEmpty());
    }

    @Test
    public void emptyTest() {
        ChildDiff.Builder b = new ChildDiff.Builder();
        ChildDiff diff = b.build();

        ChildDiff.ChildManipulator mock = Mockito.mock(ChildDiff.ChildManipulator.class);
        diff.apply(mock);

        Mockito.verify(mock, Mockito.never()).showChild(Mockito.anyInt());
        Mockito.verify(mock, Mockito.never()).hideChild(Mockito.anyInt());
        org.junit.Assert.assertTrue(diff.isEmpty());
    }

    @Test
    public void realTest() {
        ChildDiff.Builder b = new ChildDiff.Builder();
        b.hide(0);
        b.show(1);
        ChildDiff diff = b.build();

        ChildDiff.ChildManipulator mock = Mockito.mock(ChildDiff.ChildManipulator.class);
        diff.apply(mock);

        Mockito.verify(mock).hideChild(0);
        Mockito.verify(mock).showChild(1);
        org.junit.Assert.assertFalse(diff.isEmpty());
    }

}