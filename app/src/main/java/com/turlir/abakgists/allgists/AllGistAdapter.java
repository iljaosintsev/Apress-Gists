package com.turlir.abakgists.allgists;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.turlir.abakgists.base.OnClickListener;
import com.turlir.abakgists.model.GistModel;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class AllGistAdapter extends RecyclerView.Adapter<ModelViewHolder> {

    private final LayoutInflater mInflater;
    private final OnClickListener mClick;

    private final TypesFactory mFactory;
    private final List<ViewModel> mContent;

    public AllGistAdapter(Context cnt, OnClickListener clickListener) {
        mInflater = LayoutInflater.from(cnt);
        mClick = clickListener;
        mContent = new ArrayList<>();

        mFactory = new TypesFactory();
    }

    @Override
    public int getItemViewType(int position) {
        return mContent.get(position).type(mFactory);
    }

    @Override
    public ModelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(viewType, parent, false);

        final ModelViewHolder holder = mFactory.holder(viewType, view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
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
    public void onBindViewHolder(ModelViewHolder holder, int position) {
        ViewModel item = mContent.get(position);
        //noinspection unchecked
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return mContent.size();
    }

    public ViewModel getItemByPosition(int p) {
        return mContent.get(p);
    }

    @Nullable
    public GistModel getGistByPosition(int p) {
        ViewModel item = getItemByPosition(p);
        return mFactory.instance(item);
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
                GistModel old = getGistByPosition(index);
                GistModel now = value.get(index);
                if (old != null && !now.equals(old)) {
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

}
