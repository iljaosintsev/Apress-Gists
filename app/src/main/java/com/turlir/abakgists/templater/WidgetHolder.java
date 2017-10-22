package com.turlir.abakgists.templater;

import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.turlir.abakgists.templater.base.Interceptor;
import com.turlir.abakgists.templater.check.Checker;
import com.turlir.abakgists.templater.widget.FormWidget;

public class WidgetHolder {

    private final FormWidget mWidget;

    @Nullable
    private final Interceptor mCallback;

    private final Checker mChecker;

    private final Node mNode;

    WidgetHolder(FormWidget widget, Checker checker, @Nullable Interceptor callback, Node node) {
        mWidget= widget;
        mChecker = checker;
        mCallback = callback;
        mNode = node;
    }

    public String value() {
        return mWidget.content();
    }

    public void showError(String message) {
        mWidget.showError(message);
    }

    void connect(ViewGroup group, int size) {
        group.addView(mWidget.view());
        final int position = mNode.position;
        if (position == 0) {
            mWidget.position(FormWidget.FIRST);
        } else if (position < size) {
            mWidget.position(FormWidget.MIDDLE);
        } else {
            mWidget.position(FormWidget.LAST);
        }
        if (mCallback != null) {
            mCallback.add(mWidget.view());
        }
    }

    void bind() {
        if (mCallback != null) {
            String add = mCallback.bind();
            mWidget.bind(add);
        }
        mWidget.setName(mNode.name);
        mWidget.setHint(mNode.hint);
        mWidget.setExample(mNode.example);
    }

    boolean verify() {
        String value = value();
        return mChecker.check(value);
    }

    void showError() {
        if (mChecker.error() == null) {
            throw new RuntimeException("showing before verify");
        }
        mWidget.showError(mChecker.error());
    }

    void hideError() {
        mWidget.showError(null);
    }

    String tag() {
        return mNode.tag;
    }

    /*public*/ void enabled(boolean state) {
        mWidget.setEnabled(state);
    }

    public void visibility(int visibility) {
        mWidget.setVisibility(visibility);
    }

}
