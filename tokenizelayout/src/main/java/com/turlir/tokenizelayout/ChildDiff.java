package com.turlir.tokenizelayout;

import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.List;

class ChildDiff {

    private final List<Pair<Integer, Boolean>> mInstructions;

    private ChildDiff(List<Pair<Integer, Boolean>> data) {
        mInstructions = data;
    }

    void apply(ChildManipulator manipulator) {
        for (Pair<Integer, Boolean> item : mInstructions) {
            if (item.second) {
                manipulator.showChild(item.first);
            } else {
                manipulator.hideChild(item.first);
            }
        }
    }

    static class Builder {

        private List<Pair<Integer, Boolean>> mInstructions;

        Builder() {
            mInstructions = new ArrayList<>(2);
        }

        Builder hide(int position) {
            mInstructions.add(new Pair<>(position, false));
            return this;
        }

        Builder show(int position) {
            mInstructions.add(new Pair<>(position, true));
            return this;
        }

        ChildDiff build() {
            return new ChildDiff(mInstructions);
        }

    }

    interface ChildManipulator {

        void hideChild(int i);

        void showChild(int i);

    }
}
