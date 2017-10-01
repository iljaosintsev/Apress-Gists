package com.turlir.abakgists.templater;

import android.view.View;

interface Callback<T extends View> {

    void added(T view);

}
