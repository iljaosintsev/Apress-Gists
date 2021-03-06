package com.turlir.abakgists.gist;

import androidx.annotation.NonNull;

import com.turlir.abakgists.api.data.GistLocalDao;
import com.turlir.abakgists.api.data.GistMapper;
import com.turlir.abakgists.model.GistModel;

import javax.annotation.Nullable;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class GistInteractor {

    private final GistLocalDao mDao;
    private final GistMapper.Local mapper;

    @Nullable
    private GistModel content;

    public GistInteractor(GistLocalDao dao) {
        mDao = dao;
        mapper = new GistMapper.Local();
    }

    Single<GistModel> load(String id) {
        return mDao.byId(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(mapper)
                .doOnSuccess(model -> content = model);
    }

    @Nullable
    String insteadWebLink() {
        if (content != null) {
            return (content.insteadWebLink());
        } else {
            return null;
        }
    }

    boolean isChange(String desc, String note) {
        if (content == null) {
            return false;
        }
        GistModel now = new GistModel(content, desc, note);
        return !content.equals(now);
    }

    Completable transact(@NonNull String desc, @NonNull String note) {
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

    Maybe<String> delete() {
        if (content == null) {
            return Maybe.error(new IllegalStateException());
        }
        return Maybe.fromCallable(() -> {
            int c = mDao.deleteById(content.id);
            if (c != 1)  throw new Exception();
            return content.id;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
