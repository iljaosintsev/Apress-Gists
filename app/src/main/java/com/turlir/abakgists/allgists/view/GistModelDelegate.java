package com.turlir.abakgists.allgists.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.turlir.abakgists.R;
import com.turlir.abakgists.model.GistModel;
import com.turlir.abakgists.model.InterfaceModel;

import java.util.List;

public class GistModelDelegate extends BaseAdapterDelegate {

    GistModelDelegate(LayoutInflater inflater) {
        super(inflater);
    }

    @Override
    protected boolean isForViewType(@NonNull List<InterfaceModel> items, int position) {
        return items.get(position) instanceof GistModel;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, View itemView) {
        GistModelHolder holder = new GistModelHolder(itemView);
        /*holder.itemView.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                mClick.onGistItemClick(position, holder.ivAvatar);
            }
        });*/
        return holder;
    }

    @Override
    protected int getLayout() {
        return R.layout.item_gist;
    }

    @Override
    protected void onBindViewHolder(@NonNull List<InterfaceModel> items, int position,
                                    @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        GistModelHolder gmh = ((GistModelHolder) holder);
        GistModel data = (GistModel) items.get(position);
        gmh.bind(data);
    }
}
