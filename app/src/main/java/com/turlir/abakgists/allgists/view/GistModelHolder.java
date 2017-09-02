package com.turlir.abakgists.allgists.view;


import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.TextView;

import com.turlir.abakgists.R;
import com.turlir.abakgists.model.GistModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

class GistModelHolder extends ModelViewHolder<GistModel> {

    @BindView(R.id.item_gist_id)
    TextView tvId;

    @BindView(R.id.item_gist_created)
    TextView tvCreated;

    @BindView(R.id.item_gist_desc)
    TextView tvDesc;

    @BindView(R.id.item_gist_note)
    TextView tvNote;

    GistModelHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        Timber.v("new holder created");
    }

    @Override
    void bind(GistModel item) {
        if (item.ownerLogin == null) {
            tvId.setText(String.valueOf(item.id));
        } else {
            tvId.setText(item.ownerLogin);
        }
        tvCreated.setText(item.created);
        tvNote.setText(item.note);
        tvDesc.setText(item.description);

        final @DrawableRes int left;
        if (item.isLocal) {
            left = R.drawable.indicator_online;
        } else {
            left = R.drawable.indicator_offline;
        }
        tvCreated.setCompoundDrawablesWithIntrinsicBounds(left, 0, 0, 0);
    }

}
