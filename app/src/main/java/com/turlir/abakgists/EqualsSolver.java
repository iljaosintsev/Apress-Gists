package com.turlir.abakgists;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Utility класс для определения относительного состояния двух наборов данных
 * <ul>
 *     <li>старое и новое описание</li>
 *     <li>старая и новая заметка</li>
 * </ul>
 * Итоговый результат вычисляется как логическое ИЛИ от сравнений заметок и описаний.
 */
class EqualsSolver {

    EqualsSolver() {

    }

    boolean solveDescAndNote(@Nullable String oldDesc, @NonNull String nowDes,
                             @Nullable String oldNote, @NonNull String nowNote) {
        return solveDesc(oldDesc, nowDes) || solveNote(oldNote, nowNote);
    }

    boolean solveDesc(@Nullable String old, @NonNull String now) {
        return solveNote(old, now);
    }

    boolean solveNote(@Nullable String old, @NonNull String now) {
        if (old == null) {
            return !now.isEmpty();
        } else {
            return !now.equals(old);
        }
    }

}
