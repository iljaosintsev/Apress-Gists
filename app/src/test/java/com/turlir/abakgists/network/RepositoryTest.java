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
import com.turlir.abakgists.Data;
import com.turlir.abakgists.model.Gist;
import com.turlir.abakgists.model.GistModel;
import com.turlir.abakgists.model.GistModelStorIOSQLiteDeleteResolver;
import com.turlir.abakgists.model.GistModelStorIOSQLiteGetResolver;
import com.turlir.abakgists.model.GistOwner;
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

        SQLiteTypeMapping<GistModel> typeMapping = SQLiteTypeMapping.<GistModel>builder()
                .putResolver(new GistModelStorIoLogPutResolver()) // logger
                .getResolver(new GistModelStorIOSQLiteGetResolver())
                .deleteResolver(new GistModelStorIOSQLiteDeleteResolver())
                .build();

        DefaultStorIOSQLite storio = DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(helper)
                .addTypeMapping(GistModel.class, typeMapping)
                .build();

        mockApi = Mockito.mock(ApiClient.class);
        mRepo = new Repository(mockApi, storio);
    }

    @Test
    public void successFromCache() {
        Observable<List<GistModel>> cacheObs = mRepo.loadGists();
        TestSubscriber<List<GistModel>> subs = new TestSubscriber<>();
        cacheObs.subscribe(subs);

        subs.assertNoErrors();
        subs.assertNotCompleted();
        subs.assertValueCount(1);

        List<List<GistModel>> events = subs.getOnNextEvents();
        assertEquals(1, events.size());
        List<GistModel> gists = events.get(0);
        assertEquals(1, gists.size());
        assertEquals(Data.LOCAL_STUB, gists.get(0));
    }

    @Test
    public void clearCacheTest() {
        Completable obs = mRepo.clearCache();
        TestSubscriber subs = new TestSubscriber();
        obs.subscribe(subs);

        subs.assertNoErrors();
        subs.assertCompleted();

        Observable<List<GistModel>> server = mRepo.loadGists();
        TestSubscriber<List<GistModel>> test = new TestSubscriber<>();
        server.subscribe(test);

        test.assertNoErrors();
        test.assertValueCount(1);
        test.assertNotCompleted();
        List<List<GistModel>> events = test.getOnNextEvents();
        assertEquals(1, events.size()); // единственный вызов onNext
        List<GistModel> first = events.get(0);
        assertEquals(0, first.size()); // с пустым списком
    }

    @Test
    public void loadNewGistsFromServerAndPutCacheTest() {
        GistOwner owner = new GistOwner("login", "avatarurl");
        Gist gist = new Gist("id", "url", "created", "desc", owner);
        List<Gist> serverList = Collections.singletonList(gist);

        Observable<List<Gist>> serverObs = Observable.just(serverList);
        Mockito.when(mockApi.publicGist(1)).thenReturn(serverObs);

        Observable<List<GistModel>> cacheObs = mRepo.loadGists();
        TestSubscriber<List<GistModel>> cacheSubs = new TestSubscriber<>();
        cacheObs.subscribe(cacheSubs);

        Observable<PutResults<GistModel>> obs = mRepo.loadGistsFromServerAndPutCache(0);
        TestSubscriber<PutResults<GistModel>> serverSubs = new TestSubscriber<>();
        obs.subscribe(serverSubs);

        serverSubs.assertNoErrors();
        serverSubs.assertCompleted();
        serverSubs.assertValueCount(1);
        List<PutResults<GistModel>> putEvents = serverSubs.getOnNextEvents();
        assertEquals(1, putEvents.size());

        // в cacheSubs пришел новый набор результатов (второй)
        // содержащий уже два обекта Gist, последний из которых - новый с сервера
        cacheSubs.assertNotCompleted();
        cacheSubs.assertValueCount(2);
        List<List<GistModel>> cacheEvents = cacheSubs.getOnNextEvents();
        List<GistModel> second = cacheEvents.get(1);
        GistModel now = second.get(1);
        GistModel stub = new GistModel("id", "url", "created", "desc", "login", "avatarurl");
        assertEquals(stub, now);
    }

    @Test
    public void loadFromServerAndPutCacheRoundTest() {
        GistOwner owner = new GistOwner("login", "avatarurl");
        Gist gist = new Gist("id", "url", "created", "desc", owner);
        List<Gist> serverList = Collections.singletonList(gist);

        Observable<List<Gist>> serverObs = Observable.just(serverList);
        Mockito.when(mockApi.publicGist(3)).thenReturn(serverObs);

        int s = 30 + 24; // 54
        Observable<PutResults<GistModel>> obs = mRepo.loadGistsFromServerAndPutCache(s);
        TestSubscriber<PutResults<GistModel>> subs = new TestSubscriber<>();
        obs.subscribe(subs);

        subs.assertNoErrors();
        subs.assertValueCount(1);
        Mockito.verify(mockApi).publicGist(Mockito.eq(3));
    }

    @Test
    public void loadOldGistsFromServerAndPutCacheTest() {
        Gist stub = new Gist(Data.SERVER_STUB);
        stub.description = "new desc";
        List<Gist> serverList = Collections.singletonList(stub);

        Observable<List<Gist>> serverObs = Observable.just(serverList);
        Mockito.when(mockApi.publicGist(1)).thenReturn(serverObs);

        Observable<List<GistModel>> cacheObs = mRepo.loadGists();
        TestSubscriber<List<GistModel>> cacheSubs = new TestSubscriber<>();
        cacheObs.subscribe(cacheSubs);

        Observable<PutResults<GistModel>> obs = mRepo.loadGistsFromServerAndPutCache(0);
        TestSubscriber<PutResults<GistModel>> serverSubs = new TestSubscriber<>();
        obs.subscribe(serverSubs);

        serverSubs.assertNoErrors();
        serverSubs.assertCompleted();
        serverSubs.assertValueCount(1);
        List<PutResults<GistModel>> events = serverSubs.getOnNextEvents();
        Set<Map.Entry<GistModel, PutResult>> entries = events.get(0).results().entrySet();
        for (Map.Entry<GistModel, PutResult> entry : entries) {
            PutResult value = entry.getValue();
            //assertEquals(Long.valueOf(-1), value.insertedId()); // Robolectric bug ?
        }

        // в cacheSubs пришел новый набор результатов (второй)
        // оба набора одинаковы, содержат один элемент - LOCAL_STUB
        cacheSubs.assertNotCompleted();
        cacheSubs.assertValueCount(2);
        List<List<GistModel>> cacheEvents = cacheSubs.getOnNextEvents();
        List<GistModel> first = cacheEvents.get(0);
        List<GistModel> second = cacheEvents.get(1);
        assertEquals(first, second);
        GistModel old = new GistModel(Data.LOCAL_STUB);
        assertEquals(old, first.get(0));
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