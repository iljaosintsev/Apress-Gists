package com.turlir.abakgists.allgists;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.turlir.abakgists.R;
import com.turlir.abakgists.base.OnClickListener;
import com.turlir.abakgists.model.ErrorModel;
import com.turlir.abakgists.model.GistModel;
import com.turlir.abakgists.model.LoadingModel;

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

    public void addGist(List<GistModel> value) {
        final int current = mContent.size(); // текущий объем данных
        final int added = value.size();

        final boolean isNew;
        if (getItemCount() > 0) {
            GistModel lastGist = getGistByPosition(0);
            isNew = lastGist != null && value.get(0).isDifferent(lastGist);
        } else {
            isNew = true;
        }

        if (!isNew) { // обновление существующего набора

            for (int i = current; i < current + added; i++) {
                int index = i - current;
                GistModel old = getGistByPosition(index);
                GistModel now = value.get(index);
                if (old != null && !now.equals(old)) {
                    mContent.set(index, now);
                    notifyItemChanged(index);
                    Timber.i("notifyItemChanged " + index);
                    break; // может быть исправлен только один элемент
                }
            }

        } else { // вставка

            if (added == 1) { // одного элемента
                mContent.add(value.get(0));
                notifyItemInserted(current + 1);
                Timber.i("notifyItemInserted %d", current + 1);

            } else { // множества элементов
                mContent.addAll(value);
                notifyItemRangeInserted(current, current + added);
                Timber.i("notifyItemRangeInserted %d - %d", current, current + added);
            }

        }

    }

    public void removeLastItem() {
        int index = mContent.size() - 1;
        mContent.remove(index);
        notifyItemRemoved(index);
    }

    void addError() {
        int s = mContent.size();
        mContent.add(new ErrorModel());
        notifyItemInserted(s + 1);
    }

    void clearAll() {
        int c = getItemCount();
        mContent.clear();
        notifyItemRangeRemoved(0, c);
    }

    public void addLoading() {
        int s = mContent.size();
        mContent.add(new LoadingModel());
        notifyItemInserted(s + 1);
    }

    public void removeLastIfLoading() {
        int i = getItemCount() - 1;
        ViewModel last = getItemByPosition(i);
        int type = last.type(mFactory);
        if (type == R.layout.inline_loading) {
            mContent.remove(i);
            notifyItemRemoved(i);
        }
    }
}
