package com.turlir.abakgists.network;

import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.turlir.abakgists.BuildConfig;
import com.turlir.abakgists.di.GistDatabaseHelper;
import com.turlir.abakgists.di.GistStorIoLogPutResolver;
import com.turlir.abakgists.model.Gist;
import com.turlir.abakgists.model.GistStorIOSQLiteDeleteResolver;
import com.turlir.abakgists.model.GistStorIOSQLiteGetResolver;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.File;
import java.util.List;

import rx.Observable;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.functions.Func1;
import rx.observers.TestSubscriber;
import rx.plugins.RxJavaHooks;
import rx.schedulers.Schedulers;

import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP, packageName = "com.turlir.abakgists")
public class RepositoryTest {

    @Before
    public void setup() throws Throwable {
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
    public void first() throws Exception {
        GistDatabaseHelper helper = makeHelper("/test.sql");

        SQLiteTypeMapping<Gist> typeMapping = SQLiteTypeMapping.<Gist>builder()
                .putResolver(new GistStorIoLogPutResolver()) // logger
                .getResolver(new GistStorIOSQLiteGetResolver())
                .deleteResolver(new GistStorIOSQLiteDeleteResolver())
                .build();

        DefaultStorIOSQLite storIo = DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(helper)
                .addTypeMapping(Gist.class, typeMapping)
                .build();

        Repository repo = new Repository(Mockito.mock(ApiClient.class), storIo);
        Observable<List<Gist>> cacheObs = repo.loadGistsFromCache(0);
        TestSubscriber<List<Gist>> subscriber = new TestSubscriber<>();
        cacheObs.subscribe(subscriber);

        subscriber.assertNoErrors();
        subscriber.assertValueCount(1);

        storIo.put()
                .object(new Gist("url", "url", "created"))
                .prepare()
                .executeAsBlocking();

        subscriber.assertValueCount(2);

        storIo.close();
    }

    private GistDatabaseHelper makeHelper(String name) {
        GistDatabaseHelper helper = new GistDatabaseHelper(RuntimeEnvironment.application);
        GistDatabaseHelper spyHelper = Mockito.spy(helper);

        File dbFile = new File(getClass().getResource(name).getFile());
        assertTrue(dbFile.exists());
        SQLiteDatabase database = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, 1);
        Mockito.when(spyHelper.getReadableDatabase()).thenReturn(database);
        Mockito.when(spyHelper.getWritableDatabase()).thenReturn(database);

        return spyHelper;
    }

}