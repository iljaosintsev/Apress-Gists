package com.turlir.abakgists.allgists;

import android.os.Build;

import com.turlir.abakgists.BuildConfig;
import com.turlir.abakgists.Data;
import com.turlir.abakgists.DatabaseMocking;
import com.turlir.abakgists.api.ApiClient;
import com.turlir.abakgists.api.Repository;
import com.turlir.abakgists.api.data.GistJson;
import com.turlir.abakgists.api.data.GistLocal;
import com.turlir.abakgists.di.AppComponent;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.subscribers.TestSubscriber;
import it.cosenonjaviste.daggermock.DaggerMockRule;
import it.cosenonjaviste.daggermock.InjectFromComponent;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.O, packageName = "com.turlir.abakgists")
public class RepositoryTest {

    @Rule
    public final DaggerMockRule<AppComponent> rule = new DatabaseMocking();

    @InjectFromComponent
    private Repository _repo;

    @Mock
    private ApiClient _client;

    @Test
    public void roomNotificationAtInsert() {
        //List<GistLocal> first = partial.blockingFirst();
        //assertTrue(first.size() > 0);

        /*List<GistLocal> list = mDao.demo(15, 0);
        int size = list.size();
        assertTrue(size > 0);*/

        TestSubscriber<List<GistLocal>> check = _repo
                .database(15, 0)
                .test()
                .awaitCount(1)
                .assertValueAt(0, lst -> lst.get(0).equals(Data.LOCAL_STUB));

        Single<List<GistJson>> mockNetwork = Single.just(Collections.singletonList(Data.NEW_SERVER));
        Mockito.when(_client.publicGist(1, 15)).thenReturn(mockNetwork);

        _repo.server(1, 15)
                .test()
                .assertNoErrors()
                .assertComplete();

        check.awaitCount(2)
                .assertValueAt(1, lst -> lst.size() == 2);
    }

}