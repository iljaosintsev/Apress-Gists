package com.turlir.tokenizelayout;

class ChildDiff {

    private final Pair[] mInstructions;

    private ChildDiff(Pair[] data) {
        mInstructions = data;
    }

    void apply(ChildManipulator manipulator) {
        for (Pair item : mInstructions) {
            if (item != null) {
                if (item.second) {
                    manipulator.showChild(item.first);
                } else {
                    manipulator.hideChild(item.first);
                }
            }
        }
    }

    static class Builder {

        private Pair[] mInstructions;

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

        final Integer first;
        final Boolean second;

        Pair(Integer first, Boolean second) {
            this.first = first;
            this.second = second;
        }

    }
}
