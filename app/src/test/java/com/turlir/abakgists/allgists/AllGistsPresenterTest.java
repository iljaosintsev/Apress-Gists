package com.turlir.abakgists.allgists;

import com.turlir.abakgists.model.Gist;
import com.turlir.abakgists.network.Repository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.functions.Func1;
import rx.plugins.RxJavaHooks;
import rx.schedulers.Schedulers;

public class AllGistsPresenterTest {

    @Before
    public void evaluate() throws Throwable {
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

    @Test
    public void successFromCache() {
        Repository mock = Mockito.mock(Repository.class);

        List<Gist> list = new ArrayList<>();
        list.add(new Gist("id", "url", "created", "desc"));
        Observable<List<Gist>> resultObs = Observable.just(list);
        Mockito.when(mock.loadGistsFromCache(Mockito.anyInt())).thenReturn(resultObs);

        AllGistsPresenter presenter = new AllGistsPresenter(mock);

        AllGistsFragment mockView = Mockito.mock(AllGistsFragment.class);
        presenter.attach(mockView);

        presenter.loadPublicGists(0);

        Mockito.verify(mock).loadGistsFromCache(0);

        Mockito.verify(mockView).onGistLoaded(list, 0, 1);
    }

}