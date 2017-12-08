package com.turlir.abakgists.allgists.view;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.turlir.abakgists.R;
import com.turlir.abakgists.model.GistModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

class GistModelHolder extends ModelViewHolder<GistModel> {

    @BindView(R.id.item_gist_nick)
    TextView tvLogin;

    @BindView(R.id.item_gist_created)
    TextView tvCreated;

    @BindView(R.id.item_gist_desc)
    TextView tvDesc;

    @BindView(R.id.item_gist_number)
    TextView tvNumber;

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
            tvLogin.setText(R.string.anonymous);
        } else {
            tvLogin.setText(item.ownerLogin);
        }

        tvCreated.setText(item.created);
        tvNote.setText(item.note);
        tvDesc.setText(item.description);
        int p = getAdapterPosition();
        if (p != RecyclerView.NO_POSITION) {
            tvNumber.setText(String.valueOf(p));
        } else {
            tvNumber.setText(String.valueOf(0));
        }
    }

}
