package com.turlir.abakgists;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.turlir.abakgists.model.Gist;

import java.util.ArrayList;
import java.util.List;

class AllGistAdapter extends RecyclerView.Adapter<AllGistAdapter.Holder> {

    private final LayoutInflater mInflater;

    private final List<Gist> mContent;

    AllGistAdapter(Context cnt) {
        mInflater = LayoutInflater.from(cnt);
        mContent = new ArrayList<>();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_gist, parent, false);
        return new Holder(view);

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

    void addGist(List<Gist> value) {
        int start = mContent.size();
        mContent.addAll(value);
        this.notifyItemRangeInserted(start, start + value.size());
    }

    static class Holder extends RecyclerView.ViewHolder {

        Holder(View itemView) {
            super(itemView);
            Log.i("HOLDER", "new holder created");
        }

        void setData(Gist item) {
            TextView tvId = (TextView) itemView.findViewById(R.id.item_gist_id);
            tvId.setText(item.id);

            TextView tvCreated = (TextView) itemView.findViewById(R.id.item_gist_created);
            tvCreated.setText(item.created);

            TextView tvDesc = (TextView) itemView.findViewById(R.id.item_gist_desc);
            tvDesc.setText(item.description);
        }

    }

}
