package com.turlir.abakgists.allgists.view;


import android.view.View;
import android.widget.TextView;

import com.turlir.abakgists.R;
import com.turlir.abakgists.model.LoadingModel;

import butterknife.BindView;
import butterknife.ButterKnife;

class LoadingModelHolder extends ModelViewHolder<LoadingModel> {

    @BindView(R.id.inline_loading_tv)
    TextView tvLoading;

    LoadingModelHolder(View view) {
        super(view);
        ButterKnife.bind(this, itemView);
    }

    @Override
    void bind(LoadingModel model) {
        String text = itemView.getContext().getString(R.string.inline_download, model.count);
        tvLoading.setText(text);
    }
}
