package com.turlir.abakgists;


import com.turlir.abakgists.base.BasePresenter;
import com.turlir.abakgists.model.Gist;
import com.turlir.abakgists.network.ApiClient;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class AllGistsPresenter extends BasePresenter<AllGistsFragment> {

    private final ApiClient mClient;

    public AllGistsPresenter(ApiClient client) {
        mClient = client;
    }

    void loadPublicGists() {
        mClient.publicGist(0)
                .compose(this.<List<Gist>>onIo())
                .compose(this.<List<Gist>>toMain())
                .subscribe(new Handler<List<Gist>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        add(d);
                    }

                    @Override
                    public void onNext(List<Gist> value) {
                        if (getView() != null) {
                            getView().onGist(value);
                        }
                    }

                });
    }

}
