package com.turlir.abakgists.template;

import android.content.Context;

import com.turlir.abakgists.templater.BaseBuilder;
import com.turlir.abakgists.templater.check.Checker;
import com.turlir.abakgists.templater.check.PhoneChecker;
import com.turlir.abakgists.templater.widget.MaterialField;
import com.turlir.abakgists.templater.widget.PhoneField;

class LoginBuilder extends BaseBuilder<EditableProfile, LoginBuilder> {

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

    @Override
    protected LoginBuilder getThis() {
        return this;
    }

}
