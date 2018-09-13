package com.turlir.abakgists.allgists;


import android.content.Context;
import android.content.res.Resources;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.turlir.abakgists.allgists.combination.ErrorProcessor;
import com.turlir.abakgists.allgists.combination.ListManipulator;
import com.turlir.abakgists.gistsloader.Accumulator;
import com.turlir.abakgists.gistsloader.DoLoadBehavior;
import com.turlir.abakgists.gistsloader.LoadablePage;
import com.turlir.abakgists.gistsloader.Range;
import com.turlir.abakgists.gistsloader.SourceListing;
import com.turlir.abakgists.gistsloader.StateAwareCallback;
import com.turlir.abakgists.base.loader.SimpleLoader;
import com.turlir.abakgists.base.loader.callback.CallbackLoader;
import com.turlir.abakgists.base.loader.server.ServerLoader;
import com.turlir.abakgists.base.loader.state.StateServerLoader;
import com.turlir.abakgists.allgists.view.GistListView;
import com.turlir.abakgists.base.App;
import com.turlir.abakgists.base.erroring.ErrorInterpreter;
import com.turlir.abakgists.base.erroring.ErrorSelector;
import com.turlir.abakgists.base.erroring.RepeatingError;
import com.turlir.abakgists.base.erroring.TroubleSelector;
import com.turlir.abakgists.gist.GistDeleteBus;
import com.turlir.abakgists.model.ErrorModel;
import com.turlir.abakgists.model.GistModel;
import com.turlir.abakgists.model.LoadingModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import timber.log.Timber;

@InjectViewState
public class AllGistsPresenter extends MvpPresenter<GistListView> {

    @Inject
    SourceListing repo;

    @Inject
    GistDeleteBus deleteBus;

    @Inject
    Context context;

    private final StateServerLoader<GistModel, LoadablePage> mLoader;
    private final LoaderCallback mViewInteract;

    private final Disposable delete;

    private final Accumulator mAccumulator;

    public AllGistsPresenter() {
        App.getComponent().inject(this);

        mLoader = new StateServerLoader<>(
                new ServerLoader<>(
                        new CallbackLoader<>(
                                new SimpleLoader<>(
                                        repo,
                                        createStartPoint()
                                )
                        ),
                        repo
                )
        );

        mViewInteract = new LoaderCallback();
        StateAwareCallback<GistModel> callback = new StateAwareCallback<>(
                new ErrorCallback(),
                mViewInteract,
                new DoLoadBehavior<>()
        );
        mLoader.setCallback(callback);

        mAccumulator = new Accumulator();
        delete = deleteBus.subscribe(s -> getViewState().onGistDeleted());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLoader.stop();
        delete.dispose();
    }

    public void firstLoad() {
        if (!mLoader.hasLoad()) {
            return;
        }
        mLoader.currentPage();
    }

    public void nextPage() {
        if (!canNext()) {
            return;
        }
        mLoader.nextPage();
    }

    public void prevPage() {
        if (!canPrevious()) {
            return;
        }
        mLoader.prevPage();
    }

    public void updateGist() {
        mLoader.update(createStartPoint());
    }

    public boolean canUpdate() {
        return mLoader.hasLoad();
    }

    private boolean canNext() {
        return mLoader.hasNext();
    }

    private boolean canPrevious() {
        return mLoader.hasPrevious();
    }

    private int trueSize() {
        return mAccumulator.accumulator();
    }

    private static Range createStartPoint() {
        return new Range(0, 30, 15);
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
            GistModel last = items.get(items.size() - 1);
            boolean forward = mLoader.resetForward(last);
            boolean backward = mLoader.resetBackward(last);

            getViewState().onGistLoaded(items, forward, backward);
            mAccumulator.now(mLoader.getWindow(), items.size());
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
