package com.turlir.abakgists.templater.base;

import android.content.Context;
import android.view.View;

import com.turlir.abakgists.template.LabeledEditText;
import com.turlir.abakgists.template.MaterialField;
import com.turlir.abakgists.template.VerticalEditText;

import java.util.ArrayList;
import java.util.List;

public class Builder {

    private final List<WidgetHolder> mHolders;
    private final Context mContext;

    public Builder(Context cnt) {
        mContext = cnt;
        mHolders = new ArrayList<>();
    }

    public Builder addField(String label, String hint, Interceptor<LabeledEditText, String> callback) {
        LabeledEditText widget = new LabeledEditText(mContext);
        widget.setTitle(label);
        widget.setHint(hint);
        add(new NotEmpty(), callback, widget);
        return this;
    }

    public Builder addVerticalField(String label, String hint,  Checker<String> rule, Interceptor<VerticalEditText, String> callback) {
        VerticalEditText widget = new VerticalEditText(mContext);
        widget.setTitle(label);
        widget.setHint(hint);
        add(rule, callback, widget);
        return this;
    }

    public Builder addMaterialField(String hint, Checker<String> rule, Interceptor<MaterialField, String> callback) {
        MaterialField field = new MaterialField(mContext);
        field.setHint(hint);
        field.setId(mHolders.size());
        add(rule, callback, field);
        return this;
    }

    public Template build() {
        return new Template(mHolders);
    }

    private <V extends View & FormWidget<T>, T> void add(Checker<T> rule, Interceptor<V, T> callback, V field) {
        WidgetHolder<V, T> h = new WidgetHolder<>(field, rule, callback);
        mHolders.add(h);
    }
}
