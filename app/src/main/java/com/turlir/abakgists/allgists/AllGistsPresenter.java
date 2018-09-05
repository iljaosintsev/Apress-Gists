package com.turlir.abakgists.allgists;


import android.content.Context;
import android.content.res.Resources;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.turlir.abakgists.allgists.combination.ErrorProcessor;
import com.turlir.abakgists.allgists.combination.ListManipulator;
import com.turlir.abakgists.allgists.view.GistListView;
import com.turlir.abakgists.base.App;
import com.turlir.abakgists.base.erroring.ErrorInterpreter;
import com.turlir.abakgists.base.erroring.ErrorSelector;
import com.turlir.abakgists.base.erroring.RepeatingError;
import com.turlir.abakgists.base.erroring.TroubleSelector;
import com.turlir.abakgists.model.ErrorModel;
import com.turlir.abakgists.model.GistModel;
import com.turlir.abakgists.model.LoadingModel;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

@InjectViewState
public class AllGistsPresenter extends MvpPresenter<GistListView> {

    @Inject
    GistListInteractor interactor;

    @Inject
    Context context;

    private final GistLoader mLoader;
    private final LoaderCallback mViewInteract;

    public AllGistsPresenter() {
        App.getComponent().inject(this);
        mViewInteract = new LoaderCallback();
        mLoader = new GistLoader(interactor, mViewInteract, new ErrorCallback());
    }

    @Override
    public void detachView(GistListView view) {
        super.detachView(view);
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

    private boolean isViewAttached() {
        return !getAttachedViews().isEmpty();
    }

    private class LoaderCallback implements ListManipulator<GistModel> {

        private int mItems;
        private boolean isBlocked;

        @Override
        public void blockingLoad(boolean visible) {
            Timber.v("blockingLoad %s", visible);
            isBlocked = visible;
            getViewState().toBlockingLoad(visible);
        }

        @Override
        public void inlineLoad(boolean visible) {
            Timber.v("inlineLoad %s", visible);
            if (visible) {
                getViewState().inlineLoad(new LoadingModel(trueSize()));
            } else {
                getViewState().removeInlineLoad();
            }
        }

        @Override
        public void renderData(List<GistModel> items) {
            Timber.v("renderData %s", items.size());
            mItems = items.size();
            boolean shouldReset = mLoader.isDifferent(items.get(items.size() - 1));
            boolean forward = shouldReset && mLoader.canNext();
            boolean backward = shouldReset && mLoader.canPrevious();
            getViewState().onGistLoaded(items, forward, backward);
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
            if (isViewAttached()) {
                return this;
            }
            return null;
        }

        @Override
        public boolean dataAvailable() {
            return mViewInteract.dataAvailable();
        }

        @Override
        public boolean isError() {
            return hasError;
        }

        @Override
        public void resetError() {
            hasError = false;
        }

        @Override
        public Resources getResources() {
            return context.getResources();
        }

        // ErrorInterpreter

        @Override
        public void nonBlockingError(String msg) {
            hasError = true;
            getViewState().nonBlockingError(msg);
        }

        @Override
        public void alertError(String msg) {
            hasError = true;
            getViewState().alertError(msg);
        }

        @Override
        public void blockingError(String msg) {
            hasError = true;
            getViewState().blockingError(new ErrorModel(msg));
        }
    }
}
