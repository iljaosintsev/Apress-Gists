package com.turlir.abakgists.templater.base;

import android.content.Context;
import android.view.View;

import com.turlir.abakgists.template.LabeledEditText;
import com.turlir.abakgists.template.VerticalEditText;

import java.util.ArrayList;
import java.util.List;

class BaseBuilder {

    private final List<WidgetHolder> mHolders;
    private final Context mContext;

    BaseBuilder(Context cnt) {
        mContext = cnt;
        mHolders = new ArrayList<>();
    }

    public BaseBuilder addField(String label, String hint, Interceptor<LabeledEditText, String> callback) {
        LabeledEditText widget = new LabeledEditText(getContext());
        widget.setTitle(label);
        widget.setHint(hint);
        add(new NotEmpty(), callback, widget);
        return this;
    }

    public BaseBuilder addVerticalField(String label, String hint, Checker<String> rule, Interceptor<VerticalEditText, String> callback) {
        VerticalEditText widget = new VerticalEditText(getContext());
        widget.setTitle(label);
        widget.setHint(hint);
        add(rule, callback, widget);
        return this;
    }

    public Template build() {
        return new Template(mHolders);
    }

    protected <V extends View & FormWidget<T>, T> void add(Checker<T> rule, Interceptor<V, T> callback, V field) {
        WidgetHolder<V, T> h = new WidgetHolder<>(field, rule, callback);
        mHolders.add(h);
    }

    protected final Context getContext() {
        return mContext;
    }
}
