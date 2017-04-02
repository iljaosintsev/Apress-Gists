package com.turlir.abakgists.di;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.turlir.abakgists.model.Gist;
import com.turlir.abakgists.model.GistStorIOSQLiteDeleteResolver;
import com.turlir.abakgists.model.GistStorIOSQLiteGetResolver;
import com.turlir.abakgists.model.GistStorIOSQLitePutResolver;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class DatabaseModule {

    private final GistStorIOSQLitePutResolver mLoggerResolver = new GistStorIOSQLitePutResolver() {
        @NonNull
        @Override
        public PutResult performPut(@NonNull StorIOSQLite storIOSQLite, @NonNull Gist object) {
            PutResult result = super.performPut(storIOSQLite, object);

            if (result.wasInserted()) {
                Long iid = result.insertedId();
                if (iid != null && iid != -1) {
                    Log.d("DATABASE", object.id + " inserted");
                }
            }
            return result;
        }
    };

    @Provides
    @Singleton
    public SQLiteOpenHelper provideSQLiteOpenHelper(@NonNull Context context) {
        return new GistDatabaseHelper(context);
    }

    @Provides
    @Singleton
    public StorIOSQLite provideStorIOSQLite(@NonNull SQLiteOpenHelper sqLiteOpenHelper) {
        SQLiteTypeMapping<Gist> typeMapping = SQLiteTypeMapping.<Gist>builder()
                .putResolver(mLoggerResolver)
                .getResolver(new GistStorIOSQLiteGetResolver())
                .deleteResolver(new GistStorIOSQLiteDeleteResolver())
                .build();

        return DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(sqLiteOpenHelper)
                .addTypeMapping(Gist.class, typeMapping)
                .build();
    }

}
