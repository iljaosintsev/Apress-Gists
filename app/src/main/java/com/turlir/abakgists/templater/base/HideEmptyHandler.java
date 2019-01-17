package com.turlir.abakgists.templater.base;

import android.view.View;

import com.turlir.abakgists.templater.WidgetHolder;

public class HideEmptyHandler implements EmptyHandler {

    @Override
    public void process(WidgetHolder holder) {
        holder.visibility(View.GONE);
    }
}
