package com.turlir.abakgists.templater.base;

import android.view.ViewGroup;

import java.util.List;

public class Template {

    private final List<WidgetHolder> mHolders;

    Template(List<WidgetHolder> holders) {
        mHolders = holders;
    }

    void connect(ViewGroup group) {
        for (WidgetHolder holder : mHolders) {
            holder.connect(group);
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

}
