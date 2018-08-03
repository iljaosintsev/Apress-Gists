package com.turlir.abakgists.allgists;

import android.support.annotation.NonNull;

import com.turlir.abakgists.api.Repository;
import com.turlir.abakgists.api.data.GistMapper;
import com.turlir.abakgists.api.data.ListGistMapper;
import com.turlir.abakgists.model.GistModel;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class GistListInteractor {

    private final Repository mRepo;
    private final ListGistMapper.Local mTransformer = new ListGistMapper.Local(new GistMapper.Local());

    @NonNull
    Range range;

    public GistListInteractor(Repository repo) {
        mRepo = repo;
        range = new Range(0, 30);
    }

    public Flowable<List<GistModel>> subscribe() {
        range = new Range(0, 30);
        return mRepo.database(range.count(), range.absStart)
                .map(mTransformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(gistModels -> {
                    Timber.d("from database (first time) loaded %d items, from %d in %d",
                            gistModels.size(), range.absStart, range.absStop);
                    if (!range.isFull(gistModels.size())) {
                        Timber.d("needs load %d items for first page", range.count() - gistModels.size());
                    }
                })
                .doOnComplete(() -> {
                    Timber.d("database subscription complete");
                })
                .doOnError(Timber::e);
    }

    public Flowable<List<GistModel>> nextPage() {
        range = range.next();
        return mRepo.database(range.count(), range.absStart)
                .map(mTransformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(gistModels -> {
                    Timber.d("next page consist of %d items from database, %d - %d",
                            gistModels.size(), range.absStart, range.absStop);
                })
                .doOnNext(items -> {
                    Timber.d("database subscription complete");
                })
                .doOnError(Timber::e);
    }

    public Single<Integer> server(int page, int perPage) {
        return mRepo.server(page, perPage)
                .doOnSuccess(count -> Timber.d("from server loaded %d items", count))
                .doOnError(e -> Timber.e(e, "network error"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
