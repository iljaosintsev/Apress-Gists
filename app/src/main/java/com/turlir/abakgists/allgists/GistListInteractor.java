package com.turlir.abakgists.allgists;

import com.turlir.abakgists.allgists.loader.LoadablePage;
import com.turlir.abakgists.allgists.loader.Window;
import com.turlir.abakgists.allgists.loader.WindowedRepository;
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

public class GistListInteractor extends WindowedRepository<GistModel> {

    private final Repository mRepo;
    private final ListGistMapper.Local mTransformer = new ListGistMapper.Local(new GistMapper.Local());

    public GistListInteractor(Repository repo, Window start) {
        super(start);
        mRepo = repo;
    }

    @Override
    public Flowable<List<GistModel>> firstPage() {
        return mRepo.database(range.count(), range.start())
                .map(mTransformer)
                .doOnNext(gistModels -> {
                    Timber.d("from database (first time) loaded %d items, from %d in %d",
                            gistModels.size(), range.start(), range.stop());
                    if (gistModels.size() == 0) {
                        Timber.d("needs load first %d items from server", range.count());
                    }
                })
                .doOnComplete(() -> {
                    Timber.d("database subscription complete");
                })
                .doOnError(Timber::e)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Flowable<List<GistModel>> nextPage() {
        range = range.next();
        return mRepo.database(range.count(), range.start())
                .map(mTransformer)
                .doOnNext(gistModels -> {
                    Timber.d("next page consist of %d items from database, %d - %d",
                            gistModels.size(), range.start(), range.stop());
                })
                .doOnComplete(() -> {
                    Timber.d("database subscription complete");
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(Timber::e);
    }

    @Override
    public Single<Integer> server(LoadablePage page) {
        return mRepo.server(page.number, page.size)
                .doOnSuccess(count -> Timber.d("from server loaded %d items", count))
                .doOnError(e -> Timber.e(e, "data error"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Flowable<List<GistModel>> prevPage() {
        range = range.prev();
        return mRepo.database(range.count(), range.start())
                .map(mTransformer)
                .doOnNext(gistModels -> {
                    Timber.d("prev page consist of %d items from database, %d - %d",
                            gistModels.size(), range.start(), range.stop());
                })
                .doOnComplete(() -> {
                    Timber.d("database subscription complete");
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(Timber::e);
    }

    @Override
    public Single<Integer> loadAndReplace(Window start) {
        LoadablePage page = start.page();
        range = start;
        return mRepo.reloadAllGist(page.number, page.size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<GistModel>> requestWithNotes() {
        return mRepo.notes()
                .map(gistLocals -> {
                    boolean tmp = mTransformer.isLocal();
                    mTransformer.setLocal(true);
                    List<GistModel> res = mTransformer.apply(gistLocals);
                    mTransformer.setLocal(tmp);
                    return res;
                });
    }

    @Override
    protected int computeApproximateSize() {
        //return range.stop() - range.addition();
        return range.addition();
    }
}
