package com.turlir.abakgists.templater.widget;

import android.content.Context;

public class WidgetFactory {

    public static final String MATERIAL_F = "material";

    public static final String PHONE_F = "phone";

    private final Context mContext;

    public WidgetFactory(Context context) {
        mContext = context;
    }

    public FormWidget widget(String type) {
        switch (type) {
            case PHONE_F:
                return new PhoneField(mContext);
            default:
                return new MaterialField(mContext);
        }
    }

}
