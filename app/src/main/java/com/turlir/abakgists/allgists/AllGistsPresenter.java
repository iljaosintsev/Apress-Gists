package com.turlir.abakgists.allgists;


import com.turlir.abakgists.base.BasePresenter;
import com.turlir.abakgists.model.Gist;
import com.turlir.abakgists.network.Repository;

import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AllGistsPresenter extends BasePresenter<AllGistsFragment> {

    private final Repository mRepo;

    public AllGistsPresenter(Repository repo) {
        mRepo = repo;
    }

    void loadPublicGists(final int currentSize) {
        Observable<List<Gist>> source = mRepo.loadGists(currentSize);
        Subscription subs = source
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Handler<List<Gist>>() {
                    @Override
                    public void onNext(List<Gist> value) {
                        if (getView() != null) {
                            getView().onGistLoaded(value, currentSize, Repository.PAGE_SIZE);
                        }
                    }
                });

        addSubscription(subs);
    }

}
