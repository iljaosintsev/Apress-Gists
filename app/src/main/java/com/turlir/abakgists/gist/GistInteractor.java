package com.turlir.abakgists.gist;

import android.net.Uri;

import com.turlir.abakgists.api.data.GistLocalDao;
import com.turlir.abakgists.api.data.GistMapper;
import com.turlir.abakgists.model.GistModel;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class GistInteractor {

    private final EqualsSolver mSolver;
    private final GistLocalDao mDao;
    private final GistMapper.Local mapper;

    @Nullable
    private GistModel content;

    public GistInteractor(EqualsSolver solver, GistLocalDao dao) {
        mSolver = solver;
        mDao = dao;
        mapper = new GistMapper.Local();
    }

    Single<GistModel> load(String id) {
        return mDao.byId(id)
                .delay(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(mapper)
                .doOnSuccess(model -> content = model);
    }

    @Nullable
    Uri insteadWebLink() {
        if (content != null) {
            return content.insteadWebLink();
        } else {
            return null;
        }
    }

    boolean isChange(String desc, String note) {
        if (content == null) {
            return false;
        }
        GistModel now = new GistModel(content, desc, note);
        return mSolver.solveModel(content, now);
    }

    Completable transact(String desc, String note) {
        if (content == null) {
            return Completable.error(new IllegalStateException());
        }
        return Completable.fromRunnable(() -> mDao.update(content.id, desc, note))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> {
                    Timber.v("update gist success");
                    content = new GistModel(content, desc, note);
                });
    }

    boolean possiblyDelete() {
        return content != null;
    }

    Completable delete() {
        if (content == null) {
            return Completable.error(new IllegalStateException());
        }
        return Completable.fromRunnable(() -> mDao.deleteById(content.id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
