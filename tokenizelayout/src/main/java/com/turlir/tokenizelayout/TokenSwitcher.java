package com.turlir.tokenizelayout;

import android.support.annotation.NonNull;

/**
 * Класс описывает логику переключения видимости потомков во ViewGroup
 */
public class TokenSwitcher {

    /**
     * Неправильный индекс потомка
     */
    public static final int INVALID_INDEX = -1;

    private int mLastItem;
    private int mToken;
    private final TokenInformator mInformator;

    TokenSwitcher(int token, TokenInformator informator) {
        mToken = token;
        mInformator = informator;
        mLastItem = INVALID_INDEX;
    }

    /**
     * @return текущий токен
     */
    int currentToken() {
        return mToken;
    }

    /**
     * применить текущий токен к указанному потомку
     * @param child индекс потомка
     * @return {@code true}, если потомка следует отобразить
     */
    boolean applyGroupByChild(int child) {
        if (mLastItem != INVALID_INDEX) {
            return child == mLastItem;
        } else if (mInformator.doesViewToToken(mToken, child)) {
            mLastItem = child;
            return true;
        } else {
            return false;
        }
    }

    /**
     * проверяет текущий набор потомков для имеющегося токена
     */
    void invalidateToken() {
        setToken(mToken);
    }

    /**
     * Установить токен для уже имеющихся потомков
     * @param token токен
     * @return объект, описывающий изменение видимости потомков
     */
    @NonNull
    ChildDiff setToken(int token) {
        ChildDiff.Builder builder = new ChildDiff.Builder();
        if (mLastItem != INVALID_INDEX) { // есть настройки
            if (token != mToken) { // токен изменился
                newToken(token, builder);
            }
        } else { // нет настроек
            int index = mInformator.getChildIndexByToken(token);
            if (index != INVALID_INDEX) {
                mLastItem = index;
                builder.show(index);
            }
            mToken = token;
        }
        return builder.build();
    }

    private void newToken(int token, ChildDiff.Builder builder) {
        int cc = mInformator.getChildCount();
        if (cc > mLastItem && mLastItem > INVALID_INDEX) {
            builder.hide(mLastItem);
        }
        if (cc > 0) {
            int index = mInformator.getChildIndexByToken(token);
            mLastItem = index;
            if (index != INVALID_INDEX) {
                builder.show(index);
                mToken = token;
            }
        }
    }

    /**
     * Интерфейс обратного вызова для получения информации о ViewGroup
     */
    interface TokenInformator {

        /**
         * @param token токен
         * @param position позиция потомка
         * @return принадлежит ли токен вью
         */
        boolean doesViewToToken(int token, int position);

        /**
         * @return общее количество потомков
         */
        int getChildCount();

        /**
         * @param token токен
         * @return индекс потомка для данного токена или {@link TokenSwitcher#INVALID_INDEX}
         */
        int getChildIndexByToken(int token);
    }
}
