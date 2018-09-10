package com.turlir.abakgists.gist;

import android.net.Uri;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.turlir.abakgists.base.App;
import com.turlir.abakgists.model.GistModel;

import javax.inject.Inject;

import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.observers.ResourceCompletableObserver;
import timber.log.Timber;

@InjectViewState
public class GistPresenter extends MvpPresenter<GistView> {

    @Inject
    GistInteractor interactor;

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
                        Timber.d(e, "failure load gist by id %s", id);
                        getViewState().onLoadFailure();
                    }
                });
    }

    boolean isChange(String desc, String note) {
        return interactor.isChange(desc, note);
    }

    void transact(String desc, String note) {
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
                        Timber.d(e, "update gist in failure");
                    }
                });
    }

    void delete() {
        if (!interactor.possiblyDelete()) {
            return;
        }
        interactor.delete()
                .subscribe(new ResourceCompletableObserver() {
                    @Override
                    public void onComplete() {
                        dispose();
                        Timber.v("gist successfully deleted");
                        getViewState().deleteSuccess();
                    }
                    @Override
                    public void onError(Throwable e) {
                        dispose();
                        Timber.d(e, "failure delete gist");
                        getViewState().deleteFailure();
                    }
                });
    }

    Uri insteadWebLink() {
        return interactor.insteadWebLink();
    }
}
