package com.turlir.abakgists.allgists;

import android.os.Build;

import com.turlir.abakgists.BuildConfig;
import com.turlir.abakgists.DatabaseMocking;
import com.turlir.abakgists.api.ApiClient;
import com.turlir.abakgists.api.Repository;
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
        Observable<List<GistModel>> obs = mRequester.request(0);
        TestSubscriber<List<GistModel>> subscriber = new TestSubscriber<>();
        obs.subscribe(subscriber);

        subscriber.assertValueCount(1);
        subscriber.assertNotCompleted();
        List<List<GistModel>> events = subscriber.getOnNextEvents();
        List<GistModel> first = events.get(0);
        assertEquals(1, first.size());
    }

}