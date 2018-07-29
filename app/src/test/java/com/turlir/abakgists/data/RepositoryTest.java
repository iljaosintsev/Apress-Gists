package com.turlir.abakgists.data;

import android.os.Build;

import com.turlir.abakgists.BuildConfig;
import com.turlir.abakgists.Data;
import com.turlir.abakgists.DatabaseMocking;
import com.turlir.abakgists.api.ApiClient;
import com.turlir.abakgists.api.Repository;
import com.turlir.abakgists.api.data.GistJson;
import com.turlir.abakgists.api.data.GistLocal;
import com.turlir.abakgists.di.AppComponent;

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
import rx.observers.TestSubscriber;
import rx.plugins.RxJavaHooks;
import rx.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.O, packageName = "com.turlir.abakgists")
public class RepositoryTest {

    @Rule
    public final DaggerMockRule<AppComponent> rule = new DatabaseMocking();

    @InjectFromComponent
    private Repository _repo;

    @Mock
    private ApiClient _mockClient;

    @BeforeClass
    public static void setupRxHooks() throws Throwable {
        RxJavaHooks.reset();
        RxAndroidPlugins.getInstance().reset();

        RxJavaHooks.setOnIOScheduler(scheduler -> Schedulers.immediate());
        RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {
            @Override
            public Scheduler getMainThreadScheduler() {
                return Schedulers.immediate();
            }
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroPageExceptionTest() {
        _repo.server(0);
    }

    @Test
    public void successFromServerTest() {
        GistJson stub = new GistJson(Data.SERVER_STUB);
        List<GistJson> serverList = Collections.singletonList(stub);
        Observable<List<GistJson>> serverObs = Observable.just(serverList);
        Mockito.when(_mockClient.publicGist(1)).thenReturn(serverObs);

        Observable<List<GistLocal>> obs = _repo.server(1);
        TestSubscriber<List<GistLocal>> subscriber = new TestSubscriber<>();
        obs.subscribe(subscriber);

        subscriber.assertCompleted();
        subscriber.assertValueCount(1);
        List<List<GistLocal>> events = subscriber.getOnNextEvents();
        List<GistLocal> first = events.get(0);
        assertEquals(1, first.size());

        GistLocal local = new GistLocal(
                "85547e4878dd9a573215cd905650f284",
                "https://api.github.com/gists/85547e4878dd9a573215cd905650f284",
                "2017-04-27T21:54:24Z",
                "Part of setTextByParts",
                "",
                "iljaosintsev",
                "https://avatars1.githubusercontent.com/u/3526847?v=3"
        );
        assertEquals(local, first.get(0));
    }

}