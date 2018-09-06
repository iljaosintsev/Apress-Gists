package com.turlir.abakgists.allgists;

import com.turlir.abakgists.allgists.combination.ErrorProcessor;
import com.turlir.abakgists.allgists.combination.ListManipulator;
import com.turlir.abakgists.allgists.loader.Loader;
import com.turlir.abakgists.allgists.loader.WindowedRepository;
import com.turlir.abakgists.model.GistModel;

class GistLoader extends Loader<GistModel> {

    //private int mPreviousSize;

    GistLoader(WindowedRepository<GistModel> interactor, ListManipulator<GistModel> callback, ErrorProcessor processor) {
       super(interactor, callback, processor);
    }

    @Override
    public boolean shouldRequest(boolean lessThan, boolean eqLast) {
        return eqLast && lessThan && canNext();
    }

    @Override
    public boolean shouldRender(int size) {
        /*boolean v = mPreviousSize != size;
        mPreviousSize = size;
        return v;*/
        return true;
    }
}
