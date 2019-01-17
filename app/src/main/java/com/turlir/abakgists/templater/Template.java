package com.turlir.abakgists.templater;

import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.turlir.abakgists.templater.base.Grouper;
import com.turlir.abakgists.templater.base.Out;

import java.util.Iterator;
import java.util.List;

class Template<T> {

    private final List<WidgetHolder> mHolders;
    private final List<Out<T>> mOuts;

    private final Mixer mixer;

    Template(List<WidgetHolder> holders, List<Out<T>> outs, Mixer mixer) {
        this.mixer = mixer;
        if (holders.size() != outs.size() || holders.size() < 1) {
            throw new IllegalArgumentException();
        }
        mHolders = holders;
        mOuts = outs;
    }

    void connect(final ViewGroup group, Grouper hack) {
        Iterator<ViewGroup> iter = mixer.iterator(group, hack);
        for (int i = 0, l = mHolders.size(), s = l - 1; i < l; i++) {
            WidgetHolder holder = mHolders.get(i);
            ViewGroup root = iter.next();
            holder.connect(root, s);
        }
    }

    void bind() {
        for (WidgetHolder holder : mHolders) {
            holder.bind();
        }
    }

    boolean verify() {
        for (WidgetHolder holder : mHolders) {
            if (!holder.verify()) {
                holder.showError();
                return false;
            } else {
                holder.hideError();
            }
        }
        return true;
    }

    boolean verifyAll() {
        boolean error = false;
        for (WidgetHolder holder : mHolders) {
            if (!holder.verify()) {
                holder.showError();
                error = true;
            } else {
                holder.hideError();
            }
        }
        return !error;
    }

    void collect(T value) {
        for (int i = 0; i < mOuts.size(); i++) {
            Out<T> o = mOuts.get(i);
            if (o != null) {
                WidgetHolder h = mHolders.get(i);
                String tmp = h.value();
                o.call(tmp, value);
            }
        }
    }

    void enabledAll(boolean state) {
        for (WidgetHolder holder : mHolders) {
            holder.enabled(state);
        }
    }

    @Nullable
    WidgetHolder findHolder(String tag) {
        for (WidgetHolder holder : mHolders) {
            if (holder.toString() != null && tag.equals(holder.tag())) {
                return holder;
            }
        }
        return null; // also warning logging
    }

}
