package com.turlir.abakgists.network;


import android.os.Build;

import com.turlir.abakgists.BuildConfig;
import com.turlir.abakgists.DatabaseMocking;
import com.turlir.abakgists.di.AppComponent;
import com.turlir.abakgists.model.GistModel;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.List;

import it.cosenonjaviste.daggermock.DaggerMockRule;
import it.cosenonjaviste.daggermock.InjectFromComponent;
import rx.Completable;
import rx.Observable;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.functions.Func1;
import rx.observers.TestSubscriber;
import rx.plugins.RxJavaHooks;
import rx.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP, packageName = "com.turlir.abakgists")
public class ModernRepoTest {

    @Rule
    public final DaggerMockRule<AppComponent> rule = new DatabaseMocking();

    @InjectFromComponent
    private Repository _repo;

    @Mock
    private ApiClient _mockClient;

    @BeforeClass
    public static void setup() throws Throwable {
        RxJavaHooks.setOnIOScheduler(new Func1<Scheduler, Scheduler>() {
            @Override
            public Scheduler call(Scheduler scheduler) {
                return Schedulers.immediate();
            }
        });
        RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {
            @Override
            public Scheduler getMainThreadScheduler() {
                return Schedulers.immediate();
            }
        });
    }

    @Before
    public void checkInject() {
        assertNotNull(_repo);
        assertNotNull(_mockClient);
    }

    @Test
    public void successFromCache() {
        Observable<List<GistModel>> obs = _repo.loadGistsFromCache(0);

        TestSubscriber<List<GistModel>> subs = new TestSubscriber<>();
        obs.subscribe(subs);
        subs.assertNoErrors();
        subs.assertNotCompleted();
        subs.assertValueCount(1);

        List<GistModel> first = subs.getOnNextEvents().get(0);
        assertNotNull(first);
        assertEquals(1, first.size());

        GistModel stub = new GistModel(
                "85547e4878dd9a573215cd905650f284",
                "https://api.github.com/gists/85547e4878dd9a573215cd905650f284",
                "2017-04-27T21:54:24Z",
                "Part of setTextByParts",
                "note",
                "iljaosintsev",
                "https://avatars1.githubusercontent.com/u/3526847?v=3"
        );
        assertEquals(stub, first.get(0));
    }

    @Test
    public void clearCacheTest() throws IOException {
        Completable obs = _repo.clearCache();
        TestSubscriber subs = new TestSubscriber();
        obs.subscribe(subs);

        subs.assertNoErrors();
        subs.assertCompleted();

        Observable<List<GistModel>> server = _repo.loadGistsFromCache(0);
        TestSubscriber<List<GistModel>> test = new TestSubscriber<>();
        server.subscribe(test);

        test.assertNoErrors();
        test.assertValueCount(1);
        test.assertNotCompleted();
        List<List<GistModel>> events = test.getOnNextEvents();
        assertEquals(1, events.size());
        List<GistModel> first = events.get(0);
        assertEquals(0, first.size());
    }

}
