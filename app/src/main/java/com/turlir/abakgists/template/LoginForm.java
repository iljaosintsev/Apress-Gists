package com.turlir.abakgists.template;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.turlir.abakgists.templater.DynamicForm;
import com.turlir.abakgists.templater.Structure;
import com.turlir.abakgists.templater.base.Grouper;
import com.turlir.abakgists.templater.base.Interceptor;
import com.turlir.abakgists.templater.widget.MaterialField;

class LoginForm extends DynamicForm<EditableProfile> {

    private MaterialField mName, mPosition, mPhone, mAdd;

    LoginForm(@NonNull ViewGroup group) {
        super(group);
    }

    @Override
    protected Grouper group() {
        return new Grouper() {
            @Override
            public ViewGroup changeRoot(String tag, ViewGroup origin, ViewGroup current) {
                switch (tag) {
                    case "position":
                        LinearLayout ll = new LinearLayout(context());
                        ll.setOrientation(LinearLayout.VERTICAL);
                        ll.setBackgroundColor(Color.parseColor("#D7CCC8"));
                        origin.addView(ll);
                        return ll;
                    case "phone":
                        return current;

                    default:
                        return origin;
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
