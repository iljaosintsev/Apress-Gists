package com.turlir.abakgists.network;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.turlir.abakgists.model.Gist;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class Repository {

    public static final int PAGE_SIZE = 30;

    private ApiClient mClient;
    private StorIOSQLite mDatabase;

    public Repository(ApiClient client, StorIOSQLite base) {
        this.mClient = client;
        this.mDatabase = base;
    }

    public Observable<List<Gist>> loadGists(int currentSize) {

        int tmp = Math.max(currentSize, PAGE_SIZE);
        int page = tmp / PAGE_SIZE;

        final Observable<List<Gist>> serverData = mClient.publicGist(page)
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
                });

        final Observable<List<Gist>> cacheData = mDatabase.get()
                .listOfObjects(Gist.class)
                .withQuery(
                        Query.builder()
                                .table("gists")
                                .limit(currentSize, PAGE_SIZE)
                                .build()
                )
                .prepare()
                .asRxObservable();

        return cacheData
                .map(new Func1<List<Gist>, List<Gist>>() {
                    @Override
                    public List<Gist> call(List<Gist> gists) {
                        if (gists.size() < 1) {
                            gists = serverData.toBlocking().first();
                            mDatabase.put().objects(gists).prepare().executeAsBlocking();
                        }
                        return gists;
                    }
                });

    }
}
