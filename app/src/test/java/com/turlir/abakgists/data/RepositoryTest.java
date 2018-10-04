package com.turlir.abakgists.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import com.google.common.io.Files;
import com.turlir.abakgists.AppDatabase;
import com.turlir.abakgists.BuildConfig;
import com.turlir.abakgists.Data;
import com.turlir.abakgists.api.ApiClient;
import com.turlir.abakgists.api.Repository;
import com.turlir.abakgists.api.data.GistJson;
import com.turlir.abakgists.api.data.GistLocal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.subscribers.TestSubscriber;

import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.O, packageName = "com.turlir.abakgists")
public class RepositoryTest {

    @Mock
    private ApiClient _client;

    private Repository _repo;

    private AppDatabase database;

    public RepositoryTest() {
        substitutionDatabase("/test.sql", "test.sqlite");
        database = Room
                .databaseBuilder(RuntimeEnvironment.application, AppDatabase.class, "test.sqlite")
                .allowMainThreadQueries()
                .build();
        SupportSQLiteDatabase sqlite = database.getOpenHelper().getReadableDatabase();
        String path = sqlite.getPath();
        System.out.println("Path to testing database: " + path);
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        _repo = new Repository(_client, database.gistDao());
    }

    @Test
    public void roomNotificationAtInsert() {
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

    private void substitutionDatabase(final String file, final String basename) {
        Context cnt = RuntimeEnvironment.application.getApplicationContext();
        String destinationPath = new ContextWrapper(cnt).getDatabasePath(basename).getAbsolutePath();
        File to = new File(destinationPath);
        File from = new File(getClass().getResource(file).getFile());
        try {
            Files.copy(from, to);
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}