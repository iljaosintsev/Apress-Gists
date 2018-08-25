package com.turlir.abakgists.di;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.turlir.abakgists.AppDatabase;
import com.turlir.abakgists.api.GistDatabaseHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class DatabaseModule {

    @Provides
    @Singleton
    public SQLiteOpenHelper provideSQLiteOpenHelper(@NonNull Context context) {
        return new GistDatabaseHelper(context);
    }


    @Provides
    @Singleton
    public AppDatabase provideRoom(@NonNull Context cnt) {
        return Room.databaseBuilder(cnt, AppDatabase.class, "database-name").build();
    }
}
