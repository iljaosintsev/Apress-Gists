package com.turlir.abakgists;

import android.content.Context;
import android.content.ContextWrapper;

import com.google.common.io.Files;
import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.turlir.abakgists.api.ApiClient;
import com.turlir.abakgists.api.GistDatabaseHelper;
import com.turlir.abakgists.api.GistLocalStorIoLogPutResolver;
import com.turlir.abakgists.api.data.GistLocal;
import com.turlir.abakgists.api.data.GistLocalStorIOSQLiteDeleteResolver;
import com.turlir.abakgists.api.data.GistLocalStorIOSQLiteGetResolver;
import com.turlir.abakgists.di.AppComponent;
import com.turlir.abakgists.di.AppModule;
import com.turlir.abakgists.di.DatabaseModule;
import com.turlir.abakgists.di.PresenterModule;
import com.turlir.abakgists.model.GistsTable;

import org.robolectric.RuntimeEnvironment;

import java.io.File;
import java.io.IOException;

import it.cosenonjaviste.daggermock.DaggerMockRule;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DatabaseMocking extends DaggerMockRule<AppComponent> {

    public DatabaseMocking() {
        super(
                AppComponent.class,
                new AppModule(RuntimeEnvironment.application),
                new DatabaseModule(),
                new PresenterModule()
        );

        GistDatabaseHelper helper = makeHelper("/test.sql");
        SQLiteTypeMapping<GistLocal> typeMapping = SQLiteTypeMapping.<GistLocal>builder()
                .putResolver(new GistLocalStorIoLogPutResolver()) // logger
                .getResolver(new GistLocalStorIOSQLiteGetResolver())
                .deleteResolver(new GistLocalStorIOSQLiteDeleteResolver())
                .build();
        DefaultStorIOSQLite instance = DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(helper)
                .addTypeMapping(GistLocal.class, typeMapping)
                .build();
        provides(StorIOSQLite.class, instance);

        providesMock(ApiClient.class);
    }

    private GistDatabaseHelper makeHelper(final String name) {
        substitutionDatabase(name, GistsTable.BASE_NAME);
        return new GistDatabaseHelper(RuntimeEnvironment.application);
    }

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
