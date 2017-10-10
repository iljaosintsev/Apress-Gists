package com.turlir.abakgists.template;

import android.content.Context;
import android.view.View;

import com.turlir.abakgists.templater.BaseBuilder;
import com.turlir.abakgists.templater.base.Interceptor;
import com.turlir.abakgists.templater.base.Out;
import com.turlir.abakgists.templater.check.Checker;
import com.turlir.abakgists.templater.check.PhoneChecker;
import com.turlir.abakgists.templater.widget.FormWidget;
import com.turlir.abakgists.templater.widget.MaterialField;
import com.turlir.abakgists.templater.widget.PhoneField;

class LoginBuilder extends BaseBuilder<EditableProfile> {

    LoginBuilder(Context cnt) {
        super(cnt);
    }

    LoginBuilder addMaterialField(String hint, Checker<String> rule, String tag) {
        MaterialField field = new MaterialField(getContext());
        field.setHint(hint);
        add(rule, field, tag);
        return this;
    }

    LoginBuilder addPhone(String hint, boolean required, String tag) {
        PhoneField field = new PhoneField(getContext());
        field.setHint(hint);
        add(new PhoneChecker(required), field, tag);
        return this;
    }

    <V extends View & FormWidget<T>, T> LoginBuilder in(Interceptor<V, T> callback) {
        super.interceptor(callback);
        return this;
    }

    LoginBuilder out(Out<EditableProfile> o) {
        super.interceptor(o);
        return this;
    }

}
