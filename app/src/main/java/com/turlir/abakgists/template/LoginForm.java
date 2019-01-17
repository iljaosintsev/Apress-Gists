package com.turlir.abakgists.template;

import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import example.turlir.com.templater.DynamicForm;
import example.turlir.com.templater.Structure;
import example.turlir.com.templater.base.Grouper;
import example.turlir.com.templater.base.Interceptor;
import example.turlir.com.templater.widget.FormWidget;
import example.turlir.com.templater.widget.MaterialField;

class LoginForm extends DynamicForm<EditableProfile> {

    private MaterialField mName, mPosition, mPhone, mAdd;

    LoginForm(@NonNull ViewGroup group) {
        super(group);
    }

    @Override
    protected Grouper group() {
        return new Grouper() {
            @Override
            public ViewGroup changeRoot(int index) {
                switch (index) {
                    case 0:
                        LinearLayout ll = new LinearLayout(context());
                        ll.setOrientation(LinearLayout.VERTICAL);
                        ll.setBackgroundColor(Color.parseColor("#D7CCC8"));
                        return ll;

                    default:
                        return null;
                }

            }
        };
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
                    public void add(FormWidget view) {
                        mName = (MaterialField) view.view();
                    }
                })

                .startGroup()
                .addMaterialField("Должность", false, "position")
                .in(new Interceptor() {
                    @Override
                    public String bind() {
                        return value().position;
                    }

                    @Override
                    public void add(FormWidget view) {
                        mPosition = (MaterialField) view.view();
                    }
                })

                .addPhone("Контактный телефон", false, "phone")
                .in(new Interceptor() {
                    @Override
                    public String bind() {
                        return value().phone();
                    }

                    @Override
                    public void add(FormWidget view) {
                        mPhone = (MaterialField) view.view();
                    }
                })
                .endGroup()

                .addMaterialField("Дополнительный телефон, ICQ, Skype", false, "additional")
                .in(new Interceptor() {
                    @Override
                    public String bind() {
                        return value().additionalContact;
                    }

                    @Override
                    public void add(FormWidget view) {
                        mAdd = (MaterialField) view.view();
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
