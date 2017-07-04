package com.turlir.abakgists.allgists;


import android.view.View;
import android.widget.TextView;

import com.turlir.abakgists.R;
import com.turlir.abakgists.model.GistModel;

import timber.log.Timber;

class GistModelHolder extends ModelViewHolder<GistModel> {

    private final TextView mId, mCreated, mDesc, mNote;

    GistModelHolder(View itemView) {
        super(itemView);
        mId = (TextView) itemView.findViewById(R.id.item_gist_id);
        mCreated = (TextView) itemView.findViewById(R.id.item_gist_created);
        mDesc = (TextView) itemView.findViewById(R.id.item_gist_desc);
        mNote = (TextView) itemView.findViewById(R.id.item_gist_note);
        Timber.i("new holder created");
    }

    @Override
    void bind(GistModel item) {
        mId.setText(String.valueOf(item.id));
        mCreated.setText(item.created);
        mNote.setText(item.note);
        mDesc.setText(item.description);
    }

}
