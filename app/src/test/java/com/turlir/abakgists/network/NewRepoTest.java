package com.turlir.abakgists.network;


import android.content.Context;

import com.turlir.abakgists.di.AppComponent;
import com.turlir.abakgists.di.AppModule;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import it.cosenonjaviste.daggermock.DaggerMockRule;
import it.cosenonjaviste.daggermock.InjectFromComponent;

public class NewRepoTest {

    @Rule
    public final DaggerMockRule<AppComponent> rule = new JUnitDaggerMockRule();

    @InjectFromComponent
    private Repository _repo;

    @Mock
    private ApiClient _mockClient;

    @Test
    public void injectTest() {
        org.junit.Assert.assertNotNull(_repo);
        org.junit.Assert.assertNotNull(_mockClient);
    }

    private static class JUnitDaggerMockRule extends DaggerMockRule<AppComponent> {
        JUnitDaggerMockRule() {
            super(AppComponent.class, new TestAppModule());
        }

    }
    private static class TestAppModule extends AppModule {
        TestAppModule() {
            super(Mockito.mock(Context.class));
        }
    }

}
