package com.turlir.abakgists.allgists.view;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter;
import com.turlir.abakgists.R;
import com.turlir.abakgists.allgists.view.listing.ErrorDelegate;
import com.turlir.abakgists.allgists.view.listing.GistModelDelegate;
import com.turlir.abakgists.allgists.view.listing.InlineErrorDelegate;
import com.turlir.abakgists.allgists.view.listing.LoadingDelegate;
import com.turlir.abakgists.base.GistItemClickListener;
import com.turlir.abakgists.model.ErrorModel;
import com.turlir.abakgists.model.GistModel;
import com.turlir.abakgists.model.InterfaceModel;
import com.turlir.abakgists.model.LoadingModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;
import androidx.recyclerview.widget.RecyclerView;
import timber.log.Timber;

public class AllGistAdapter extends ListDelegationAdapter<List<InterfaceModel>> {

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

    public AllGistAdapter(Context cnt, GistItemClickListener clickListener) {
        setItems(new ArrayList<>());

        LayoutInflater inflater = LayoutInflater.from(cnt);
        mGistDelegate = new GistModelDelegate(inflater, clickListener);
        delegatesManager.addDelegate(R.layout.item_gist, mGistDelegate);
        delegatesManager.addDelegate(R.layout.inline_loading, new LoadingDelegate(inflater));
        delegatesManager.addDelegate(R.layout.inline_error, new InlineErrorDelegate(inflater));
        delegatesManager.addDelegate(R.layout.network_error, new ErrorDelegate(inflater));
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
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

    public void resetGists(List<GistModel> value) {
        DiffUtil.DiffResult diff = DiffUtil.calculateDiff(new GistDiffCallback(items, value));
        diff.dispatchUpdatesTo(this);
        diff.dispatchUpdatesTo(mLoggerAdapterOperations);

        items.clear();
        items.addAll(value);
    }

    @Nullable
    public GistModel getGistByPosition(int p) {
        InterfaceModel item = items.get(p);
        if (getItemViewType(p) == mGistDelegate.getLayout()) {
            return (GistModel) item;
        }
        return null;
    }

    void addError(ErrorModel model) {
        items.add(model);
        notifyItemInserted(items.size());
    }

    void clearAll() {
        int c = getItemCount();
        items.clear();
        notifyItemRangeRemoved(0, c);
    }

    void addLoading(LoadingModel model) {
        items.add(model);
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

    private /*static*/ class GistDiffCallback extends DiffUtil.Callback {

        private final List<InterfaceModel> mOldList;
        private final List<GistModel> mNowList;

        private GistDiffCallback(List<InterfaceModel> oldList, List<GistModel> nowList) {
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
            InterfaceModel old = mOldList.get(oldItemPosition);
            InterfaceModel now = mNowList.get(newItemPosition);
            boolean equals = old.equals(now);
            //if (!equals) Timber.v("contents different %d - %d", oldItemPosition, newItemPosition);
            return equals;
        }
    }
}
