package com.turlir.abakgists.di;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.turlir.abakgists.model.GistModel;
import com.turlir.abakgists.model.GistModelStorIOSQLiteDeleteResolver;
import com.turlir.abakgists.model.GistModelStorIOSQLiteGetResolver;
import com.turlir.abakgists.data.GistDatabaseHelper;
import com.turlir.abakgists.data.GistModelStorIoLogPutResolver;

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
        SQLiteTypeMapping<GistModel> typeMapping = SQLiteTypeMapping.<GistModel>builder()
                .putResolver(new GistModelStorIoLogPutResolver()) // logger
                .getResolver(new GistModelStorIOSQLiteGetResolver())
                .deleteResolver(new GistModelStorIOSQLiteDeleteResolver())
                .build();

        return DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(sqLiteOpenHelper)
                .addTypeMapping(GistModel.class, typeMapping)
                .build();
    }

}
