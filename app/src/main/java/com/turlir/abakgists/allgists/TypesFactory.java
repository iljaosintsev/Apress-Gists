package com.turlir.abakgists.allgists;


import android.support.annotation.Nullable;
import android.view.View;

import com.turlir.abakgists.model.GistModel;

public class TypesFactory {

    static final int TYPE_GIST = 0;

    public int type(GistModel model) {
        return TYPE_GIST;
    }

    ModelViewHolder holder(int viewType, View view) {
        switch (viewType) {
            case TYPE_GIST:
                return new GistModelHolder(view);

            default:
                return null;
        }
    }

    @Nullable
    GistModel instance(ViewModel viewModel, Class<GistModel> classz) {
        if (viewModel.getClass() == classz) {
            return (GistModel) viewModel;
        } else {
            return null;
        }
    }
}
