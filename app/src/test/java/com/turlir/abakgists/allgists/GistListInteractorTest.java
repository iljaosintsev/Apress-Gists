package com.turlir.abakgists.allgists;

import android.os.Build;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.turlir.abakgists.BuildConfig;
import com.turlir.abakgists.Data;
import com.turlir.abakgists.DatabaseMocking;
import com.turlir.abakgists.api.ApiClient;
import com.turlir.abakgists.api.Repository;
import com.turlir.abakgists.api.data.GistJson;
import com.turlir.abakgists.api.data.GistLocal;
import com.turlir.abakgists.di.AppComponent;
import com.turlir.abakgists.model.GistModel;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Collections;
import java.util.List;

import it.cosenonjaviste.daggermock.DaggerMockRule;
import it.cosenonjaviste.daggermock.InjectFromComponent;
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
public class GistListInteractorTest {

    @Rule
    public final DaggerMockRule<AppComponent> rule = new DatabaseMocking();

    @InjectFromComponent
    private Repository _repo;

    @InjectFromComponent
    private StorIOSQLite _database;

    @Mock
    private ApiClient _client;

    private GistListInteractor mRequester;

    @BeforeClass
    public static void setupRx() {
        RxJavaHooks.reset();
        RxAndroidPlugins.getInstance().reset();

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
    public void setup() {
        mRequester = new GistListInteractor(_repo);
        assertNotNull(mRequester);
    }

    @Test
    public void successFromCache() {
        simpleRequest();
    }

    @Test
    public void successFromCacheAndLoad() {
        TestSubscriber<List<GistModel>> subscriber = simpleRequest();

        GistLocal now = new GistLocal("id2", "url2", "created2", "desc2");
        _database.put()
                .object(now)
                .prepare()
                .executeAsBlocking();

        subscriber.assertValueCount(2);

        List<GistModel> second = subscriber.getOnNextEvents().get(1);
        assertEquals(2, second.size());
        assertEquals(now.id, second.get(1).id);
    }

    @Test
    public void accumulatorTest() {
        simpleRequest();

        List<GistModel> accumulator = mRequester.accumulator();
        assertNotNull(accumulator);
        assertEquals(1, accumulator.size());

        setup();
        accumulator = mRequester.accumulator();
        assertNotNull(accumulator);
        assertEquals(0, accumulator.size());
    }

    @Test
    public void serverLoadTest() {
        GistJson server = Data.SERVER_STUB;
        String serverId = "id3";
        server.id = serverId;
        Observable<List<GistJson>> serverObs = Observable.just(Collections.singletonList(server));
        Mockito.when(_client.publicGist(2)).thenReturn(serverObs);

        Observable<List<GistModel>> obs = mRequester.request(30);
        TestSubscriber<List<GistModel>> subscriber = new TestSubscriber<>();
        obs.subscribe(subscriber);

        subscriber.assertValueCount(1);
        subscriber.assertNotCompleted();
        List<List<GistModel>> events = subscriber.getOnNextEvents();
        List<GistModel> first = events.get(0);
        assertEquals(1, first.size());
        assertEquals(serverId, first.get(0).id);
    }

    private TestSubscriber<List<GistModel>> simpleRequest() {
        Observable<List<GistModel>> obs = mRequester.request(0);
        TestSubscriber<List<GistModel>> subscriber = new TestSubscriber<>();
        obs.subscribe(subscriber);

        subscriber.assertValueCount(1);
        subscriber.assertNotCompleted();
        List<List<GistModel>> events = subscriber.getOnNextEvents();
        List<GistModel> first = events.get(0);
        assertEquals(1, first.size());

        return subscriber;
    }

}