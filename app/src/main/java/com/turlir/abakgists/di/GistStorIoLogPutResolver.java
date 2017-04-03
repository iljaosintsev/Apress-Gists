package com.turlir.abakgists.di;

import android.support.annotation.NonNull;
import android.util.Log;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.turlir.abakgists.model.Gist;
import com.turlir.abakgists.model.GistStorIOSQLitePutResolver;

class GistStorIoLogPutResolver extends GistStorIOSQLitePutResolver {

    private static final String TAG = "DATABASE";

    @NonNull
    @Override
    public PutResult performPut(@NonNull StorIOSQLite storIOSQLite, @NonNull Gist object) {
        PutResult result = super.performPut(storIOSQLite, object);

        if (result.wasInserted()) {
            Long iid = result.insertedId();
            if (iid != null && iid != -1) {
                Log.d(TAG, object.id + " inserted");
            }
        } else if (result.wasUpdated()){
            Log.d(TAG, object.id + " updated");
        }
        return result;
    }

}
