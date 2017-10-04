package com.turlir.abakgists.templater.base;

import android.content.Context;

import com.turlir.abakgists.template.MaterialField;

public class LoginBuilder extends BaseBuilder {

    public LoginBuilder(Context cnt) {
        super(cnt);
    }

    public LoginBuilder addMaterialField(String hint, Checker<String> rule, Interceptor<MaterialField, String> callback) {
        MaterialField field = new MaterialField(getContext());
        field.setHint(hint);
        add(rule, callback, field);
        return this;
    }

}
