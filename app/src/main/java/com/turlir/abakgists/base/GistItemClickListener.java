package com.turlir.abakgists.base;

import android.widget.ImageView;

import com.turlir.abakgists.model.GistModel;

public interface GistItemClickListener {
    void onListItemClick(GistModel model, ImageView ivAvatar);
}

