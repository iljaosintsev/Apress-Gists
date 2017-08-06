package com.turlir.abakgists.allgists.view;


import android.support.annotation.Nullable;
import android.view.View;

import com.turlir.abakgists.R;
import com.turlir.abakgists.model.ErrorModel;
import com.turlir.abakgists.model.GistModel;
import com.turlir.abakgists.model.LoadingModel;

public class TypesFactory {

    public int type(GistModel model) {
        return R.layout.item_gist;
    }

    public int type(ErrorModel model) {
        return R.layout.network_error;
    }

    public int type(LoadingModel model) {
        return R.layout.inline_loading;
    }

    ModelViewHolder holder(int viewType, View view) {
        switch (viewType) {
            case R.layout.item_gist:
                return new GistModelHolder(view);

            case R.layout.network_error:
                return new ErrorModelHolder(view);

            case R.layout.inline_loading:
                return new LoadingModelHolder(view);

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
