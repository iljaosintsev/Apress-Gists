package com.turlir.abakgists.templater;

import android.content.Context;
import android.view.ViewGroup;

abstract class BaseForm {

    private final Context mContext;

    private Template mTemplate;

    BaseForm(Context mContext) {
        this.mContext = mContext;

        mTemplate = template();
    }

    protected abstract Template template();

    protected abstract void interact();

    protected abstract void verified();

    /*public final void create() {
        mTemplate = template();
    }*/

    public final void connect(ViewGroup group) {
        mTemplate.connect(group);
    }

    public final void bind() {
        mTemplate.bind();
        interact();
    }

    public final void verify() {
        if (mTemplate.verify()) {
            verified();
        }
    }

    public final void verifyAll() {
        if (mTemplate.verifyAll()) {
            verified();
        }
    }

    protected final Context getContext() {
        return mContext;
    }
}
