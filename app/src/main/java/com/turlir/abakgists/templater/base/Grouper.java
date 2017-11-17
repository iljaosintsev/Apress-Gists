package com.turlir.abakgists.templater.base;

import android.view.ViewGroup;

public interface Grouper {
    ViewGroup changeRoot(String tag, ViewGroup origin, ViewGroup current);
}
