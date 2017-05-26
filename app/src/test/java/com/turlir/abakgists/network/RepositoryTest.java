package com.turlir.abakgists.network;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import com.google.common.io.Files;
import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.turlir.abakgists.BuildConfig;
import com.turlir.abakgists.di.GistDatabaseHelper;
import com.turlir.abakgists.di.GistStorIoLogPutResolver;
import com.turlir.abakgists.model.Gist;
import com.turlir.abakgists.model.GistStorIOSQLiteDeleteResolver;
import com.turlir.abakgists.model.GistStorIOSQLiteGetResolver;
import com.turlir.abakgists.model.GistsTable;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.File;
import java.io.IOException;
import java.util.List;

import rx.Completable;
import rx.Observable;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.functions.Func1;
import rx.observers.TestSubscriber;
import rx.plugins.RxJavaHooks;
import rx.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP, packageName = "com.turlir.abakgists")
public class RepositoryTest {

    private Repository mRepo;

    @BeforeClass
    public static void setupRxHooks() throws Throwable {
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
    public void setup() throws IOException {
        GistDatabaseHelper helper = makeHelper("/test.sql");

        SQLiteTypeMapping<Gist> typeMapping = SQLiteTypeMapping.<Gist>builder()
                .putResolver(new GistStorIoLogPutResolver()) // logger
                .getResolver(new GistStorIOSQLiteGetResolver())
                .deleteResolver(new GistStorIOSQLiteDeleteResolver())
                .build();

        DefaultStorIOSQLite storio = DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(helper)
                .addTypeMapping(Gist.class, typeMapping)
                .build();

        mRepo = new Repository(Mockito.mock(ApiClient.class), storio);
    }

    @Test
    public void successFromCache() throws Exception {
        Observable<List<Gist>> cacheObs = mRepo.loadGistsFromCache(0);
        TestSubscriber<List<Gist>> subs = new TestSubscriber<>();
        cacheObs.subscribe(subs);

        subs.assertNoErrors();
        subs.assertNotCompleted();
        subs.assertValueCount(1);

        List<List<Gist>> events = subs.getOnNextEvents();
        assertEquals(1, events.size());
        List<Gist> gists = events.get(0);
        assertEquals(1, gists.size());
        Gist stub = new Gist(
                "85547e4878dd9a573215cd905650f284",
                "https://api.github.com/gists/85547e4878dd9a573215cd905650f284",
                "2017-04-27T21:54:24Z",
                "Part of setTextByParts",
                "note",
                "https://avatars1.githubusercontent.com/u/3526847?v=3",
                "iljaosintsev"
        );
        assertEquals(stub, gists.get(0));
    }

    @Test
    public void clearCacheTest() throws IOException {
        Completable obs = mRepo.clearCache();
        TestSubscriber subs = new TestSubscriber();
        obs.subscribe(subs);

        subs.assertNoErrors();
        subs.assertCompleted();

        Observable<List<Gist>> server = mRepo.loadGistsFromCache(0);
        TestSubscriber<List<Gist>> test = new TestSubscriber<>();
        server.subscribe(test);

        test.assertNoErrors();
        test.assertValueCount(1);
        test.assertNotCompleted();
        List<List<Gist>> events = test.getOnNextEvents();
        assertEquals(1, events.size()); // единственный вызов onNext
        List<Gist> first = events.get(0);
        assertEquals(0, first.size()); // с пустым списком
    }

    private GistDatabaseHelper makeHelper(final String name) {
        substitutionDatabase(name, GistsTable.BASE_NAME);
        return new GistDatabaseHelper(RuntimeEnvironment.application);
    }

    /**
     * Для каждого теста подменяется файл базы данных
     * @param file имя файла бд из ресурсов
     * @param basename имя базы данных
     */
    private void substitutionDatabase(final String file, final String basename) {
        String filePath = getClass().getResource(file).getFile();

        Context cnt = RuntimeEnvironment.application.getApplicationContext();
        String destinationPath = new ContextWrapper(cnt).getDatabasePath(basename).getAbsolutePath();
        File to = new File(destinationPath);
        String parent = to.getParent();
        boolean isDirCreate = new File(parent).mkdir();
        assertTrue(isDirCreate);

        File from = new File(filePath);
        try {
            Files.copy(from, to);
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

}