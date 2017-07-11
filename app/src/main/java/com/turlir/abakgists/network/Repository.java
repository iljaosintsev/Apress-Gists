package com.turlir.abakgists.network;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResult;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.turlir.abakgists.model.GistModel;
import com.turlir.abakgists.model.ListGistToModelMapper;
import com.turlir.abakgists.model.GistsTable;

import java.util.List;

import rx.Completable;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class Repository {

    private static final float PAGE_SIZE = 30;

    private ApiClient mClient;
    private StorIOSQLite mDatabase;

    public Repository(ApiClient client, StorIOSQLite base) {
        mClient = client;
        mDatabase = base;
    }

    public Observable<List<GistModel>> loadGistsFromServer(int currentSize) {
        int page = Math.round(currentSize / PAGE_SIZE) + 1;
        return mClient
                .publicGist(page)
                .doOnNext(new LagSideEffect(2500))
                .subscribeOn(Schedulers.io())
                .map(new ListGistToModelMapper());
    }

    public Observable<PutResults<GistModel>> loadGistsFromServerAndPutCache(int currentSize) {
        return loadGistsFromServer(currentSize)
                .flatMap(new Func1<List<GistModel>, Observable<PutResults<GistModel>>>() {
                    @Override
                    public Observable<PutResults<GistModel>> call(List<GistModel> gists) {
                        return putGistsToCache(gists);
                    }
                });
    }

    public Observable<PutResults<GistModel>> putGistsToCache(List<GistModel> gists) {
        return mDatabase.put()
                .objects(gists)
                .prepare()
                .asRxObservable();
    }

    public Observable<List<GistModel>> loadGistsFromCache(int currentSize) {
        return mDatabase.get()
                .listOfObjects(GistModel.class)
                .withQuery(
                        Query.builder()
                                .table(GistsTable.GISTS)
                                .limit(currentSize, (int) PAGE_SIZE)
                                .build()
                )
                .prepare()
                .asRxObservable();
    }

    public Observable<DeleteResult> clearCache() {
        return mDatabase.delete()
                .byQuery(
                        DeleteQuery
                                .builder()
                                .table(GistsTable.GISTS)
                                .build()
                )
                .prepare()
                .asRxObservable();
    }

    /**
     * Like a network lag in {@code millis} millisecond
     */
    private static class LagSideEffect implements Action1<Object> {

        private final int mLag;

        private LagSideEffect(int millis) {
            mLag = millis;
        }

        @Override
        public void call(Object data) {
            try {
                Thread.sleep(mLag);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
