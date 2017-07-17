package com.turlir.abakgists.allgists;


import android.support.v4.content.ContextCompat;
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

    @BindView(R.id.item_gist_source_indicator)
    View indicator;

    GistModelHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        Timber.i("new holder created");
    }

    @Override
    void bind(GistModel item) {
        if (!item.doesOwnerLogin(item)) {
            tvId.setText(String.valueOf(item.id));
        } else {
            tvId.setText(item.ownerLogin);
        }
        tvCreated.setText(item.created);
        tvNote.setText(item.note);
        tvDesc.setText(item.description);

        final int color;
        if (item.isLocal) {
            color = ContextCompat.getColor(itemView.getContext(), android.R.color.holo_green_light);
        } else {
            color = ContextCompat.getColor(itemView.getContext(), android.R.color.holo_red_dark);
        }
        indicator.setBackgroundColor(color);
    }

}
