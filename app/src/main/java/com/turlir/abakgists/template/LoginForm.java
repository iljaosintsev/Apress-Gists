package com.turlir.abakgists.template;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.turlir.abakgists.templater.DynamicForm;
import com.turlir.abakgists.templater.Template;
import com.turlir.abakgists.templater.base.Interceptor;
import com.turlir.abakgists.templater.check.NotEmpty;
import com.turlir.abakgists.templater.check.TrueCheck;
import com.turlir.abakgists.templater.widget.MaterialField;

class LoginForm extends DynamicForm<EditableProfile> {

    private MaterialField mName, mPosition, mPhone, mAdd;

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

                    @Override
                    public void add(MaterialField view) {
                        mName = view;
                    }
                })

                .addMaterialField("Должность", new TrueCheck<String>(), "position")
                .in(new Interceptor<MaterialField, String>() {
                    @Override
                    public String bind() {
                        return value().position;
                    }

                    @Override
                    public void add(MaterialField view) {
                        mPosition = view;
                    }
                })

                .addPhone("Контактный телефон", false, "phone")
                .in(new Interceptor<MaterialField, String>() {
                    @Override
                    public String bind() {
                        return value().phone();
                    }

                    @Override
                    public void add(MaterialField view) {
                        mPhone = view;
                    }
                })

                .addMaterialField("Дополнительный телефон, ICQ, Skype", new TrueCheck<String>(), "additional")
                .in(new Interceptor<MaterialField, String>() {
                    @Override
                    public String bind() {
                        return value().additionalContact;
                    }

                    @Override
                    public void add(MaterialField view) {
                        view.setOnEditorActionListener(doneVerifierListener());
                        mAdd = view;
                    }
                })
                .build();
    }

    @Override
    protected void interact() {
        // dynamic widget usage
    }

    @Override
    @NonNull
    public EditableProfile collect() {
        return value().clone(
                mName.content(),
                mPosition.content(),
                mPhone.content(),
                mAdd.content()
        );
    }

}
