package com.turlir.abakgists.allgists.view.listing;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.turlir.abakgists.R;
import com.turlir.abakgists.model.InlineErrorModel;
import com.turlir.abakgists.model.InterfaceModel;

import java.util.List;

public class InlineErrorDelegate extends BaseAdapterDelegate {

    public InlineErrorDelegate(LayoutInflater inflater) {
        super(inflater);
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, View itemView) {
        return new InlineErrorModelHolder(itemView);
    }

    @Override
    protected int getLayout() {
        return R.layout.inline_error;
    }

    @Override
    protected boolean isForViewType(@NonNull List<InterfaceModel> items, int position) {
        return items.get(position) instanceof InlineErrorModel;
    }

    @Override
    protected void onBindViewHolder(@NonNull List<InterfaceModel> items, int position,
                                    @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        ((InlineErrorModelHolder) holder).bind((InlineErrorModel) items.get(position));
    }
}
