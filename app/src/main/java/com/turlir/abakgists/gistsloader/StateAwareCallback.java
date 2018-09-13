package com.turlir.abakgists.gistsloader;

import android.support.annotation.NonNull;

import com.turlir.abakgists.allgists.combination.ErrorProcessor;
import com.turlir.abakgists.allgists.combination.InlineLoading;
import com.turlir.abakgists.allgists.combination.ListCombination;
import com.turlir.abakgists.allgists.combination.ListManipulator;
import com.turlir.abakgists.allgists.combination.Refresh;
import com.turlir.abakgists.allgists.combination.Start;
import com.turlir.abakgists.base.loader.Window;
import com.turlir.abakgists.base.loader.server.LoadableItem;
import com.turlir.abakgists.base.loader.server.StateServerBehavior;
import com.turlir.abakgists.base.loader.state.StateCallback;
import com.turlir.abakgists.base.loader.state.StateServerLoader;

import java.util.List;

public class StateAwareCallback<T> implements StateCallback<T, LoadablePage> {

    private final ErrorProcessor mProcessor;

    private final StateServerBehavior<T> behavior;

    @NonNull
    private ListCombination<T> mState;

    public StateAwareCallback(ErrorProcessor processor, ListManipulator<T> manipulator, StateServerBehavior<T> beh) {
        mProcessor = processor;
        mState = new Start<>(manipulator);
        behavior = beh;
    }

    @Override
    public void content(List<T> nextItems, Window w, StateServerLoader<T, LoadablePage> parent) {
        final int nowSize = nextItems.size();

        if (behavior.shouldRender(nextItems, w)) {
            if (!canLoad()) { // loading in process
                mState.content(nextItems).perform(); // side effect without state change
                mState.perform(); // repeat loading
                behavior.afterRender(nextItems); // TODO
                return;
            } else {
                changeState(mState.content(nextItems)); // perform
                behavior.afterRender(nextItems); // TODO
            }
            mProcessor.resetError();
        }

        if (behavior.shouldRequest(nextItems, w) && canLoad()) {
            int _size = nowSize == 0 ? 0 : w.addition();
            LoadableItem page = w.constraint(_size);
            parent.loadFromServer(page);
            changeState(mState.doLoad());
        }
    }

    @Override
    public void failure(Throwable t) {
        changeState(mState.error(t, mProcessor));
    }

    @Override
    public void onLoadedFromServer(int c) {
        changeState(mState.doIntermediate());
    }

    @Override
    public void onPreUpdate(Window w) {
        changeState(mState.refresh());
    }

    @Override
    public boolean canLoad() {
        return !(mState instanceof InlineLoading) && !(mState instanceof Refresh);
    }

    @Override
    public StateServerBehavior<T> getHelper() {
        return behavior;
    }

    private void changeState(ListCombination<T> now) {
        mState = now;
        mState.perform();
    }
}
