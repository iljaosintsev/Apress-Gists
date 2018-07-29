package com.turlir.abakgists.allgists;

import android.os.Build;

import com.turlir.abakgists.BuildConfig;
import com.turlir.abakgists.Data;
import com.turlir.abakgists.DatabaseMocking;
import com.turlir.abakgists.api.ApiClient;
import com.turlir.abakgists.api.Repository;
import com.turlir.abakgists.api.data.GistJson;
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

import io.reactivex.Observable;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;
import it.cosenonjaviste.daggermock.DaggerMockRule;
import it.cosenonjaviste.daggermock.InjectFromComponent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.O, packageName = "com.turlir.abakgists")
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
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(__ -> Schedulers.trampoline());
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
    public void serverLoadTest() {
        GistJson server = Data.SERVER_STUB;
        String serverId = "id3";
        server.id = serverId;
        Observable<List<GistJson>> serverObs = Observable.just(Collections.singletonList(server));
        Mockito.when(_client.publicGist(2)).thenReturn(serverObs);

        Observable<List<GistModel>> obs = mRequester.request(30);
        TestObserver<List<GistModel>> subscriber = new TestObserver<>();
        obs.subscribe(subscriber);

        subscriber.assertValueCount(1);
        subscriber.assertNotComplete();
        List<List<GistModel>> events = subscriber.values();
        List<GistModel> first = events.get(0);
        assertEquals(1, first.size());
        assertEquals(serverId, first.get(0).id);
    }

    private TestObserver<List<GistModel>> simpleRequest() {
        Observable<List<GistModel>> obs = mRequester.request(0);
        TestObserver<List<GistModel>> subscriber = obs.test();

        subscriber.assertValueCount(1);
        subscriber.assertNotComplete();
        List<List<GistModel>> events = subscriber.values();
        List<GistModel> first = events.get(0);
        assertEquals(1, first.size());

        return subscriber;
    }

}