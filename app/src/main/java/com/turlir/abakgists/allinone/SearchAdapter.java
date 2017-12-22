package com.turlir.abakgists.allinone;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.turlir.abakgists.R;

import java.util.ArrayList;
import java.util.List;

class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SimpleHolder> {

    private final List<String> mData;

    SearchAdapter() {
        mData = new ArrayList<>();
    }

    @Override
    public SearchAdapter.SimpleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.toolbar_search_item, parent, false);
        return new SearchAdapter.SimpleHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchAdapter.SimpleHolder holder, int position) {
        holder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    void replace(List<String> item) {
        mData.clear();
        mData.addAll(item);
        notifyDataSetChanged();
    }

    static class SimpleHolder extends RecyclerView.ViewHolder {

        SimpleHolder(View itemView) {
            super(itemView);
        }

        public void bind(String s) {
            ((TextView) itemView).setText(s);
        }
    }

}
