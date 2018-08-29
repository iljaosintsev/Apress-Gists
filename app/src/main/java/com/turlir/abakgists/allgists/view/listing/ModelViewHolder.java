package com.turlir.abakgists.allgists.view.listing;


import android.support.v7.widget.RecyclerView;
import android.view.View;

abstract class ModelViewHolder<T> extends RecyclerView.ViewHolder {

    ModelViewHolder(View itemView) {
        super(itemView);
    }

    abstract void bind(T model);

}
