package com.turlir.abakgists.template;

import android.view.ViewGroup;

import com.turlir.abakgists.templater.DynamicForm;
import com.turlir.abakgists.templater.Template;
import com.turlir.abakgists.templater.base.Interceptor;
import com.turlir.abakgists.templater.base.Out;
import com.turlir.abakgists.templater.check.NotEmpty;
import com.turlir.abakgists.templater.check.TrueCheck;
import com.turlir.abakgists.templater.widget.MaterialField;

class LoginForm extends DynamicForm<EditableProfile> {

    LoginForm(ViewGroup group) {
        super(group);
    }

    @Override
    protected Template<EditableProfile> createTemplate() {
        return new LoginBuilder(getContext())
                .addMaterialField("Имя", new NotEmpty(), "name")
                .in(new Interceptor<MaterialField, String>() {
                    @Override
                    public String bind() {
                        return value().name;
                    }
                })
                .out(new Out<EditableProfile>() {
                    @Override
                    public void call(String value, EditableProfile object) {
                        int l = value.length();
                    }
                })

                .addMaterialField("Должность", new TrueCheck<String>(), "position")
                .in(new Interceptor<MaterialField, String>() {
                    @Override
                    public String bind() {
                        return value().position;
                    }
                })

                .addPhone("Контактный телефон", false, "phone")
                .in(new Interceptor<MaterialField, String>() {
                    @Override
                    public String bind() {
                        return value().phone();
                    }
                })
                .out(new Out<EditableProfile>() {
                    @Override
                    public void call(String value, EditableProfile object) {
                        int l = value.length();
                    }
                })

                .addMaterialField("Дополнительный телефон, ICQ, Skype", new TrueCheck<String>(), "additional")
                .in(new Interceptor<MaterialField, String>() {
                    @Override
                    public String bind() {
                        return value().additionalContact;
                    }
                })
                .out(new Out<EditableProfile>() {
                    @Override
                    public void call(String value, EditableProfile object) {
                        int l = value.length();
                    }
                })
                .build();
    }

    @Override
    protected void interact() {
        // dynamic widget usage
    }

    /*@Override
    @NonNull
    public EditableProfile collect() {
        return value();
    }*/

}
