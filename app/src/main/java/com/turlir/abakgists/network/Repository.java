package com.turlir.abakgists.network;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.turlir.abakgists.model.Gist;
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

}
