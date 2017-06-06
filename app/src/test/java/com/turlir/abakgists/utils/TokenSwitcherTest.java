package com.turlir.abakgists.utils;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TokenSwitcherTest {

    private TokenSwitcher mSwitcher;

    @Test
    public void applyGroupByChild_BySingleChild_DoesToken_Test() {
        TokenSwitcher.TokenInformator inf = mock(TokenSwitcher.TokenInformator.class);
        mSwitcher = new TokenSwitcher(0, inf);

        when(inf.doesViewToToken(0, 0)).thenReturn(true);
        boolean b = mSwitcher.applyGroupByChild(0);
        assertTrue(b);
    }

    @Test
    public void applyGroupByChild_BySingleChild_NotDoesToken_Test() {
        TokenSwitcher.TokenInformator inf = mock(TokenSwitcher.TokenInformator.class);
        mSwitcher = new TokenSwitcher(0, inf);

        when(inf.doesViewToToken(0, 0)).thenReturn(false);
        boolean b = mSwitcher.applyGroupByChild(0);
        assertFalse(b);
    }

    @Test
    public void applyGroupChild_BySecondChild_NotEquals_Test() {
        TokenSwitcher.TokenInformator inf = mock(TokenSwitcher.TokenInformator.class);
        mSwitcher = new TokenSwitcher(0, inf);

        when(inf.doesViewToToken(0, 0)).thenReturn(true);
        boolean b = mSwitcher.applyGroupByChild(0);
        assertTrue(b);

        when(inf.doesViewToToken(0, 1)).thenReturn(false);
        b = mSwitcher.applyGroupByChild(1);
        assertFalse(b);
    }

    @Test
    public void setToken_FistRun_ThereChild_Test() {
        TokenSwitcher.TokenInformator inf = mock(TokenSwitcher.TokenInformator.class);
        mSwitcher = new TokenSwitcher(0, inf);

        when(inf.getChildIndexByToken(0)).thenReturn(1);
        ChildDiff diff = mSwitcher.setToken(0);

        ChildDiff.ChildManipulator man = mock(ChildDiff.ChildManipulator.class);
        diff.apply(man);

        verify(man).showChild(1);
    }

    @Test
    public void setToken_FistRun_NoChild_Test() {
        TokenSwitcher.TokenInformator inf = mock(TokenSwitcher.TokenInformator.class);
        mSwitcher = new TokenSwitcher(0, inf);

        when(inf.getChildIndexByToken(0)).thenReturn(TokenizeLayout.INVALID_INDEX);
        ChildDiff diff = mSwitcher.setToken(0); // пустой diff

        ChildDiff.ChildManipulator man = mock(ChildDiff.ChildManipulator.class);
        diff.apply(man);

        verify(man, never()).hideChild(anyInt());
        verify(man, never()).showChild(anyInt());
    }

}