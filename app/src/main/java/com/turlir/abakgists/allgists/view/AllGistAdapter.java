package com.turlir.abakgists.allgists.view;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter;
import com.turlir.abakgists.R;
import com.turlir.abakgists.base.OnClickListener;
import com.turlir.abakgists.model.ErrorModel;
import com.turlir.abakgists.model.GistModel;
import com.turlir.abakgists.model.LoadingModel;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class AllGistAdapter extends ListDelegationAdapter<List<ViewModel>> {

    private final OnClickListener mClick;
    private final GistModelDelegate mGistDelegate;

    private final ListUpdateCallback mLoggerAdapterOperations = new ListUpdateCallback() {
        @Override
        public void onInserted(int position, int count) {
            Timber.d("onInserted %d %d", position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            Timber.d("onRemoved %d %d", position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            Timber.d("onMoved %d %d", fromPosition, toPosition);
        }

        @Override
        public void onChanged(int position, int count, Object payload) {
            Timber.d("onChanged %d %d", position, count);
        }
    };

    public AllGistAdapter(Context cnt, OnClickListener clickListener) {
        LayoutInflater inflater = LayoutInflater.from(cnt);
        mClick = clickListener;
        setItems(new ArrayList<>());

        mGistDelegate = new GistModelDelegate(inflater);
        delegatesManager.addDelegate(R.layout.item_gist, mGistDelegate);
        delegatesManager.addDelegate(R.layout.inline_loading, new LoadingDelegate(inflater));
        delegatesManager.addDelegate(R.layout.inline_error, new InlineErrorDelegate(inflater));
        delegatesManager.addDelegate(R.layout.network_error, new ErrorDelegate(inflater));
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = super.onCreateViewHolder(parent, viewType);
        holder.itemView.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                GistModel g = getGistByPosition(position);
                if (g != null) {
                    mClick.onListItemClick(g, ((GistModelHolder) holder).ivAvatar);
                }
            }
        });
        return holder;
    }

    @Override
    public long getItemId(int position) {
        GistModel maybeGist = getGistByPosition(position);
        if (maybeGist != null) {
            return Long.parseLong(maybeGist.id);
        } else {
            return position;
        }
    }

    @Nullable
    public GistModel getGistByPosition(int p) {
        ViewModel item = getItemByPosition(p);
        if (getItemViewType(p) == mGistDelegate.getLayout()) {
            return (GistModel) item;
        }
        return null;
    }

    public void resetGists(List<GistModel> value) {
        DiffUtil.DiffResult diff = DiffUtil.calculateDiff(new GistDiffCallback(items, value));
        diff.dispatchUpdatesTo(this);
        diff.dispatchUpdatesTo(mLoggerAdapterOperations);

        items.clear();
        items.addAll(value);
    }

    void addError(String desc) {
        items.add(new ErrorModel(desc));
        notifyItemInserted(items.size());
    }

    void clearAll() {
        int c = getItemCount();
        items.clear();
        notifyItemRangeRemoved(0, c);
    }

    void addLoading(int viewed) {
        items.add(new LoadingModel(viewed));
        notifyItemInserted(items.size());
    }

    void removeLastIfLoading() {
        int i = getItemCount() - 1;
        if (i > 0) {
            if (getItemViewType(i) == R.layout.inline_loading) {
                items.remove(i);
                notifyItemRemoved(i);
            }
        }
    }

    private ViewModel getItemByPosition(int p) {
        return items.get(p);
    }

    private /*static*/ class GistDiffCallback extends DiffUtil.Callback {

        private final List<ViewModel> mOldList;
        private final List<GistModel> mNowList;

        private GistDiffCallback(List<ViewModel> oldList, List<GistModel> nowList) {
            this.mOldList = oldList;
            this.mNowList = nowList;
        }

        @Override
        public int getOldListSize() {
            return mOldList.size();
        }

        @Override
        public int getNewListSize() {
            return mNowList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            GistModel oldModel = getGistByPosition(oldItemPosition);
            if (oldModel != null) {
                GistModel now = mNowList.get(newItemPosition);
                boolean diff = now.isDifferent(oldModel);
                //if (diff) Timber.v("items different %d - %d", oldItemPosition, newItemPosition);
                return !diff;
            } else {
                //Timber.v("items incomparable %d - %d", oldItemPosition, newItemPosition);
                return false;
            }
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            ViewModel old = mOldList.get(oldItemPosition);
            ViewModel now = mNowList.get(newItemPosition);
            boolean equals = old.equals(now);
            //if (!equals) Timber.v("contents different %d - %d", oldItemPosition, newItemPosition);
            return equals;
        }
    }
}
