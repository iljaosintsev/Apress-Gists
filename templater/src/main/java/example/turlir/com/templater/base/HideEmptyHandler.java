package example.turlir.com.templater.base;

import android.view.View;

import example.turlir.com.templater.WidgetHolder;

public class HideEmptyHandler implements EmptyHandler {

    @Override
    public void process(WidgetHolder holder) {
        holder.visibility(View.GONE);
    }
}
