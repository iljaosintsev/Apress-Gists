package com.turlir.abakgists;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.ContextWrapper;

import com.google.common.io.Files;
import com.turlir.abakgists.di.AppComponent;
import com.turlir.abakgists.di.AppModule;
import com.turlir.abakgists.di.DatabaseModule;
import com.turlir.abakgists.di.PresenterModule;

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

        substitutionDatabase("/test.sql", "test.sqlite");

        AppDatabase database = Room.databaseBuilder(RuntimeEnvironment.application, AppDatabase.class, "test.sqlite")
                .allowMainThreadQueries()
                .build();

        SupportSQLiteDatabase sqlite = database.getOpenHelper().getReadableDatabase();
        String path = sqlite.getPath();
        System.out.println("Path testing database " + path);
        provides(AppDatabase.class, database);
    }

    private void substitutionDatabase(final String file, final String basename) {
        Context cnt = RuntimeEnvironment.application.getApplicationContext();
        String destinationPath = new ContextWrapper(cnt).getDatabasePath(basename).getAbsolutePath();
        File to = new File(destinationPath);
        String parent = to.getParent();
        boolean isDirExists = new File(parent).exists();
        assertTrue(isDirExists);

        File from = new File(getClass().getResource(file).getFile());
        try {
            Files.copy(from, to);
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

}
