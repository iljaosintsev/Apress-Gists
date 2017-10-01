package com.turlir.abakgists.templater;

import android.content.Context;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class Template {

    private final List<WidgetHolder> mHolders;

    private Template(List<WidgetHolder> holders) {
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


    public static class Builder {

        private final List<WidgetHolder> mHolders;
        private final Context mContext;

        public Builder(Context cnt) {
            mContext = cnt;
            mHolders = new ArrayList<>();
        }

        Builder addField(String label, String content, Callback<LabeledEditText> callback) {
            LabeledEditText widget = new LabeledEditText(mContext);
            widget.setTitle(label);
            WidgetHolder<LabeledEditText, String> h = new WidgetHolder<>(
                    widget,
                    content,
                    new NotEmpty(),
                    callback
            );
            mHolders.add(h);
            return this;
        }

        Builder addVerticalField(String label, String content, Callback<VerticalEditText> callback) {
            VerticalEditText widget = new VerticalEditText(mContext);
            widget.setTitle(label);
            WidgetHolder<VerticalEditText, String> h = new WidgetHolder<>(
                    widget,
                    content,
                    new MinLimit(4),
                    callback
            );
            mHolders.add(h);
            return this;
        }

        Template build() {
            return new Template(mHolders);
        }
    }

    private static class NotEmpty implements Checker<String> {

        @Override
        public boolean check(String actual) {
            return actual != null && actual.trim().length() > 0;
        }

        @Override
        public String error() {
            return "Can not be empty";
        }
    }

    private static class MinLimit implements Checker<String> {

        private final int minimum;

        MinLimit(int minimum) {
            this.minimum = minimum;
        }

        @Override
        public boolean check(String actual) {
            return actual != null && actual.trim().length() > minimum;
        }

        @Override
        public String error() {
            return "Minimum " + (minimum + 1) + " symbol(s)";
        }
    }

}
