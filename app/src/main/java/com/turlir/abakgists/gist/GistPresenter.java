package com.turlir.abakgists.gist;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.turlir.abakgists.base.App;
import com.turlir.abakgists.model.GistModel;

import javax.inject.Inject;

import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.observers.ResourceCompletableObserver;
import io.reactivex.observers.ResourceMaybeObserver;
import timber.log.Timber;

@InjectViewState
public class GistPresenter extends MvpPresenter<GistView> {

    @Inject
    GistInteractor interactor;

    @Inject
    GistDeleteBus deleteBus;

    GistPresenter() {
        App.getComponent().inject(this);
    }

    void load(String id) {
        interactor.load(id)
                .subscribe(new DisposableSingleObserver<GistModel>() {
                    @Override
                    public void onSuccess(GistModel model) {
                        dispose();
                        getViewState().onLoadSuccess(model);
                    }
                    @Override
                    public void onError(Throwable e) {
                        dispose();
                        Timber.e(e, "failure load gist by id %s", id);
                        getViewState().onLoadFailure();
                    }
                });
    }

    boolean isChange(@NonNull String desc, @NonNull String note) {
        return interactor.isChange(desc, note);
    }

    void transact(@NonNull String desc, @NonNull String note) {
        interactor.transact(desc, note)
                .subscribe(new ResourceCompletableObserver() {
                    @Override
                    public void onComplete() {
                        dispose();
                        getViewState().updateSuccess();
                    }
                    @Override
                    public void onError(Throwable e) {
                        dispose();
                        Timber.e(e, "update gist in failure");
                    }
                });
    }

    void delete() {
        if (!interactor.possiblyDelete()) {
            return;
        }
        interactor.delete()
                .doOnSuccess(id -> deleteBus.gistDeleted(id))
                .subscribe(new ResourceMaybeObserver<String>() {
                    @Override
                    public void onSuccess(String id) {
                        dispose();
                        Timber.v("gist successfully deleted %s", id);
                        getViewState().deleteSuccess();
                    }
                    @Override
                    public void onError(Throwable e) {
                        dispose();
                        Timber.e(e, "failure delete gist");
                        getViewState().deleteFailure();
                    }
                    @Override
                    public void onComplete() {
                        // stuff
                    }
                });
    }

    Uri insteadWebLink() {
        return Uri.parse(interactor.insteadWebLink());
    }
}
