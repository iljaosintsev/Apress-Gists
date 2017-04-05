package com.turlir.abakgists.network;

import android.util.Log;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.turlir.abakgists.model.Gist;

import java.util.List;

import rx.Completable;
import rx.Observable;
import rx.functions.Func1;

public class Repository {

    private static final String TAG = "REPO";
    private static final int PAGE_SIZE = 30;

    private ApiClient mClient;
    private StorIOSQLite mDatabase;

    public Repository(ApiClient client, StorIOSQLite base) {
        this.mClient = client;
        this.mDatabase = base;
    }

    public Observable<List<Gist>> loadGists(int currentSize) {
        final int page = currentSize / PAGE_SIZE + 1;
        Observable<List<Gist>> cacheData = createCacheObservable(currentSize);
        return cacheData
                .flatMap(new Func1<List<Gist>, Observable<List<Gist>>>() {
                    @Override
                    public Observable<List<Gist>> call(List<Gist> gists) {
                        Log.d(TAG, "fromCache " + gists.size());
                        if (gists.size() < 1) {
                            return createServerObservable(page);
                        }
                        return Observable.just(gists);
                    }
                });
    }

    private Observable<List<Gist>> createCacheObservable(int currentSize) {
       return mDatabase.get()
                .listOfObjects(Gist.class)
                .withQuery(
                        Query.builder()
                                .table("gists")
                                .limit(currentSize, PAGE_SIZE)
                                .build()
                )
                .prepare()
                .asRxObservable(); // подписка на обновления
    }

    private Observable<List<Gist>> createServerObservable(int page) {
        return mClient.publicGist(page)
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
                        Log.d("REPO", "fromServerToCache " + gists.size());
                        return mDatabase.put()
                                .objects(gists)
                                .prepare()
                                .asRxObservable();
                    }
                })
                .skip(1)
                .concatMapIterable(new Func1<PutResults<Gist>, Iterable<Gist>>() {
                    @Override
                    public Iterable<Gist> call(PutResults<Gist> gistPutResults) {
                        return gistPutResults.results().keySet();
                    }
                })
                .toList();
    }

    public Completable clearCache() {
        return mDatabase.delete()
                .byQuery(
                        DeleteQuery
                                .builder()
                                .table("gists")
                                .build()
                )
                .prepare()
                .asRxCompletable();
    }
}
