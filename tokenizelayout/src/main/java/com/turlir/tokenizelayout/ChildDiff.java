package com.turlir.tokenizelayout;

final class ChildDiff {

    private final Pair[] mInstructions;

    private ChildDiff(Pair[] data) {
        mInstructions = data;
    }

    void apply(ChildManipulator manipulator) {
        for (Pair item : mInstructions) {
            if (item != null) {
                if (item.visibility) {
                    manipulator.showChild(item.index);
                } else {
                    manipulator.hideChild(item.index);
                }
            }
        }
    }

    boolean isEmpty() {
        return mInstructions[0] == null && mInstructions[1] == null;
    }

    static class Builder {

        private final Pair[] mInstructions;

        Builder() {
            mInstructions = new Pair[2];
        }

        Builder hide(int position) {
            mInstructions[0] = new Pair(position, false);
            return this;
        }

        Builder show(int position) {
            mInstructions[1] = new Pair(position, true);
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

    private static class Pair {

        final Integer index;
        final Boolean visibility;

        Pair(Integer index, Boolean visibility) {
            this.index = index;
            this.visibility = visibility;
        }
    }
}
