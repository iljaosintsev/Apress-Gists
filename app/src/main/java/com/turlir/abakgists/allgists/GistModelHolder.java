package com.turlir.abakgists.allgists;


import android.view.View;
import android.widget.TextView;

import com.turlir.abakgists.R;
import com.turlir.abakgists.model.GistModel;

import timber.log.Timber;

class GistModelHolder extends ModelViewHolder<GistModel> {

    GistModelHolder(View itemView) {
        super(itemView);
        Timber.i("new holder created");
    }

    @Override
    void bind(GistModel item) {
        TextView tvId = (TextView) itemView.findViewById(R.id.item_gist_id);
        tvId.setText(String.valueOf(item.id));

        TextView tvCreated = (TextView) itemView.findViewById(R.id.item_gist_created);
        tvCreated.setText(item.created);

        TextView tvDesc = (TextView) itemView.findViewById(R.id.item_gist_desc);
        tvDesc.setText(item.description);
    }

}
