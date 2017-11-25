package com.turlir.abakgists.templater;

import com.turlir.abakgists.templater.base.Group;
import com.turlir.abakgists.templater.base.Interceptor;
import com.turlir.abakgists.templater.base.Out;
import com.turlir.abakgists.templater.check.Checker;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public abstract class BaseBuilder<T, B extends BaseBuilder<T, B>>  {

    private final List<Node> mNodes;
    private final List<Interceptor> mInterceptors;
    private final List<Out<T>> mOuts;

    private final Deque<Group> mGroups;

    public BaseBuilder() {
        mNodes = new ArrayList<>();
        mInterceptors = new ArrayList<>();
        mOuts = new ArrayList<>();

        mGroups = new LinkedList<>();
    }

    //

    public final B add(String type, String name, String hint, String example, String tag, Interceptor inter) {
        return add(type, name, hint, example, tag, false, inter);
    }

    //

    public final B add(String type, String name, String hint, String example, String tag, boolean required,
                       Interceptor interceptor) {
        Node n = new Node(type, name, hint, example, tag, mNodes.size(), required);
        privateAdd(interceptor, n);
        return getThis();
    }

    public final B add(String type, String name, String hint, String example, String tag, int min, int max,
                       Interceptor interceptor) {
        Node n = new Node(type, name, hint, example, tag, mNodes.size(), min, max);
        privateAdd(interceptor, n);
        return getThis();
    }

    public final B add(String type, String name, String hint, String example, String tag, String regexp,
                       Interceptor interceptor) {
        Node n = new Node(type, name, hint, example, tag, mNodes.size(), regexp);
        privateAdd(interceptor, n);
        return getThis();
    }

    public final B add(String type, String name, String hint, String example, String tag, Checker checker,
                       Interceptor interceptor) {
        Node n = new Node(type, name, hint, example, tag, mNodes.size(), checker);
        privateAdd(interceptor, n);
        return getThis();
    }

    //

    public final B add(String type, String name, String tag, boolean required, Interceptor interceptor) {
        return add(type, name, null, null, tag, required, interceptor);
    }

    public final B add(String type, String name, String tag, int min, int max, Interceptor interceptor) {
        return add(type, name, null, null, tag, min, max, interceptor);
    }

    public final B add(String type, String name, String tag, String regexp, Interceptor interceptor) {
        return add(type, name, null, null, tag, regexp, interceptor);
    }

    public final B add(String type, String name, String tag, Checker checker, Interceptor interceptor) {
        return add(type, name, null, null, tag, checker, interceptor);
    }

    //

    public final B out(Out<T> o) {
        if (mNodes.size() > 0) {
            mOuts.set(mNodes.size() - 1, o);
        } else {
            throw new IllegalStateException();
        }
        return getThis();
    }

    public final B in(Interceptor callback) {
        int last = Math.max(mInterceptors.size() - 1, 0);
        mInterceptors.set(last, callback);
        return getThis();
    }

    public final B startGroup() {
        int index = mNodes.size();
        if (mGroups.size() > 0 && !mGroups.getLast().isClose()) { // если такая группа уже есть
            throw new UnsupportedOperationException();
        }
        Group group = new Group(mGroups.size(), index + 1);
        mGroups.add(group);
        return getThis();
    }

    public final B endGroup() {
        Group lastGroup = mGroups.getLast();
        lastGroup.close(mNodes.size());
        lastGroup.shift();
        return getThis();
    }

    public final Structure<T> build() {
        return new Structure<>(mNodes, mInterceptors, mOuts, mGroups);
    }

    protected abstract B getThis();

    private void privateAdd(Interceptor interceptor, Node n) {
        mNodes.add(n);
        mInterceptors.add(interceptor);
        mOuts.add(null);
    }
}
