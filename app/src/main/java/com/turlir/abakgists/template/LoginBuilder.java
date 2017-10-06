package com.turlir.abakgists.template;

import android.content.Context;
import android.view.View;

import com.turlir.abakgists.templater.base.BaseBuilder;
import com.turlir.abakgists.templater.base.Checker;
import com.turlir.abakgists.templater.base.FormWidget;
import com.turlir.abakgists.templater.base.Interceptor;
import com.turlir.abakgists.templater.base.PhoneChecker;

class LoginBuilder extends BaseBuilder {

    LoginBuilder(Context cnt) {
        super(cnt);
    }

    LoginBuilder addMaterialField(String hint, Checker<String> rule, Interceptor<MaterialField, String> callback) {
        MaterialField field = new MaterialField(getContext());
        field.setHint(hint);
        add(rule, callback, field);
        return this;
    }

    LoginBuilder addMaterialField(String hint, Checker<String> rule) {
        MaterialField field = new MaterialField(getContext());
        field.setHint(hint);
        add(rule, field);
        return this;
    }

    LoginBuilder addPhone(String hint, Interceptor<MaterialField, String> callback) {
        PhoneField field = new PhoneField(getContext());
        field.setHint(hint);
        add(new PhoneChecker(), callback, field);
        return this;
    }

    public <V extends View & FormWidget<T>, T> LoginBuilder context(Interceptor<V, T> callback) {
        super.interceptor(callback);
        return this;
    }

}
