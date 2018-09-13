package com.turlir.abakgists.gist;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import io.reactivex.functions.Consumer;

public class GistDeleteBusTest {

    @Mock
    private Consumer<String> agent, other;

    private GistDeleteBus mBus;

    @Before
    public void setup() {
        mBus = new GistDeleteBus();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void subscribeAfterDelete() throws Exception {
        mBus.gistDeleted("1");
        mBus.subscribe(agent);
        Mockito.verify(agent, Mockito.never()).accept(Mockito.anyString());
    }

    @Test
    public void subscribeBeforeDelete() throws Exception {
        mBus.subscribe(agent);
        mBus.gistDeleted("1");
        Mockito.verify(agent).accept("1");
    }

    @Test
    public void multiDelete() throws Exception {
        mBus.subscribe(agent);
        mBus.gistDeleted("1");
        Mockito.verify(agent).accept("1");

        mBus.gistDeleted("2");
        Mockito.verify(agent).accept("1");
    }

    @Test
    public void multiSubscribe() throws Exception {
        mBus.subscribe(agent);
        mBus.subscribe(other);
        mBus.gistDeleted("1");
        Mockito.verify(agent).accept("1");
        Mockito.verify(other).accept("1");

        mBus.gistDeleted("2");
        Mockito.verify(agent).accept("2");
        Mockito.verify(other).accept("2");
    }
}