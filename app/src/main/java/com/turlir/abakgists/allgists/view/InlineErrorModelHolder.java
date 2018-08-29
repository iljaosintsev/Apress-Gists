package com.turlir.abakgists.allgists.view;

import android.view.View;

import com.turlir.abakgists.R;
import com.turlir.abakgists.model.InlineErrorModel;

import butterknife.ButterKnife;
import butterknife.OnClick;

class InlineErrorModelHolder extends ModelViewHolder<InlineErrorModel> {

    private View.OnClickListener mClicker;

    InlineErrorModelHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @OnClick(R.id.btn_try_now_inline_error)
    void tryNowClicked(View v) {
        if (mClicker != null) {
            mClicker.onClick(v);
        }
    }

    @Override
    void bind(InlineErrorModel model) {
        mClicker = model.clicker;
    }
}
