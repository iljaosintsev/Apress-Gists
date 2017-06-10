package com.turlir.abakgists.allgists;


import android.support.v7.widget.RecyclerView;
import android.view.View;

abstract class ModelViewHolder<T> extends RecyclerView.ViewHolder {

    public ModelViewHolder(View itemView) {
        super(itemView);
    }

    abstract void bind(T model);

}
