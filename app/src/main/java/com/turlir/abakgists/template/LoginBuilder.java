package com.turlir.abakgists.template;

import com.turlir.abakgists.templater.BaseBuilder;
import com.turlir.abakgists.templater.check.PhoneChecker;
import com.turlir.abakgists.templater.widget.WidgetFactory;

class LoginBuilder extends BaseBuilder<EditableProfile, LoginBuilder> {

    LoginBuilder() {
        super();
    }

    LoginBuilder addMaterialField(String hint, boolean required, String tag) {
        add(WidgetFactory.MATERIAL_F, hint, tag, required, null);
        return this;
    }

    LoginBuilder addPhone(String hint, boolean required, String tag) {
        add(WidgetFactory.PHONE_F, hint, tag, new PhoneChecker(required), null);
        return this;
    }

    @Override
    protected LoginBuilder getThis() {
        return this;
    }

}
