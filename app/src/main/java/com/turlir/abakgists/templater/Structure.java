package com.turlir.abakgists.templater;

import com.turlir.abakgists.templater.base.Group;
import com.turlir.abakgists.templater.base.Interceptor;
import com.turlir.abakgists.templater.base.Out;
import com.turlir.abakgists.templater.widget.FormWidget;
import com.turlir.abakgists.templater.widget.WidgetFactory;

import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Structure<T> {

    private final Iterator<Interceptor> mInterceptors;
    private final ListIterator<Node> mIterator;
    private final List<Out<T>> mOuts;
    private final int mCount;

    private final Deque<Group> mGroups;

    Structure(List<Node> nodes, List<Interceptor> interceptors, List<Out<T>> outs, Deque<Group> gr) {
        mInterceptors = interceptors.iterator();
        mIterator = nodes.listIterator();
        mOuts = outs;
        mCount = nodes.size();

        mGroups = gr;
    }

    boolean hasNext() {
        return mIterator.hasNext();
    }

    WidgetHolder next(WidgetFactory factory) {
        Node n = mIterator.next();
        FormWidget widget = factory.widget(n.type);
        return new WidgetHolder(
                widget,
                n.getChecker(),
                mInterceptors.next(),
                n
        );
    }

    int count() {
        return mCount;
    }

    List<Out<T>> outs() {
        return mOuts;
    }

    Mixer groups() {
        return new Mixer(mGroups);
    }
}