package com.turlir.abakgists.allgists;

import com.turlir.abakgists.allgists.combination.ListManipulator;
import com.turlir.abakgists.model.GistModel;

class GistLoader extends Loader<GistModel> {

    //private int mPreviousSize;

    GistLoader(WindowedRepository<GistModel> interactor, ListManipulator<GistModel> callback) {
       super(interactor, callback);
    }

    @Override
    boolean shouldRequest(boolean lessThan, boolean eqLast) {
        return eqLast && lessThan && canNext();
    }

    @Override
    boolean shouldRender(int size) {
        /*boolean v = mPreviousSize != size;
        mPreviousSize = size;
        return v;*/
        return true;
    }
}
