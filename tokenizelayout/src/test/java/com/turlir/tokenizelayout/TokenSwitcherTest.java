package com.turlir.tokenizelayout;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TokenSwitcherTest {

    @Mock
    private ChildDiff.ChildManipulator man;

    @Mock
    private TokenSwitcher.TokenInformator inf;

    private TokenSwitcher mSwitcher;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mSwitcher = new TokenSwitcher(0, inf);
    }

    //<editor-fold desc="applyGroup">

    @Test
    public void applyGroupByChild_BySingleChild_DoesToken() {
        Mockito.when(inf.doesViewToToken(0, 0)).thenReturn(true);
        boolean b = mSwitcher.applyGroupByChild(0);
        assertTrue(b);
    }

    @Test
    public void applyGroupByChild_BySingleChild_NotDoesToken() {
        Mockito.when(inf.doesViewToToken(0, 0)).thenReturn(false);
        boolean b = mSwitcher.applyGroupByChild(0);
        assertFalse(b);
    }

    @Test
    public void applyGroupChild_BySecondChild_NotEquals() {
        Mockito.when(inf.doesViewToToken(0, 0)).thenReturn(true);
        boolean b = mSwitcher.applyGroupByChild(0);
        assertTrue(b);

        Mockito.when(inf.doesViewToToken(0, 1)).thenReturn(false);
        b = mSwitcher.applyGroupByChild(1);
        assertFalse(b);
    }

    //</editor-fold>

    //<editor-fold desc="setToken">

    /**
     * Назначаем токен в первый раз при наличии цели
     */
    @Test
    public void setToken_FistTime_ThereChild() {
        Mockito.when(inf.getChildIndexByToken(0)).thenReturn(0);
        ChildDiff diff = mSwitcher.setToken(0);

        diff.apply(man);

        Mockito.verify(man).showChild(0);
        Mockito.verify(man, Mockito.never()).hideChild(ArgumentMatchers.anyInt());

        assertEquals(0, mSwitcher.currentToken());
    }

    /**
     * Назначаем токен в первый раз, показываемого элемемента пока нет
     */
    @Test
    public void setToken_FistTime_NoChild() {
        Mockito.when(inf.getChildIndexByToken(1)).thenReturn(TokenSwitcher.INVALID_INDEX);
        ChildDiff diff = mSwitcher.setToken(1); // пустой diff

        diff.apply(man);

        Mockito.verify(man, Mockito.never()).hideChild(ArgumentMatchers.anyInt());
        Mockito.verify(man, Mockito.never()).showChild(ArgumentMatchers.anyInt());

        assertEquals(1, mSwitcher.currentToken());
    }

    //////
    ////// Переустанавливаем токен
    //////

    /**
     * Не меняем токен
     */
    @Test
    public void setToken_TokenNotChange() {
        setTokenZero();

        ChildDiff diff = mSwitcher.setToken(0);

        diff.apply(man);

        Mockito.verify(man, Mockito.never()).hideChild(ArgumentMatchers.anyInt());
        Mockito.verify(man, Mockito.never()).showChild(ArgumentMatchers.anyInt());

        assertEquals(0, mSwitcher.currentToken());
    }

    /**
     * Меняем токен при наличии и скрываемого и показываемого потомков (1;1)
     */
    @Test
    public void setToken_WithOld_WitTarget() {
        setTokenZero();
        //
        Mockito.when(inf.getChildCount()).thenReturn(2);
        Mockito.when(inf.getChildIndexByToken(1)).thenReturn(1);
        ChildDiff diff = mSwitcher.setToken(1);

        diff.apply(man);

        Mockito.verify(man).hideChild(0);
        Mockito.verify(man).showChild(1);

        assertEquals(1, mSwitcher.currentToken());
    }

    /**
     * Ранее показываемого потомка нет, новый есть (0;1)
     */
    @Test
    public void setToken_WithoutOld_WitTarget() {
        Mockito.when(inf.getChildIndexByToken(0)).thenReturn(2); // 0, 1, 2 - три потомка
        mSwitcher.setToken(0);
        //
        Mockito.when(inf.getChildCount()).thenReturn(2);
        Mockito.when(inf.getChildIndexByToken(1)).thenReturn(1); // 0, 1 - два потомка
        ChildDiff diff = mSwitcher.setToken(1);

        diff.apply(man);

        Mockito.verify(man).showChild(1);
        Mockito.verify(man, Mockito.never()).hideChild(ArgumentMatchers.anyInt());

        assertEquals(1, mSwitcher.currentToken());
    }

    /**
     * Ранее показываемый потомок есть, а нового нет (1;0)
     */
    @Test
    public void setToken_WithOld_WithoutTarget() {
        setTokenZero();
        //
        Mockito.when(inf.getChildCount()).thenReturn(1);
        Mockito.when(inf.getChildIndexByToken(1)).thenReturn(TokenSwitcher.INVALID_INDEX);
        ChildDiff diff = mSwitcher.setToken(1);

        diff.apply(man);

        Mockito.verify(man).hideChild(0);
        Mockito.verify(man, Mockito.never()).showChild(ArgumentMatchers.anyInt());

        assertEquals(0, mSwitcher.currentToken());
    }

    /**
     * Ранее показываемого есть, нового нет (0;0)
     */
    @Test
    public void setToken_withoutAny() {
        Mockito.when(inf.getChildIndexByToken(1)).thenReturn(1); // было > 0 элементов
        mSwitcher.setToken(1);
        //
        Mockito.when(inf.getChildCount()).thenReturn(0); // стало 0
        ChildDiff diff = mSwitcher.setToken(0);

        diff.apply(man);

        Mockito.verify(man, Mockito.never()).hideChild(ArgumentMatchers.anyInt()); // ничего не сработало
        Mockito.verify(man, Mockito.never()).showChild(ArgumentMatchers.anyInt());
    }

    /**
     * Вариант развития событий после {@link #setToken_WithOld_WithoutTarget()}
     * Повторном устанавливаем этот же токен, но потомок уже есть
     */
    @Test
    public void setToken_afterHided_and_NotShowing() {
        setTokenZero();
        //
        Mockito.when(inf.getChildCount()).thenReturn(1);
        Mockito.when(inf.getChildIndexByToken(1)).thenReturn(TokenSwitcher.INVALID_INDEX);
        mSwitcher.setToken(1);
        //

        Mockito.when(inf.getChildCount()).thenReturn(2);
        Mockito.when(inf.getChildIndexByToken(1)).thenReturn(1);
        ChildDiff diff = mSwitcher.setToken(1);

        diff.apply(man);

        Mockito.verify(man).showChild(1);
    }

    //</editor-fold>

    private void setTokenZero() {
        Mockito.when(inf.getChildIndexByToken(0)).thenReturn(0);
        mSwitcher.setToken(0);
    }

}