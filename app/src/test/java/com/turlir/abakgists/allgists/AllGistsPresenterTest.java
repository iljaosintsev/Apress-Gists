package com.turlir.abakgists.allgists;

import android.os.Build;

import com.turlir.abakgists.BuildConfig;
import com.turlir.abakgists.DatabaseMocking;
import com.turlir.abakgists.di.AppComponent;
import com.turlir.abakgists.model.Gist;
import com.turlir.abakgists.model.GistModel;
import com.turlir.abakgists.network.ApiClient;
import com.turlir.abakgists.network.Repository;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.cosenonjaviste.daggermock.DaggerMockRule;
import it.cosenonjaviste.daggermock.InjectFromComponent;
import rx.Observable;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.functions.Func1;
import rx.plugins.RxJavaHooks;
import rx.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP, packageName = "com.turlir.abakgists")
public class AllGistsPresenterTest {

    private static final GistModel LOCAL_STUB = new GistModel(
            "85547e4878dd9a573215cd905650f284",
            "https://api.github.com/gists/85547e4878dd9a573215cd905650f284",
            "2017-04-27T21:54:24Z",
            "Part of setTextByParts",
            "note",
            "iljaosintsev",
            "https://avatars1.githubusercontent.com/u/3526847?v=3"
    );

    @Rule
    public final DaggerMockRule<AppComponent> rule = new DatabaseMocking();

    @InjectFromComponent
    private Repository _repo;

    @InjectFromComponent
    private AllGistsPresenter _presenter;

    @Mock
    private ApiClient _mockClient;

    @Captor
    private ArgumentCaptor<List<GistModel>> mListCaptor;

    private AllGistsFragment mView;

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
        assertNotNull(_presenter);

        mView = mock(AllGistsFragment.class);
        _presenter.attach(mView);
    }

    @Test
    public void successFirstLoadFromCacheTest() {
        _presenter.loadPublicGists(0);
        verify(mView).onGistLoaded(Collections.singletonList(LOCAL_STUB), 0 , 1);
    }

    @Test
    public void successLoadFromServerTest() {
        Gist stub = new Gist("id", "url", "created", "desc");
        List<Gist> serverList = Collections.singletonList(stub);
        Observable<List<Gist>> serverObs = Observable.just(serverList);
        when(_mockClient.publicGist(eq(1))).thenReturn(serverObs);

        _presenter.loadPublicGists(1);

        // verify(_repo).loadGistsFromServerAndPutCache(eq(1));
        verify(_mockClient).publicGist(eq(1));

        verify(mView).onGistLoaded(mListCaptor.capture(), eq(1), eq(1));
        assertEquals(1, mListCaptor.getValue().size()); //
    }

    //@Test
    public void successFromCache() {
        Repository mock = mock(Repository.class);

        List<GistModel> list = new ArrayList<>();
        list.add(new GistModel("id", "url", "created", "desc"));
        Observable<List<GistModel>> resultObs = Observable.just(list);
        Mockito.when(mock.loadGistsFromCache(Mockito.anyInt())).thenReturn(resultObs);

        AllGistsPresenter presenter = new AllGistsPresenter(mock);

        AllGistsFragment mockView = mock(AllGistsFragment.class);
        presenter.attach(mockView);

        presenter.loadPublicGists(0);

        verify(mock).loadGistsFromCache(0);

        verify(mockView).onGistLoaded(list, 0, 1);
    }

}