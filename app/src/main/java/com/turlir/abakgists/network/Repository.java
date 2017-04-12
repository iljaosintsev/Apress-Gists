package com.turlir.abakgists.network;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.util.Log;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResolver;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;
import com.pushtorefresh.storio.sqlite.queries.InsertQuery;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.turlir.abakgists.model.Gist;
import com.turlir.abakgists.model.GistStorIOSQLitePutResolver;
import com.turlir.abakgists.model.GistsTable;

import java.util.List;

import rx.Completable;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class Repository {

    private static final int PAGE_SIZE = 30;

    private ApiClient mClient;
    private StorIOSQLite mDatabase;
    private PutResolver<Gist> mPutResolver = new SimpleInsertResolver();

    public Repository(ApiClient client, StorIOSQLite base) {
        mClient = client;
        mDatabase = base;
    }

    public Observable<PutResults<Gist>> loadGistsFromServerAndPutCache(int currentSize) {
        int page = currentSize / PAGE_SIZE + 1;
        return mClient
                .publicGist(page)
                .subscribeOn(Schedulers.io())
                .map(new Func1<List<Gist>, List<Gist>>() {
                    @Override
                    public List<Gist> call(List<Gist> gists) {
                        for (Gist item : gists) {
                            if (item.owner != null) {
                                item.ownerLogin = item.owner.login;
                                item.ownerAvatarUrl = item.owner.avatarUrl;
                            }
                        }
                        return gists;
                    }
                })
                .flatMap(new Func1<List<Gist>, Observable<PutResults<Gist>>>() {
                    @Override
                    public Observable<PutResults<Gist>> call(List<Gist> gists) {
                        return mDatabase.put()
                                .objects(gists)
                                .withPutResolver(mPutResolver)
                                .prepare()
                                .asRxObservable();
                    }
                });
    }

    public Observable<List<Gist>> loadGistsFromCache(int currentSize) {
        return mDatabase.get()
                .listOfObjects(Gist.class)
                .withQuery(
                        Query.builder()
                                .table(GistsTable.GISTS)
                                .limit(currentSize, PAGE_SIZE)
                                .build()
                )
                .prepare()
                .asRxObservable();
    }

    public Completable clearCache() {
        return mDatabase.delete()
                .byQuery(
                        DeleteQuery
                                .builder()
                                .table(GistsTable.GISTS)
                                .build()
                )
                .prepare()
                .asRxCompletable();
    }

    private static class SimpleInsertResolver extends GistStorIOSQLitePutResolver {

        @NonNull
        @Override
        public PutResult performPut(@NonNull StorIOSQLite storIOSQLite, @NonNull Gist object) {
            StorIOSQLite.LowLevel lowLevel = storIOSQLite.lowLevel();
            // for data consistency in concurrent environment, encapsulate Put Operation into transaction
            lowLevel.beginTransaction();

            try {
                PutResult putResult;
                ContentValues contentValues = mapToContentValues(object);

                InsertQuery insertQuery = mapToInsertQuery(object);
                long insertedId = lowLevel.insert(insertQuery, contentValues);
                // существующие элементы будут проигнорированы ядром базы данных
                // так как у поля ID задано ограничение UNIQUE ON CONFLICT IGNORE
                putResult = PutResult.newInsertResult(insertedId, insertQuery.table());

                // everything okay
                lowLevel.setTransactionSuccessful();
                Log.i("DATABASE", "processed " + object.id);
                return putResult;

            } finally {
                // in case of bad situations, db won't be affected
                lowLevel.endTransaction();
            }
        }

    }

}
