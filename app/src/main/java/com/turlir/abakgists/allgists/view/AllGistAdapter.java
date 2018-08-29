package com.turlir.abakgists.allgists.view;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;
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
        mInflater = LayoutInflater.from(cnt);
        mClick = clickListener;
        mContent = new ArrayList<>();

        mFactory = new TypesFactory();
    }

    @Override
    public int getItemViewType(int position) {
        return mContent.get(position).type(mFactory);
    }

    @NonNull
    @Override
    public ModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(viewType, parent, false);

        final ModelViewHolder holder = mFactory.holder(viewType, view);
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
    public void onBindViewHolder(@NonNull ModelViewHolder holder, int position) {
        ViewModel item = mContent.get(position);
        //noinspection unchecked
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return mContent.size();
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
        return mFactory.instance(item);
    }

    public void resetGists(List<GistModel> value) {
        DiffUtil.DiffResult diff = DiffUtil.calculateDiff(new GistDiffCallback(mContent, value));
        diff.dispatchUpdatesTo(this);
        diff.dispatchUpdatesTo(mLoggerAdapterOperations);

        mContent.clear();
        mContent.addAll(value);
    }

    void addError(String desc) {
        mContent.add(new ErrorModel(desc));
        notifyItemInserted(mContent.size());
    }

    void clearAll() {
        int c = getItemCount();
        mContent.clear();
        notifyItemRangeRemoved(0, c);
    }

    void addLoading(int viewed) {
        mContent.add(new LoadingModel(viewed));
        notifyItemInserted(mContent.size());
    }

    void removeLastIfLoading() {
        int i = getItemCount() - 1;
        if (i > 0) {
            if (getItemViewType(i) == R.layout.inline_loading) {
                mContent.remove(i);
                notifyItemRemoved(i);
            }
        }
    }

    private ViewModel getItemByPosition(int p) {
        return mContent.get(p);
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
