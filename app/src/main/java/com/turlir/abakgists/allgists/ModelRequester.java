package com.turlir.abakgists.allgists;

import com.pushtorefresh.storio.sqlite.operations.put.PutResults;
import com.turlir.abakgists.data.Repository;
import com.turlir.abakgists.model.GistModel;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class ModelRequester {

    private final Repository mRepo;
    private boolean isLocal;

    public ModelRequester(Repository repo) {
        mRepo = repo;
        isLocal = true;
    }

    public Observable<List<GistModel>> request(final int size) {
        if (size == 0) isLocal = true;

        return mRepo.loadGists()
                .switchMap(new Func1<List<GistModel>, Observable<List<GistModel>>>() {
                    @Override
                    public Observable<List<GistModel>> call(List<GistModel> gistModels) {
                        if (gistModels.size() < size + 1) {
                            return server(size);
                        } else {
                            return Observable.just(gistModels);
                        }
                    }
                })
                .doOnNext(new Action1<List<GistModel>>() {
                    @Override
                    public void call(List<GistModel> gistModels) {
                        for (int i = Math.max(0, size); i < gistModels.size(); i++) {
                            GistModel item = gistModels.get(i);
                            item.isLocal = isLocal;
                        }
                    }
                });
    }

    public Observable<PutResults<GistModel>> update() {
        isLocal = false;
        return mRepo.reloadGists();
    }

    private Observable<List<GistModel>> server(int size) {
        isLocal = false;
        return mRepo.loadGistsFromServerAndPutCache(size);
    }

}
