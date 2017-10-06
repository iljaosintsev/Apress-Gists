package com.turlir.abakgists.templater.base;

import android.content.Context;

import com.turlir.abakgists.template.MaterialField;
import com.turlir.abakgists.template.PhoneField;

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

    public LoginBuilder addPhone(String hint, Interceptor<MaterialField, String> callback) {
        PhoneField field = new PhoneField(getContext());
        field.setHint(hint);
        add(new PhoneChecker(), callback, field);
        return this;
    }

}
