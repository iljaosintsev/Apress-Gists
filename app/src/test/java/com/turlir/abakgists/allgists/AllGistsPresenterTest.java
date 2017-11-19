package com.turlir.abakgists.allgists;

import android.os.Build;

import com.turlir.abakgists.BuildConfig;
import com.turlir.abakgists.DatabaseMocking;
import com.turlir.abakgists.allgists.view.AllGistsFragment;
import com.turlir.abakgists.api.ApiClient;
import com.turlir.abakgists.api.Repository;
import com.turlir.abakgists.api.data.GistJson;
import com.turlir.abakgists.di.AppComponent;
import com.turlir.abakgists.model.GistModel;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
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
import rx.plugins.RxJavaHooks;
import rx.schedulers.Schedulers;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.O, packageName = "com.turlir.abakgists")
public class AllGistsPresenterTest {

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

    @Before
    public void checkInject() {
        assertNotNull(_repo);
        assertNotNull(_mockClient);
        assertNotNull(_presenter);

        mView = mock(AllGistsFragment.class);
        _presenter.attach(mView);
    }

    private void mockServerRequest() {
        GistJson stub = new GistJson("id", "url", "created", "desc");
        List<GistJson> serverList = Collections.singletonList(stub);
        Observable<List<GistJson>> serverObs = Observable.just(serverList);
        when(_mockClient.publicGist(eq(1))).thenReturn(serverObs);
    }

}