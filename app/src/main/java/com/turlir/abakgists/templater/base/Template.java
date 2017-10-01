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

    static class NotEmpty implements Checker<String> {

        @Override
        public boolean check(String actual) {
            return actual != null && actual.trim().length() > 0;
        }

        @Override
        public String error() {
            return "Can not be empty";
        }
    }

    static class MinLimit implements Checker<String> {

        private final int minimum;

        MinLimit(int minimum) {
            this.minimum = minimum - 1;
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
