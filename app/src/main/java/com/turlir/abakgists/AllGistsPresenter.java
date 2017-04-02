package com.turlir.abakgists;


import com.turlir.abakgists.base.BasePresenter;
import com.turlir.abakgists.model.Gist;
import com.turlir.abakgists.network.ApiClient;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class AllGistsPresenter extends BasePresenter<AllGistsFragment> {

    private static final int PAGE_SIZE = 30;

    private final ApiClient mClient;

    public AllGistsPresenter(ApiClient client) {
        mClient = client;
    }

    void loadPublicGists(int currentSize) {
        currentSize = Math.max(currentSize, PAGE_SIZE);
        int page = currentSize / PAGE_SIZE;
        mClient.publicGist(page)
                .compose(this.<List<Gist>>subscribeIo())
                .compose(this.<List<Gist>>observeMain())
                .subscribe(new Handler<List<Gist>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        add(d);
                    }

                    @Override
                    public void onNext(List<Gist> value) {
                        if (getView() != null) {
                            if (value.size() < PAGE_SIZE) {
                                // достигнут конец списка
                                getView().stopGistLoad();
                            } else {
                                getView().onGistLoaded(value);
                            }
                        }
                    }
                });
    }

}
