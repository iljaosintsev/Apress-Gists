package com.turlir.abakgists.gistsloader;

import android.support.annotation.Nullable;

import com.turlir.abakgists.base.loader.Window;
import com.turlir.abakgists.base.loader.server.StateServerBehavior;
import com.turlir.abakgists.model.Identifiable;

import java.util.List;

public class DoLoadBehavior<T extends Identifiable<T>> implements StateServerBehavior<T> {

    @Nullable
    private T mLast;

    @Override
    public boolean shouldRequest(List<T> nextItems, Window window) {
        int nowSize = nextItems.size();
        if (nowSize != 0) {
            boolean lessThan = nowSize < window.count();
            final boolean eqLast;
            T lastItem = nextItems.get(nowSize - 1);
            //eqLast = mLast == null || !mObj.isDifferent(lastItem, mLast);
            eqLast = mLast == null || !lastItem.isDifferent(mLast);
            mLast = lastItem;
            return lessThan && eqLast;
        } else {
            return true;
        }
    }

    @Override
    public boolean shouldRender(List<T> items, Window window) {
        return items.size() > 0;
    }

    @Override
    public void afterRender(List<T> last) {
        mLast = last.get(last.size() - 1);
    }

    @Override
    public boolean lastEqual(T last) {
        //return mLast != null && mObj.isDifferent(mLast, last);
        return mLast != null && mLast.isDifferent(last);
    }
}
