package com.turlir.abakgists.network;


import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.turlir.abakgists.BuildConfig;
import com.turlir.abakgists.di.AppComponent;
import com.turlir.abakgists.di.AppModule;
import com.turlir.abakgists.di.DatabaseModule;
import com.turlir.abakgists.di.GistDatabaseHelper;
import com.turlir.abakgists.di.GistStorIoLogPutResolver;
import com.turlir.abakgists.model.Gist;
import com.turlir.abakgists.model.GistStorIOSQLiteDeleteResolver;
import com.turlir.abakgists.model.GistStorIOSQLiteGetResolver;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Verifier;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.File;
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
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP, packageName = "com.turlir.abakgists")
public class ModernRepoTest {

    @Rule
    public final DaggerMockRule<AppComponent> rule = new JUnitDaggerMockRule();

    @Rule
    public Verifier verifier = new Verifier() {
        @Override
        protected void verify() throws Throwable {
            assertNotNull(_repo);
            assertNotNull(_mockClient);
        }
    };

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

    @Test
    public void successFromCache() {
        Observable<List<Gist>> obs = _repo.loadGistsFromCache(0);

        TestSubscriber<List<Gist>> subs = new TestSubscriber<>();
        obs.subscribe(subs);
        subs.assertNoErrors();
        subs.assertValueCount(1);

        List<Gist> first = subs.getOnNextEvents().get(0);
        assertNotNull(first);
        assertEquals(1, first.size());

        Gist stub = new Gist(
                "85547e4878dd9a573215cd905650f284",
                "https://api.github.com/gists/85547e4878dd9a573215cd905650f284",
                "2017-04-27T21:54:24Z",
                "Part of setTextByParts",
                "note",
                "https://avatars1.githubusercontent.com/u/3526847?v=3",
                "iljaosintsev"
        );
        assertEquals(stub, first.get(0));
    }

    private static class JUnitDaggerMockRule extends DaggerMockRule<AppComponent> {

        JUnitDaggerMockRule() {
            super(
                    AppComponent.class,
                    new AppModule(RuntimeEnvironment.application),
                    new DatabaseModule()
            );

            GistDatabaseHelper helper = makeHelper("/test.sql");
            SQLiteTypeMapping<Gist> typeMapping = SQLiteTypeMapping.<Gist>builder()
                    .putResolver(new GistStorIoLogPutResolver()) // logger
                    .getResolver(new GistStorIOSQLiteGetResolver())
                    .deleteResolver(new GistStorIOSQLiteDeleteResolver())
                    .build();
            DefaultStorIOSQLite instance = DefaultStorIOSQLite.builder()
                    .sqliteOpenHelper(helper)
                    .addTypeMapping(Gist.class, typeMapping)
                    .build();
            provides(StorIOSQLite.class, instance);
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

}
