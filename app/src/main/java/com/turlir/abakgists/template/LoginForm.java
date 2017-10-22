package com.turlir.abakgists.template;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.turlir.abakgists.templater.DynamicForm;
import com.turlir.abakgists.templater.Structure;
import com.turlir.abakgists.templater.base.Interceptor;
import com.turlir.abakgists.templater.widget.MaterialField;

class LoginForm extends DynamicForm<EditableProfile> {

    private MaterialField mName, mPosition, mPhone, mAdd;

    LoginForm(@NonNull ViewGroup group) {
        super(group);
    }

    @Override
    protected Structure<EditableProfile> createTemplate() {
        return new LoginBuilder()
                .addMaterialField("Имя", true, "name")
                .in(new Interceptor() {
                    @Override
                    public String bind() {
                        return value().name;
                    }

                    @Override
                    public void add(View view) {
                        mName = (MaterialField) view;
                    }
                })

                .addMaterialField("Должность", false, "position")
                .in(new Interceptor() {
                    @Override
                    public String bind() {
                        return value().position;
                    }

                    @Override
                    public void add(View view) {
                        mPosition = (MaterialField) view;
                    }
                })

                .addPhone("Контактный телефон", false, "phone")
                .in(new Interceptor() {
                    @Override
                    public String bind() {
                        return value().phone();
                    }

                    @Override
                    public void add(View view) {
                        mPhone = (MaterialField) view;
                    }
                })

                .addMaterialField("Дополнительный телефон, ICQ, Skype", false, "additional")
                .in(new Interceptor() {
                    @Override
                    public String bind() {
                        return value().additionalContact;
                    }

                    @Override
                    public void add(View view) {
                        mAdd = (MaterialField) view;
                        mAdd.setOnEditorActionListener(doneVerifierListener());
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
