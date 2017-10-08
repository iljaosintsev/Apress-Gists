package com.turlir.abakgists.template;

import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.turlir.abakgists.templater.base.DynamicForm;
import com.turlir.abakgists.templater.base.Interceptor;
import com.turlir.abakgists.templater.base.NotEmpty;
import com.turlir.abakgists.templater.base.Out;
import com.turlir.abakgists.templater.base.Template;
import com.turlir.abakgists.templater.base.TrueCheck;

class LoginForm extends DynamicForm<EditableProfile> {

    LoginForm(ViewGroup group) {
        super(group);
    }

    @Override
    protected Template<EditableProfile> createTemplate() {
        return new LoginBuilder(getContext())
                .addMaterialField("Имя", new NotEmpty())
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

                .addMaterialField("Должность", new TrueCheck<String>())
                .in(new Interceptor<MaterialField, String>() {
                    @Override
                    public String bind() {
                        return value().position;
                    }
                })

                .addPhone("Контактный телефон", false)
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

                .addMaterialField("Дополнительный телефон, ICQ, Skype", new TrueCheck<String>())
                .in(new Interceptor<MaterialField, String>() {
                        @Override
                        public String bind() {
                            return value().additionalContact;
                        }

                        @Override
                        public void add(MaterialField view) {
                            EditText et = view.getEditText();
                            if (et != null) {
                                et.setImeOptions(EditorInfo.IME_ACTION_DONE);
                            }
                        }
                    }
                )
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
