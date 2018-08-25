package com.turlir.abakgists.base;

import android.widget.ImageView;

import com.turlir.abakgists.model.GistModel;

public interface OnClickListener {
    void onListItemClick(GistModel model, ImageView ivAvatar);
}

