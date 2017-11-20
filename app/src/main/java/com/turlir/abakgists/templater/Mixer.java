package com.turlir.abakgists.templater;

import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.turlir.abakgists.templater.base.Group;
import com.turlir.abakgists.templater.base.Grouper;

import java.util.HashMap;
import java.util.Iterator;

class Mixer {

    private final HashMap<Integer, Group> mGroups;

    Mixer(HashMap<Integer, Group> groups) {
        mGroups = groups;
    }

    Iterator<ViewGroup> iterator(ViewGroup mainRoot, Grouper hack) {
        return new RootIterator(mainRoot, hack);
    }

    private class RootIterator implements Iterator<ViewGroup> {

        private final Grouper mHack; // интерфейс обратного вызова для смены рута
        private final ViewGroup mBackup; // оригинальный рут для добавления
        private ViewGroup mLastRoot; // куда последний раз добавили (для групп из > 1 элемента)

        @Nullable
        private Group mLastGroup; // группа, в которой сейчас находимся или null
        private int mIndex; /// счетчик виджетов на форме

        private RootIterator(ViewGroup mainRoot, Grouper hack) {
            mLastRoot = mainRoot;
            mBackup = mainRoot;
            mHack = hack;
            mIndex = 0;
        }

        @Override
        public boolean hasNext() {
            return mIndex < Integer.MAX_VALUE;
        }

        @Override
        public ViewGroup next() {
            if (mGroups.size() < 1) {
                return mBackup;
            }

            if (mGroups.containsKey(mIndex)) {
                mLastGroup = mGroups.get(mIndex);
                mLastRoot = mHack.changeRoot(mLastGroup.number);
                mBackup.addView(mLastRoot); // обяз.

                mIndex++;
                return mLastRoot;

            } else if (mLastGroup != null && mLastGroup.does(mIndex)) {
                mIndex++;
                return mLastRoot;

            } else {
                mIndex++;
                mLastGroup = null;
                return mBackup;
            }

        }
    }

}
