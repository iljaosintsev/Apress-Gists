package com.turlir.abakgists.allgists;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.turlir.abakgists.base.OnClickListener;
import com.turlir.abakgists.R;
import com.turlir.abakgists.model.GistModel;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class AllGistAdapter extends RecyclerView.Adapter<AllGistAdapter.Holder> {

    private final LayoutInflater mInflater;
    private final OnClickListener mClick;

    private final List<GistModel> mContent;

    public AllGistAdapter(Context cnt, OnClickListener clickListener) {
        mInflater = LayoutInflater.from(cnt);
        mClick = clickListener;
        mContent = new ArrayList<>();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_gist, parent, false);
        final Holder holder = new Holder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mClick.onListItemClick(position);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        GistModel item = mContent.get(position);
        holder.setData(item);
    }

    @Override
    public int getItemCount() {
        return mContent.size();
    }

    public GistModel getItemByPosition(int p) {
        return mContent.get(p);
    }

    public void addGist(List<GistModel> value, int start, int count) {
        int l = mContent.size(); // текущий объем данных

        if (l <= start) { // вставка
            if (count == 1) { // одного элемента
                mContent.add(value.get(0));
                notifyItemInserted(start);
                Timber.i("notifyItemInserted " + l);

            } else { // множества элементов
                mContent.addAll(value);
                notifyItemRangeInserted(l, l + value.size());
                Timber.i("notifyItemRangeInserted " + l + " " + (l + value.size()));
            }

        } else if (l > start) { // обновление
            for (int i = l; i < l + count; i++) {
                int index = i - l;
                GistModel old = mContent.get(index);
                GistModel now = value.get(index);
                if (!now.equals(old)) {
                    mContent.set(index, now);
                    notifyItemChanged(index);
                    Timber.i("notifyItemChanged " + index);
                    break;
                }
            }

        }
    }

    public void removeLastGist() {
        int index = mContent.size() - 1;
        mContent.remove(index);
        notifyItemRemoved(index);
    }

    void clearGist() {
        int c = getItemCount();
        mContent.clear();
        notifyItemRangeRemoved(0, c);
    }

    static class Holder extends RecyclerView.ViewHolder {

        Holder(View itemView) {
            super(itemView);
            Timber.i("new holder created");
        }

        void setData(GistModel item) {
            TextView tvId = (TextView) itemView.findViewById(R.id.item_gist_id);
            tvId.setText(String.valueOf(item.id));

            TextView tvCreated = (TextView) itemView.findViewById(R.id.item_gist_created);
            tvCreated.setText(item.created);

            TextView tvDesc = (TextView) itemView.findViewById(R.id.item_gist_desc);
            tvDesc.setText(item.description);
        }

    }

}
