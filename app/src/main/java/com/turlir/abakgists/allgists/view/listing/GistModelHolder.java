package com.turlir.abakgists.allgists.view.listing;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.turlir.abakgists.R;
import com.turlir.abakgists.model.GistModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class GistModelHolder extends ModelViewHolder<GistModel> {

    @BindView(R.id.item_gist_avatar)
    public ImageView ivAvatar;

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
        final String prefix;
        if (item.ownerLogin == null) {
            prefix = tvId.getResources().getString(R.string.anonymous);
        } else {
            prefix = item.ownerLogin;
        }
        tvId.setText(prefix);
        tvCreated.setText(item.created);
        tvNote.setText(item.note);
        tvDesc.setText(item.description);
        Picasso.with(ivAvatar.getContext())
                .load(item.ownerAvatarUrl)
                .fit()
                .error(R.drawable.ic_github)
                .placeholder(R.drawable.ic_github)
                .into(ivAvatar);
    }

}
