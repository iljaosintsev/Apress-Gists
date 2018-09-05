package com.turlir.abakgists.allgists;


import android.content.res.Resources;

import com.turlir.abakgists.allgists.combination.ErrorProcessor;
import com.turlir.abakgists.allgists.combination.ListManipulator;
import com.turlir.abakgists.allgists.view.GistListView;
import com.turlir.abakgists.base.BasePresenter;
import com.turlir.abakgists.base.erroring.ErrorInterpreter;
import com.turlir.abakgists.base.erroring.ErrorSelector;
import com.turlir.abakgists.base.erroring.RepeatingError;
import com.turlir.abakgists.base.erroring.TroubleSelector;
import com.turlir.abakgists.model.ErrorModel;
import com.turlir.abakgists.model.GistModel;
import com.turlir.abakgists.model.LoadingModel;

import java.util.List;

import timber.log.Timber;

public class AllGistsPresenter extends BasePresenter<GistListView> {

    private final GistLoader mLoader;
    private final LoaderCallback mViewInteract;

    public AllGistsPresenter(GistListInteractor interactor) {
        mViewInteract = new LoaderCallback();
        mLoader = new GistLoader(interactor, mViewInteract, new ErrorCallback());
    }

    @Override
    public void detach() {
        super.detach();
        mLoader.stop();
    }

    public void firstLoad() {
        mLoader.firstPage();
    }

    public void nextPage() {
        mLoader.nextPage();
    }

    public void prevPage() {
        mLoader.prevPage();
    }

    public void updateGist() {
        mLoader.updateGist();
    }

    private int trueSize() {
        return mLoader.size();
    }

    private class LoaderCallback implements ListManipulator<GistModel> {

        private int mItems;
        private boolean isBlocked;

        @Override
        public void blockingLoad(boolean visible) {
            if (getView() != null) {
                Timber.v("blockingLoad %s", visible);
                isBlocked = visible;
                getView().toBlockingLoad(visible);
            }
        }

        @Override
        public void inlineLoad(boolean visible) {
            if (getView() != null) {
                Timber.v("inlineLoad %s", visible);
                if (visible) {
                    getView().inlineLoad(new LoadingModel(trueSize()));
                } else {
                    getView().removeInlineLoad();
                }
            }
        }

        @Override
        public void renderData(List<GistModel> items) {
            if (getView() != null) {
                Timber.v("renderData %s", items.size());
                mItems = items.size();
                boolean shouldReset = mLoader.isDifferent(items.get(items.size() - 1));
                boolean forward = shouldReset && mLoader.canNext();
                boolean backward = shouldReset && mLoader.canPrevious();
                getView().onGistLoaded(items, forward, backward);
            }
        }

        @Override
        public void emptyData(boolean visible) {
            // not impl
        }

        private boolean dataAvailable() {
            return mItems > 0 && !isBlocked;
        }
    }

    private class ErrorCallback implements ErrorProcessor, ErrorInterpreter {

        private final ErrorSelector mSelector;
        private boolean hasError;

        ErrorCallback() {
            mSelector = new TroubleSelector(new RepeatingError());
        }

        @Override
        public ErrorSelector getErrorSelector() {
            return mSelector;
        }

        @Override
        public ErrorInterpreter interpreter() {
            if (getView() != null) {
                return this;
            }
            return null;
        }

        @Override
        public boolean dataAvailable() {
            return getView() != null && mViewInteract.dataAvailable();
        }

        @Override
        public boolean isError() {
            return getView() != null && hasError;
        }

        @Override
        public void resetError() {
            hasError = false;
        }

        @Override
        public Resources getResources() {
            if (getView() != null) {
                return getView().getContext().getResources();
            }
            return null;
        }

        // ErrorInterpreter

        @Override
        public void nonBlockingError(String msg) {
            if (getView() != null) {
                getView().nonBlockingError(msg);
            }
        }

        @Override
        public void alertError(String msg) {
            if (getView() != null) {
                hasError = true;
                getView().alertError(msg);
            }
        }

        @Override
        public void blockingError(String msg) {
            if (getView() != null) {
                hasError = true;
                getView().blockingError(new ErrorModel(msg));
            }
        }
    }
}
