package com.turlir.abakgists.base.erroring;

import androidx.annotation.NonNull;

public interface ErrorSelector {
    @NonNull
    ErrorSituation select(Exception ex, boolean dataAvailable, boolean isError);
}
