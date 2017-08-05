package com.turlir.abakgists.data;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import com.google.common.io.Files;
import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;
import com.turlir.abakgists.BuildConfig;
import com.turlir.abakgists.Data;
import com.turlir.abakgists.api.ApiClient;
import com.turlir.abakgists.api.GistDatabaseHelper;
import com.turlir.abakgists.api.GistLocalStorIoLogPutResolver;
import com.turlir.abakgists.api.Repository;
import com.turlir.abakgists.api.data.GistJson;
import com.turlir.abakgists.api.data.GistLocal;
import com.turlir.abakgists.api.data.GistLocalStorIOSQLiteDeleteResolver;
import com.turlir.abakgists.api.data.GistLocalStorIOSQLiteGetResolver;
import com.turlir.abakgists.api.data.GistOwnerJson;
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
import java.util.Set;

import rx.Observable;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.functions.Func1;
import rx.observers.TestSubscriber;
import rx.plugins.RxJavaHooks;
import rx.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP, packageName = "com.turlir.abakgists")
public class RepositoryTest {

    private Repository mRepo;
    private ApiClient mockApi;

    @BeforeClass
    public static void setupRxHooks() throws Throwable {
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
        GistDatabaseHelper helper = makeHelper("/test.sql");

        SQLiteTypeMapping<GistLocal> typeMapping = SQLiteTypeMapping.<GistLocal>builder()
                .putResolver(new GistLocalStorIoLogPutResolver()) // logger
                .getResolver(new GistLocalStorIOSQLiteGetResolver())
                .deleteResolver(new GistLocalStorIOSQLiteDeleteResolver())
                .build();

        DefaultStorIOSQLite storio = DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(helper)
                .addTypeMapping(GistLocal.class, typeMapping)
                .build();

        mockApi = Mockito.mock(ApiClient.class);
        mRepo = new Repository(mockApi, storio);
    }

    @Test
    public void successFromCache() {
        Observable<List<GistLocal>> cacheObs = mRepo.load();
        TestSubscriber<List<GistLocal>> subs = new TestSubscriber<>();
        cacheObs.subscribe(subs);

        subs.assertNoErrors();
        subs.assertNotCompleted();
        subs.assertValueCount(1);

        List<List<GistLocal>> events = subs.getOnNextEvents();
        assertEquals(1, events.size());
        List<GistLocal> gists = events.get(0);
        assertEquals(1, gists.size());
        assertEquals(Data.LOCAL_STUB, gists.get(0));
    }

    @Test
    public void reloadTest() {
        GistOwnerJson owner = new GistOwnerJson("login", "avatarurl");
        GistJson gist = new GistJson("id", "url", "created", "desc", owner);
        List<GistJson> serverList = Collections.singletonList(gist);

        Observable<List<GistJson>> serverObs = Observable.just(serverList);
        Mockito.when(mockApi.publicGist(1)).thenReturn(serverObs);

        Observable<PutResults<GistLocal>> server = mRepo.reload();
        TestSubscriber<PutResults<GistLocal>> test = new TestSubscriber<>();
        server.subscribe(test);

        test.assertNoErrors();
        test.assertValueCount(1);
        test.assertCompleted();
        List<PutResults<GistLocal>> events = test.getOnNextEvents();
        Set<GistLocal> first = events.get(0).results().keySet();
        assertEquals(serverList.size(), first.size()); // с пустым списком
    }

    @Test
    public void reloadAfterLoadTest() {
        Observable<List<GistLocal>> cacheObs = mRepo.load();
        TestSubscriber<List<GistLocal>> cacheSubs = new TestSubscriber<>();
        cacheObs.subscribe(cacheSubs);
        cacheSubs.assertValueCount(1);

        GistJson stub = new GistJson(Data.SERVER_STUB);
        stub.description = "new desc";
        List<GistJson> serverList = Collections.singletonList(stub);
        Observable<List<GistJson>> serverObs = Observable.just(serverList);
        Mockito.when(mockApi.publicGist(1)).thenReturn(serverObs);

        Observable<PutResults<GistLocal>> obs = mRepo.reload();
        TestSubscriber<PutResults<GistLocal>> serverSubs = new TestSubscriber<>();
        obs.subscribe(serverSubs);

        serverSubs.assertNoErrors();
        serverSubs.assertCompleted();
        serverSubs.assertValueCount(1);
        List<PutResults<GistLocal>> events = serverSubs.getOnNextEvents();
        Set<Map.Entry<GistLocal, PutResult>> entries = events.get(0).results().entrySet();
        for (Map.Entry<GistLocal, PutResult> entry : entries) {
            PutResult value = entry.getValue();
            //assertEquals(Long.valueOf(-1), value.insertedId()); // Robolectric bug ?
        }

        // всего в cacheSubs пришли три набора
        cacheSubs.assertNotCompleted();
        cacheSubs.assertValueCount(1 + 2);
        List<List<GistLocal>> cacheEvents = cacheSubs.getOnNextEvents();

        List<GistLocal> second = cacheEvents.get(1);
        assertTrue(second.isEmpty());

        List<GistLocal> first = cacheEvents.get(0);
        List<GistLocal> three = cacheEvents.get(2);
        assertNotEquals(first, three);

        GistLocal old = new GistLocal(Data.LOCAL_STUB);
        old.note = "";
        old.description = "new desc";
        assertEquals(old, three.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroPageExceptionTest() {
        mRepo.server(0);
    }

    @Test
    public void successFromServerTest() {
        GistJson stub = new GistJson(Data.SERVER_STUB);
        List<GistJson> serverList = Collections.singletonList(stub);
        Observable<List<GistJson>> serverObs = Observable.just(serverList);
        Mockito.when(mockApi.publicGist(1)).thenReturn(serverObs);

        Observable<List<GistLocal>> obs = mRepo.server(1);
        TestSubscriber<List<GistLocal>> subscriber = new TestSubscriber<>();
        obs.subscribe(subscriber);

        subscriber.assertCompleted();
        subscriber.assertValueCount(1);
        List<List<GistLocal>> events = subscriber.getOnNextEvents();
        List<GistLocal> first = events.get(0);
        assertEquals(1, first.size());

        GistLocal local = new GistLocal(
                "85547e4878dd9a573215cd905650f284",
                "https://api.github.com/gists/85547e4878dd9a573215cd905650f284",
                "2017-04-27T21:54:24Z",
                "Part of setTextByParts",
                "",
                "iljaosintsev",
                "https://avatars1.githubusercontent.com/u/3526847?v=3"
        );
        assertEquals(local, first.get(0));
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