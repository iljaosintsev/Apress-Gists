package com.turlir.abakgists.gist;

import com.turlir.abakgists.Data;
import com.turlir.abakgists.api.data.GistLocalDao;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import io.reactivex.Single;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class GistInteractorTest {

    @Rule
    public TestRule schedulers = new TrampolineSchedulerRule();

    @Mock
    private GistLocalDao dao;

    private GistInteractor inter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        inter = new GistInteractor(dao);
    }

    @Test
    public void interactionBeforeLoad() {
        assertFalse(inter.possiblyDelete());
        assertFalse(inter.isChange("", ""));
        assertNull(inter.insteadWebLink());

        inter.delete().test().assertError(throwable -> true);
        inter.transact("", "").test().assertError(throwable -> true);
    }

    @Test
    public void interactionAfterLoad() {
        Mockito.when(dao.byId(Mockito.anyString())).thenReturn(Single.just(Data.LOCAL_STUB));
        inter.load("1")
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValueCount(1);
        assertTrue(inter.possiblyDelete());
        assertTrue(inter.isChange("", ""));
        assertNotNull(inter.insteadWebLink());
    }

    @Test
    public void descriptionOrNoteChange() {
        Mockito.when(dao.byId(Mockito.anyString())).thenReturn(Single.just(Data.LOCAL_STUB));
        inter.load("1")
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValueCount(1);
        //
        assertFalse(inter.isChange(Data.LOCAL_STUB.description, Data.LOCAL_STUB.note));
        assertTrue(inter.isChange("123", Data.LOCAL_STUB.note));
        assertTrue(inter.isChange(Data.LOCAL_STUB.description, "123"));
        assertTrue(inter.isChange("", ""));
    }
}