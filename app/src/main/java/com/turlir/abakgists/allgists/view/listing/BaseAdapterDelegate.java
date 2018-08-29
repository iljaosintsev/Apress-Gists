package com.turlir.abakgists.allgists.view.listing;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;
import com.turlir.abakgists.model.InterfaceModel;

import java.util.List;

abstract class BaseAdapterDelegate extends AdapterDelegate<List<InterfaceModel>> {

    private final LayoutInflater mInflater;

    BaseAdapterDelegate(LayoutInflater inflater) {
        mInflater = inflater;
    }

    @NonNull
    protected abstract RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, View itemView);

    protected abstract int getLayout();

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return onCreateViewHolder(
                parent,
                mInflater.inflate(getLayout(), parent, false)
        );
    }
}
