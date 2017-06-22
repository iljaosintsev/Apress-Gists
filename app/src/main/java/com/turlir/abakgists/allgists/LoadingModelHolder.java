package com.turlir.abakgists.allgists;


import android.view.View;
import android.widget.TextView;

import com.turlir.abakgists.R;
import com.turlir.abakgists.model.LoadingModel;

class LoadingModelHolder extends ModelViewHolder<LoadingModel> {

    private final TextView tv;

    public LoadingModelHolder(View view) {
        super(view);
        tv = (TextView) itemView.findViewById(R.id.inline_loading_tv);
    }

    @Override
    void bind(LoadingModel model) {
        String text = itemView.getContext().getString(R.string.inline_download, model.count);
        tv.setText(text);
    }
}
