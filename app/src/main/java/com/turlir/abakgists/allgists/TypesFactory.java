package com.turlir.abakgists.allgists;


import android.support.annotation.Nullable;
import android.view.View;

import com.turlir.abakgists.R;
import com.turlir.abakgists.model.GistModel;

public class TypesFactory {

    public int type(GistModel model) {
        return R.layout.item_gist;
    }

    ModelViewHolder holder(int viewType, View view) {
        switch (viewType) {
            case R.layout.item_gist:
                return new GistModelHolder(view);

            default:
                return null;
        }
    }

    @Nullable
    GistModel instance(ViewModel viewModel) {
        int actual = viewModel.type(this);
        if (actual == R.layout.item_gist) {
            return (GistModel) viewModel;
        } else {
            return null;
        }
    }
}
