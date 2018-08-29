package com.turlir.abakgists.allgists.view.listing;


import android.view.View;
import android.widget.TextView;

import com.turlir.abakgists.model.ErrorModel;

import butterknife.BindView;
import butterknife.ButterKnife;

class ErrorModelHolder extends ModelViewHolder<ErrorModel> {

    @BindView(android.R.id.text1)
    TextView errorTv;

    ErrorModelHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    @Override
    void bind(ErrorModel model) {
        errorTv.setText(model.description);
    }
}
