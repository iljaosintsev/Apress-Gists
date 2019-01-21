package com.turlir.abakgists.all;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.turlir.abakgists.api.Repository;
import com.turlir.abakgists.api.data.GistLocal;
import com.turlir.abakgists.base.App;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

@InjectViewState
public class AllInOnePresenter extends MvpPresenter<AllInOneView> {

    @Inject
    Repository repo;

    private Disposable resource;

    AllInOnePresenter() {
        App.getComponent().inject(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (resource != null) {
            resource.dispose();
        }
    }

    void connectSearch(Observable<String> search) {
        resource = search.switchMapSingle((Function<String, Single<List<GistLocal>>>) repo::search)
                .map(gistLocals -> {
                    List<String> res = new ArrayList<>(gistLocals.size());
                    for (GistLocal item : gistLocals) {
                        res.add(String.format("%s\n%s", item.description, item.note));
                    }
                    return res;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(strings -> {
                    if (getViewState() != null) {
                        getViewState().onSearchResults(strings);
                    }
                });
    }
}
