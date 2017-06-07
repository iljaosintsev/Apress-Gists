package com.turlir.abakgists.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TokenSwitcherTest {

    private TokenSwitcher mSwitcher;

    //<editor-fold desc="ApplyGroup">

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

    //</editor-fold>

    /**
     * Назначаем токен в первый раз при наличии цели
     */
    @Test
    public void setToken_FistTime_ThereChild_Test() {
        TokenSwitcher.TokenInformator inf = mock(TokenSwitcher.TokenInformator.class);
        mSwitcher = new TokenSwitcher(0, inf);

        when(inf.getChildIndexByToken(0)).thenReturn(0);
        ChildDiff diff = mSwitcher.setToken(0);

        ChildDiff.ChildManipulator man = mock(ChildDiff.ChildManipulator.class);
        diff.apply(man);

        verify(man).showChild(0);
        verify(man, never()).hideChild(anyInt());

        assertEquals(0, mSwitcher.currentToken());
    }

    /**
     * Назначаем токен в первый раз, показываемого элемемента пока нет
     */
    @Test
    public void setToken_FistTime_NoChild_Test() {
        TokenSwitcher.TokenInformator inf = mock(TokenSwitcher.TokenInformator.class);
        mSwitcher = new TokenSwitcher(0, inf);

        when(inf.getChildIndexByToken(1)).thenReturn(TokenizeLayout.INVALID_INDEX);
        ChildDiff diff = mSwitcher.setToken(1); // пустой diff

        ChildDiff.ChildManipulator man = mock(ChildDiff.ChildManipulator.class);
        diff.apply(man);

        verify(man, never()).hideChild(anyInt());
        verify(man, never()).showChild(anyInt());

        assertEquals(1, mSwitcher.currentToken());
    }

    //////
    ////// Переустанавливаем токен
    //////

    /**
     * Не меняем токен
     */
    @Test
    public void setToken_TokenNotChange_Test() {
        TokenSwitcher.TokenInformator inf = mock(TokenSwitcher.TokenInformator.class);
        mSwitcher = new TokenSwitcher(0, inf);

        when(inf.getChildIndexByToken(0)).thenReturn(0);
        mSwitcher.setToken(0);

        ChildDiff diff = mSwitcher.setToken(0);

        ChildDiff.ChildManipulator man = mock(ChildDiff.ChildManipulator.class);
        diff.apply(man);

        verify(man, never()).hideChild(anyInt());
        verify(man, never()).showChild(anyInt());

        assertEquals(0, mSwitcher.currentToken());
    }

    /**
     * Меняем токен при наличии и скрываемого и показываемого потомков (1;1)
     */
    @Test
    public void setToken_WithOld_WitTarget_Test() {
        TokenSwitcher.TokenInformator inf = mock(TokenSwitcher.TokenInformator.class);
        mSwitcher = new TokenSwitcher(0, inf);

        when(inf.getChildIndexByToken(0)).thenReturn(0);
        mSwitcher.setToken(0);
        //
        when(inf.getChildCount()).thenReturn(2);
        when(inf.getChildIndexByToken(1)).thenReturn(1);
        ChildDiff diff = mSwitcher.setToken(1);

        ChildDiff.ChildManipulator man = mock(ChildDiff.ChildManipulator.class);
        diff.apply(man);

        verify(man).hideChild(0);
        verify(man).showChild(1);

        assertEquals(1, mSwitcher.currentToken());
    }

    /**
     * Ранее показываемого потомка нет, новый есть (0;1)
     */
    @Test
    public void setToken_WithoutOld_WitTarget_Test() {
        TokenSwitcher.TokenInformator inf = mock(TokenSwitcher.TokenInformator.class);
        mSwitcher = new TokenSwitcher(0, inf);

        when(inf.getChildIndexByToken(0)).thenReturn(2); // 0, 1, 2 - три потомка
        mSwitcher.setToken(0);
        //
        when(inf.getChildCount()).thenReturn(2);
        when(inf.getChildIndexByToken(1)).thenReturn(1); // 0, 1 - два потомка
        ChildDiff diff = mSwitcher.setToken(1);

        ChildDiff.ChildManipulator man = mock(ChildDiff.ChildManipulator.class);
        diff.apply(man);

        verify(man).showChild(1);
        verify(man, never()).hideChild(anyInt());

        assertEquals(1, mSwitcher.currentToken());
    }

    /**
     * Ранее показываемый потомок есть, а нового нет (1;0)
     */
    @Test
    public void setToken_WithOld_WithoutTarget_Test() {
        TokenSwitcher.TokenInformator inf = mock(TokenSwitcher.TokenInformator.class);
        mSwitcher = new TokenSwitcher(0, inf);

        when(inf.getChildIndexByToken(0)).thenReturn(0);
        mSwitcher.setToken(0);
        //
        when(inf.getChildCount()).thenReturn(1);
        when(inf.getChildIndexByToken(1)).thenReturn(TokenizeLayout.INVALID_INDEX);
        ChildDiff diff = mSwitcher.setToken(1);

        ChildDiff.ChildManipulator man = mock(ChildDiff.ChildManipulator.class);
        diff.apply(man);

        verify(man).hideChild(0);
        verify(man, never()).showChild(anyInt());

        assertEquals(0, mSwitcher.currentToken());
    }

    /**
     * Ранее показываемого есть, нового нет (0;0)
     */
    @Test
    public void setToken_withoutAny_Test() {
        TokenSwitcher.TokenInformator inf = mock(TokenSwitcher.TokenInformator.class);
        mSwitcher = new TokenSwitcher(0, inf);

        when(inf.getChildIndexByToken(1)).thenReturn(1); // было > 0 элементов
        mSwitcher.setToken(1);
        //
        when(inf.getChildCount()).thenReturn(0); // стало 0
        ChildDiff diff = mSwitcher.setToken(0);

        ChildDiff.ChildManipulator man = mock(ChildDiff.ChildManipulator.class);
        diff.apply(man);

        verify(man, never()).hideChild(anyInt()); // ничего не сработало
        verify(man, never()).showChild(anyInt());
    }

    /**
     * Вариант развития событий после {@link #setToken_WithOld_WithoutTarget_Test()}
     * Повторном устанавливаем этот же токен, но потомок уже есть
     */
    @Test
    public void setToken_afterHided_and_NotShowing_Test() {
        TokenSwitcher.TokenInformator inf = mock(TokenSwitcher.TokenInformator.class);
        mSwitcher = new TokenSwitcher(0, inf);

        when(inf.getChildIndexByToken(0)).thenReturn(0);
        mSwitcher.setToken(0);
        //
        when(inf.getChildCount()).thenReturn(1);
        when(inf.getChildIndexByToken(1)).thenReturn(TokenizeLayout.INVALID_INDEX);
        mSwitcher.setToken(1);
        //

        when(inf.getChildCount()).thenReturn(2);
        when(inf.getChildIndexByToken(1)).thenReturn(1);
        ChildDiff diff = mSwitcher.setToken(1);

        ChildDiff.ChildManipulator man = mock(ChildDiff.ChildManipulator.class);
        diff.apply(man);

        verify(man).showChild(1);
    }

}