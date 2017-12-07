package com.turlir.abakgists.allgists;


import com.turlir.abakgists.allgists.view.AllGistsFragment;
import com.turlir.abakgists.base.BasePresenter;
import com.turlir.abakgists.base.erroring.ErrorInterpreter;
import com.turlir.abakgists.base.erroring.ErrorSelector;
import com.turlir.abakgists.base.erroring.RepeatingError;
import com.turlir.abakgists.base.erroring.TroubleSelector;
import com.turlir.abakgists.model.GistModel;

import java.util.List;

import timber.log.Timber;

public class AllGistsPresenter extends BasePresenter<AllGistsFragment> {

    private final GistLoader mLoader;

    public AllGistsPresenter(GistListInteractor interactor) {
        ErrorSelector selector = new TroubleSelector(new RepeatingError());
        mLoader = new GistLoader(interactor, new LoaderCallback(), selector, new ErrorCallback());
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

    private class LoaderCallback implements ListCombination.Callback<GistModel> {

        private static final String TAG = "DataCycle";

        @Override
        public void blockingLoad(boolean visible) {
            if (getView() != null) {
                Timber.v(TAG, "blockingLoad %s", visible);
                getView().toBlockingLoad(visible);
            }
        }

        @Override
        public void inlineLoad(boolean visible) {
            if (getView() != null) {
                Timber.v(TAG, "inlineLoad %s", visible);
                getView().inlineLoad(visible);
            }
        }

        @Override
        public void renderData(List<GistModel> items) {
            if (getView() != null) {
                Timber.v(TAG, "renderData %s", items.size());
                getView().onGistLoaded(items);
            }

        }

        @Override
        public void emptyData(boolean visible) {
            // not impl
       }
    }

    private class ErrorCallback implements ListCombination.ErrorProcessing {

        @Override
        public ErrorInterpreter error() {
            return getView();
        }

        @Override
        public boolean dataAvailable() {
            return getView() != null && !getView().isEmpty();
        }

        @Override
        public boolean isError() {
            return getView() != null && getView().isError();
        }

    }
}
