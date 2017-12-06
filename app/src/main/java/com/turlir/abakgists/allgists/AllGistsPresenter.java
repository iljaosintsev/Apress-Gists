package com.turlir.abakgists.allgists;


import android.support.annotation.NonNull;

import com.turlir.abakgists.allgists.view.AllGistsFragment;
import com.turlir.abakgists.base.BasePresenter;
import com.turlir.abakgists.base.erroring.ErrorInterpreter;
import com.turlir.abakgists.base.erroring.ErrorSituation;
import com.turlir.abakgists.base.erroring.RepeatingError;
import com.turlir.abakgists.model.GistModel;

import java.util.List;

public class AllGistsPresenter extends BasePresenter<AllGistsFragment> {

    private final GistLoader mLoader;

    public AllGistsPresenter(GistListInteractor interactor) {
        GistDownloadHandler handler = new GistDownloadHandler() {
            @Override
            public void onNext(Object o) {
                // TODO why ?
            }
        };
        mLoader = new GistLoader(interactor, new LoaderCallback(), handler);
    }

    /**
     * из локального кеша или сетевого запроса
     */
    public void loadPublicGists(final int currentSize) {
        mLoader.loadNewPage(currentSize);
    }

    public void updateGist() {
        mLoader.refresh();
    }

    public void first() {
        mLoader.resetState();
        loadPublicGists(0);
    }

    public void again() {
        mLoader.resetState();
        loadPublicGists(GistListInteractor.IGNORE_SIZE);
    }

    private abstract class GistDownloadHandler<E> extends ErrorHandler<E> {

        @NonNull
        @Override
        protected ErrorSituation[] additionalSituation() {
            return new ErrorSituation[] { new RepeatingError() };
        }

        @Override
        protected boolean isError() {
            return getView() != null && getView().isError();
        }

        @Override
        protected boolean isDataAvailable() {
            return getView() != null && !getView().isEmpty();
        }

        @Override
        protected ErrorInterpreter interpreter() {
            return getView();
        }

    }

    private class LoaderCallback implements ListCombination.Callback<GistModel> {

        @Override
        public void blockingLoad(boolean visible) {
            if (getView() != null) {
                getView().toBlockingLoad(visible);
            }
        }

        @Override
        public void inlineLoad(boolean visible) {
            if (getView() != null) {
                getView().inlineLoad(visible);
            }
        }

        @Override
        public void renderData(List<GistModel> items) {
            if (getView() != null) {
                getView().onGistLoaded(items);
            }

        }

        @Override
        public void emptyData(boolean visible) {
            if (getView() != null) {
                // not impl
            }
        }
    }
}
