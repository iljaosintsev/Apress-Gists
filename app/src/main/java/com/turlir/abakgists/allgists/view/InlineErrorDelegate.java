package com.turlir.abakgists.allgists.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.turlir.abakgists.R;

import java.util.List;

public class InlineErrorDelegate extends BaseAdapterDelegate {

    InlineErrorDelegate(LayoutInflater inflater) {
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
    protected boolean isForViewType(@NonNull List<ViewModel> items, int position) {
        return items.get(position) instanceof InlineErrorModel;
    }

    @Override
    protected void onBindViewHolder(@NonNull List<ViewModel> items, int position,
                                    @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        ((InlineErrorModelHolder) holder).bind((InlineErrorModel) items.get(position));
    }
}
