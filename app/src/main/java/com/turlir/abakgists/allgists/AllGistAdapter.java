package com.turlir.abakgists.allgists;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.turlir.abakgists.base.OnClickListener;
import com.turlir.abakgists.R;
import com.turlir.abakgists.model.Gist;

import java.util.ArrayList;
import java.util.List;

public class AllGistAdapter extends RecyclerView.Adapter<AllGistAdapter.Holder> {

    private static final String TAG = "AllGistAdapter";

    private final LayoutInflater mInflater;
    private final OnClickListener mClick;

    private final List<Gist> mContent;

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
        Gist item = mContent.get(position);
        holder.setData(item);
    }

    @Override
    public int getItemCount() {
        return mContent.size();
    }

    public Gist getItemByPosition(int p) {
        return mContent.get(p);
    }

    public void addGist(List<Gist> value, int start, int offset) {
        int l = mContent.size();
        if (l <= start) { // вставка
            mContent.addAll(value);
            Log.d(TAG, "notifyItemRangeInserted " + l + " " + value.size());
            notifyItemRangeInserted(l, l + value.size());
        } else if (l > start) { // обновление
            for (int i = l; i < l + offset; i++) {
                Gist old = mContent.get(i - l);
                Gist now = value.get(i - l);
                if (!now.equals(old)) {
                    mContent.set(i - l, value.get(i - l));
                    notifyItemChanged(i - l);
                    Log.d(TAG, "notifyItemChanged " + (i - l));
                    return;
                }
            }
        }
    }

    void clearGist() {
        int c = getItemCount();
        notifyItemRangeRemoved(0, c);
        mContent.clear();
    }

    static class Holder extends RecyclerView.ViewHolder {

        Holder(View itemView) {
            super(itemView);
            Log.i(TAG, "new holder created");
        }

        void setData(Gist item) {
            TextView tvId = (TextView) itemView.findViewById(R.id.item_gist_id);
            tvId.setText(String.valueOf(item.id));

            TextView tvCreated = (TextView) itemView.findViewById(R.id.item_gist_created);
            tvCreated.setText(item.created);

            TextView tvDesc = (TextView) itemView.findViewById(R.id.item_gist_desc);
            tvDesc.setText(item.description);
        }

    }

}
