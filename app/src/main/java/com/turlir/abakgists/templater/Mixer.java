package com.turlir.abakgists.templater;

import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.turlir.abakgists.templater.base.Group;
import com.turlir.abakgists.templater.base.Grouper;

import java.util.Iterator;
import java.util.Queue;

class Mixer {

    private final Queue<Group> mGroups;

    Mixer(Queue<Group> groups) {
        mGroups = groups;
    }

    Iterator<ViewGroup> iterator(final ViewGroup mainRoot, Grouper hack) {
        if (mGroups.size() > 0) {
            return new RootIterator(mainRoot, hack);
        } else {
            return new RootIterator(mainRoot, hack);
        }
    }

    private class RootIterator implements Iterator<ViewGroup> {

        private final Grouper mHack; // интерфейс обратного вызова для смены рута
        private final ViewGroup mBackup; // оригинальный рут для добавления
        private ViewGroup mLastRoot; // куда последний раз добавили (для групп из > 1 элемента)

        @Nullable
        private Group mLastGroup; // группа, в которой сейчас находимся или null
        private int mIndex; /// счетчик виджетов на форме

        private RootIterator(ViewGroup mainRoot, Grouper hack) {
            mLastRoot = null;
            mBackup = mainRoot;
            mHack = hack;
            mIndex = 0;
        }

        @Override
        public boolean hasNext() {
            throw new UnsupportedOperationException();
        }

        @Override
        public ViewGroup next() {

            if (!mGroups.isEmpty() || mLastGroup != null) { // группы еще есть или последняя не закрыта

                if (mIndex == 0) { // получение первой группы
                    mLastGroup = mGroups.poll();
                }

                // очередной элемент НЕ входит в группу
                // или защита от NPE
                if (mLastGroup == null || !mLastGroup.does(mIndex) ) {
                    mIndex++;
                    mLastRoot = null; // забываем последнюю группу
                    return mBackup; // оригинальный рут

                } else {

                    if (mLastRoot == null) { // первый раз зашли в группу
                        mLastRoot = mHack.changeRoot(mLastGroup.number);
                        mBackup.addView(mLastRoot); // добавляем новый рут в оригинальный
                    }
                    mIndex++;
                    if (!mLastGroup.does(mIndex) ) { // если следующий элемент НЕ входит в группе
                        mLastGroup = mGroups.poll(); // получаем следующую группу или null
                    }

                    return mLastRoot;
                }

            } else {
                return mBackup; // групп нет
            }

        }
    }

}
