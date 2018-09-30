package com.turlir.abakgists.allgists.view.listing;


import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

abstract class ModelViewHolder<T> extends RecyclerView.ViewHolder {

    ModelViewHolder(View itemView) {
        super(itemView);
    }

    abstract void bind(T model);

}
