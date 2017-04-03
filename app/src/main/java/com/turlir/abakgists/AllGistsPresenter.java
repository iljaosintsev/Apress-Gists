package com.turlir.abakgists;


import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.turlir.abakgists.base.BasePresenter;
import com.turlir.abakgists.model.Gist;
import com.turlir.abakgists.network.ApiClient;

import java.util.List;

public class AllGistsPresenter extends BasePresenter<AllGistsFragment> {

    private static final int PAGE_SIZE = 30;

    private final ApiClient mClient;
    private final StorIOSQLite mDatabase;

    public AllGistsPresenter(ApiClient client, StorIOSQLite database) {
        mClient = client;
        mDatabase = database;
    }

    void loadPublicGists(int currentSize) {
        currentSize = Math.max(currentSize, PAGE_SIZE);
        int page = currentSize / PAGE_SIZE;
        addSubscription(mClient.publicGist(page)
                .compose(this.<List<Gist>>defaultSchedule())
                .subscribe(new Handler<List<Gist>>() {

                    @Override
                    public void onNext(List<Gist> value) {
                        if (getView() != null) {
                            mDatabase.put()
                                    .objects(value)
                                    .prepare()
                                    .executeAsBlocking();
                            if (value.size() < PAGE_SIZE) {
                                // достигнут конец списка
                                getView().stopGistLoad();
                            } else {
                                getView().onGistLoaded(value);
                            }
                        }
                    }

                }));
    }

}
