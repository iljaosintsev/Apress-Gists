package com.turlir.abakgists.allgists;

import com.pushtorefresh.storio.sqlite.operations.put.PutResults;
import com.turlir.abakgists.api.Repository;
import com.turlir.abakgists.api.data.GistLocal;
import com.turlir.abakgists.model.GistModel;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class ModelRequester {

    private final Repository mRepo;
    private final List<GistModel> mData;

    private boolean isLocal;

    public ModelRequester(Repository repo) {
        mRepo = repo;
        isLocal = true;
        mData = new ArrayList<>();
    }

    public Observable<List<GistModel>> request(final int size) {
        if (size == 0) {
            mData.clear();
            isLocal = true;
        }

        return mRepo.loadGists()
                .switchMap(new Func1<List<GistLocal>, Observable<List<GistLocal>>>() {
                    @Override
                    public Observable<List<GistLocal>> call(List<GistLocal> gistModels) {
                        if (gistModels.size() < size + 1) {
                            return server(size);
                        } else {
                            return Observable.just(gistModels);
                        }
                    }
                })
                .map(new Func1<List<GistLocal>, List<GistModel>>() {
                    @Override
                    public List<GistModel> call(List<GistLocal> gistLocals) {
                        final int originCacheSize = mData.size();
                        boolean isSingleChangeDetected = false;
                        for (int i = 0; i < gistLocals.size(); i++) {
                            GistLocal item = gistLocals.get(i);
                            if (i + 1 > originCacheSize) {
                                GistModel m = new GistModel(item, isLocal);
                                mData.add(m);
                            } else {
                                GistModel cache = mData.get(i);
                                if (!isSingleChangeDetected &&
                                        (!cache.description.equals(item.description) ||
                                                !cache.note.equals(item.note))
                                        ) {
                                    mData.set(i, new GistModel(item, cache.isLocal));
                                    isSingleChangeDetected = true;
                                }
                            }
                        }
                        return mData;
                    }
                });
    }

    public Observable<PutResults<GistLocal>> update() {
        isLocal = false;
        mData.clear();

        return mRepo.reloadGists();
    }

    private Observable<List<GistLocal>> server(int size) {
        isLocal = false;

        return mRepo.loadGistsFromServerAndPutCache(size);
    }

}
