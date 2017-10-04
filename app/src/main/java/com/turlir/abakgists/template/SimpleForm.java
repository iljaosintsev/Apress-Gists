package com.turlir.abakgists.template;

import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.turlir.abakgists.templater.base.Builder;
import com.turlir.abakgists.templater.base.DynamicForm;
import com.turlir.abakgists.templater.base.Interceptor;
import com.turlir.abakgists.templater.base.MinLimit;
import com.turlir.abakgists.templater.base.NotEmpty;
import com.turlir.abakgists.templater.base.Template;
import com.turlir.abakgists.templater.base.TrueCheck;

class SimpleForm extends DynamicForm<EditableProfile> {

    SimpleForm(ViewGroup group) {
        super(group);
    }

    @Override
    protected Template createTemplate() {
        return new Builder(getContext())
                .addMaterialField("Имя", new NotEmpty(), new Interceptor<MaterialField, String>() {
                    @Override
                    public String bind() {
                        return value().name;
                    }
                })
                .addMaterialField("Должность", new NotEmpty(), new Interceptor<MaterialField, String>() {
                    @Override
                    public String bind() {
                        return value().position;
                    }
                })
                .addMaterialField("Контактный телефон", new MinLimit(16), new Interceptor<MaterialField, String>() {
                    @Override
                    public String bind() {
                        return value().contacts;
                    }
                })
                .addMaterialField("Дополнительный телефон, ICQ, Skype", new TrueCheck<String>(), new Interceptor<MaterialField, String>() {
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
        return value();
    }

}
