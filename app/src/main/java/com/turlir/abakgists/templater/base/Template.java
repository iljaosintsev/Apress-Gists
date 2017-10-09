package com.turlir.abakgists.templater.base;

import android.view.ViewGroup;

import java.util.List;

public class Template<T>{

    private final List<WidgetHolder> mHolders;
    private final List<Out<T>> mOuts;

    Template(List<WidgetHolder> holders, List<Out<T>> outs) {
        if (holders.size() != outs.size() || holders.size() < 1) {
            throw new IllegalArgumentException();
        }
        mHolders = holders;
        mOuts = outs;
    }

    void connect(ViewGroup group) {
        for (int i = 0, size = mHolders.size(); i < size; i++) {
            WidgetHolder holder = mHolders.get(i);
            holder.connect(group, size - 1);
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
}
