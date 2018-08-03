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

    public int trueSize() {
        return mLoader.size();
    }

    public void firstLoad() {
        mLoader.firstPage();
    }

    public void nextPage() {
        mLoader.nextPage();
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
