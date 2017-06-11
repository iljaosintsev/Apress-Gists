package com.turlir.abakgists.network;

import android.content.ContentValues;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.queries.InsertQuery;
import com.turlir.abakgists.model.GistModel;
import com.turlir.abakgists.model.GistModelStorIOSQLitePutResolver;

import timber.log.Timber;

public class GistModelStorIoLogPutResolver extends GistModelStorIOSQLitePutResolver {

    @NonNull
    @Override
    public PutResult performPut(@NonNull StorIOSQLite storIOSQLite, @NonNull GistModel object) {
        StorIOSQLite.LowLevel lowLevel = storIOSQLite.lowLevel();

        ContentValues contentValues = mapToContentValues(object);
        InsertQuery insertQuery = mapToInsertQuery(object);

        lowLevel.beginTransaction();
        try {
            long insertedId = lowLevel.insertWithOnConflict(insertQuery, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
            PutResult putResult = PutResult.newInsertResult(insertedId, insertQuery.table());
            lowLevel.setTransactionSuccessful();

            if (insertedId > -1) Timber.i("processed %s", object.id);
            return putResult;

        } catch (SQLiteConstraintException e) {
            return PutResult.newInsertResult(-1, insertQuery.table());

        } finally {
            lowLevel.endTransaction();
        }

    }

}
