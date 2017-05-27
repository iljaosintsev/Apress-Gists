package com.turlir.abakgists.network;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import com.google.common.io.Files;
import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;
import com.turlir.abakgists.BuildConfig;
import com.turlir.abakgists.di.GistDatabaseHelper;
import com.turlir.abakgists.di.GistStorIoLogPutResolver;
import com.turlir.abakgists.model.Gist;
import com.turlir.abakgists.model.GistOwner;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP, packageName = "com.turlir.abakgists")
public class RepositoryTest {

    private Repository mRepo;
    private ApiClient mockApi;

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
    public void setup() {
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

        mockApi = Mockito.mock(ApiClient.class);
        mRepo = new Repository(mockApi, storio);
    }

    @Test
    public void successFromCache() {
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
    public void clearCacheTest() {
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

    @Test
    public void loadNewGistsFromServerAndPutCacheTest() {
        GistOwner owner = new GistOwner("login", "avatarurl");
        Gist gist = new Gist("id", "url", "created", "desc", owner);
        List<Gist> serverList = Collections.singletonList(gist);

        Observable<List<Gist>> serverObs = Observable.just(serverList);
        Mockito.when(mockApi.publicGist(1)).thenReturn(serverObs);

        Observable<PutResults<Gist>> obs = mRepo.loadGistsFromServerAndPutCache(0);
        TestSubscriber<PutResults<Gist>> subs = new TestSubscriber<>();
        obs.subscribe(subs);

        subs.assertNoErrors();
        subs.assertCompleted();
        subs.assertValueCount(1);
        List<PutResults<Gist>> events = subs.getOnNextEvents();
        assertEquals(1, events.size());

        Map<Gist, PutResult> now = events.get(0).results();// new element, from server
        for (Map.Entry<Gist, PutResult> entry : now.entrySet()) { // only one
            Gist key = entry.getKey();
            Gist local = new Gist("id", "url", "created", "desc", null, "avatarurl", "login");
            assertEquals(local, key);

            PutResult value = entry.getValue();
            assertTrue(value.wasInserted());
            assertNotNull(value.affectedTables());
            assertFalse(value.affectedTables().isEmpty());
        }

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