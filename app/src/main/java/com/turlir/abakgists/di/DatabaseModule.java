package com.turlir.abakgists.di;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.turlir.abakgists.api.GistDatabaseHelper;
import com.turlir.abakgists.api.GistLocalStorIoLogPutResolver;
import com.turlir.abakgists.api.data.GistLocal;
import com.turlir.abakgists.api.data.GistLocalStorIOSQLiteDeleteResolver;
import com.turlir.abakgists.api.data.GistLocalStorIOSQLiteGetResolver;

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
    public StorIOSQLite provideStorIOSQLite(@NonNull SQLiteOpenHelper sqLiteOpenHelper) {
        SQLiteTypeMapping<GistLocal> typeMapping = SQLiteTypeMapping.<GistLocal>builder()
                .putResolver(new GistLocalStorIoLogPutResolver()) // logger
                .getResolver(new GistLocalStorIOSQLiteGetResolver())
                .deleteResolver(new GistLocalStorIOSQLiteDeleteResolver())
                .build();

        return DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(sqLiteOpenHelper)
                .addTypeMapping(GistLocal.class, typeMapping)
                .build();
    }

}
