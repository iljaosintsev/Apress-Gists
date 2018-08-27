package com.turlir.abakgists.allgists;


import android.content.res.Resources;

import com.turlir.abakgists.allgists.combination.ErrorProcessor;
import com.turlir.abakgists.allgists.combination.ListManipulator;
import com.turlir.abakgists.allgists.loader.Range;
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
    private final ErrorSelector mSelector;

    public AllGistsPresenter(DataSourceFactory factory) {
        mSelector = new TroubleSelector(new RepeatingError());
        Range policy = createStartPoint();
        GistListInteractor dataSource = factory.create(policy);
        mLoader = factory.create(dataSource, new LoaderCallback());
    }

    @Override
    public void detach() {
        super.detach();
        mLoader.stop();
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

    public void prevPage() {
        mLoader.prevPage();
    }

    public void updateGist() {
        mLoader.updateGist(createStartPoint());
    }

    private static Range createStartPoint() {
        return new Range(0, 30, 15);
        //return new Range(0, 30, 30);
    }

    private class LoaderCallback implements ListManipulator<GistModel> {

        private ErrorProcessor mProcessor;

        @Override
        public void blockingLoad(boolean visible) {
            if (getView() != null) {
                Timber.v("blockingLoad %s", visible);
                getView().toBlockingLoad(visible);
            }
        }

        @Override
        public void inlineLoad(boolean visible) {
            if (getView() != null) {
                Timber.v("inlineLoad %s", visible);
                getView().inlineLoad(visible);
            }
        }

        @Override
        public void renderData(List<GistModel> items) {
            if (getView() != null) {
                Timber.v("renderData %s", items.size());
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

        @Override
        public ErrorProcessor getErrorProcessor() {
            if (mProcessor == null) {
                mProcessor = new ErrorCallback();
            }
            return mProcessor;
        }
    }

    private class ErrorCallback implements ErrorProcessor {

        @Override
        public ErrorSelector getErrorSelector() {
            return mSelector;
        }

        @Override
        public ErrorInterpreter interpreter() {
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

        @Override
        public Resources getResources() {
            if (getView() != null) {
                return getView().getResources();
            }
            return null;
        }

    }
}
