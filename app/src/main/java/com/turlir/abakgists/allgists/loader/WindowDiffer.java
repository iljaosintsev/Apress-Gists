package com.turlir.abakgists.allgists.loader;

public class WindowDiffer {

    public LoadablePage page(Window w) {
        return page(w, w.count());
    }

    private LoadablePage page(Window w, int perPage) {
        return new LoadablePage(w.start(), w.start() + perPage);
    }

    public Window cut(Window w, int size) {
        if (size > w.count()) throw new IllegalArgumentException();
        return new Range(w.start(), w.start() + size, w.addition());
    }

    public Window diff(Window a, Window o) {
        int required = a.count() - o.count();
        if (required < 1 || a.start() != o.start()) {
            throw new IllegalArgumentException();
        }
        int at = o.stop();
        if (at % required == 0) {
            return new Range(at, a.stop(), required);
        } else {
            int center = a.start() + a.count() / 2;
            if (at > center) {
                return a.downScale(2);
            } else {
                return a;
            }
        }
    }
}
