package com.turlir.abakgists.allgists.view.listing;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.turlir.abakgists.R;
import com.turlir.abakgists.model.LoadingModel;
import com.turlir.abakgists.model.InterfaceModel;

import java.util.List;

public class LoadingDelegate extends BaseAdapterDelegate {

    public LoadingDelegate(LayoutInflater inflater) {
        super(inflater);
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, View itemView) {
        return new LoadingModelHolder(itemView);
    }

    @Override
    protected int getLayout() {
        return R.layout.inline_loading;
    }

    @Override
    protected boolean isForViewType(@NonNull List<InterfaceModel> items, int position) {
        return items.get(position) instanceof LoadingModel;
    }

    @Override
    protected void onBindViewHolder(@NonNull List<InterfaceModel> items, int position,
                                    @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        ((LoadingModelHolder) holder).bind((LoadingModel) items.get(position));
    }
}
