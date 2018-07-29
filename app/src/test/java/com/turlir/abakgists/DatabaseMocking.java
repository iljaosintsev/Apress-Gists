package com.turlir.abakgists;

import android.content.Context;
import android.content.ContextWrapper;

import com.google.common.io.Files;
import com.turlir.abakgists.api.ApiClient;
import com.turlir.abakgists.api.GistDatabaseHelper;
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

        // TODO mocking here

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
        boolean isDirExists = new File(parent).exists();
        assertTrue(isDirExists);

        File from = new File(filePath);
        try {
            Files.copy(from, to);
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

}
