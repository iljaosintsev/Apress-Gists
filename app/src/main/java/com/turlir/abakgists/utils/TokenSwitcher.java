package com.turlir.abakgists.utils;

import android.support.annotation.NonNull;

/**
 * Класс описывает логику переключения видимости потомков во ViewGroup
 */
class TokenSwitcher {

    private Setting mLastItem;
    private int mToken;
    private final TokenInformator mInformator;

    TokenSwitcher(int token, TokenInformator informator) {
        mToken = token;
        mInformator = informator;
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
        if (mLastItem != null) {

            return child == mLastItem.getPosition();

        } else {

            if (mInformator.doesViewToToken(mToken, child)) {
                mLastItem = new Setting(mToken, child);
                return true;
            } else {
                return false;
            }

        }
    }

    /**
     * @return проверить текущий набор потомков для имеющегося токена
     */
    @NonNull
    ChildDiff invalidateToken() {
        return setToken(mToken);
    }

    /**
     * Установить токен для уже имеющихся потомков
     * @param token токен
     * @return объект, описывающий изменение видимости потомков
     */
    @NonNull
    ChildDiff setToken(int token) {

        ChildDiff.Builder builder = new ChildDiff.Builder();

        if (mLastItem != null) { // есть настройки

            if (token != mLastItem.getToken()) { // группа изменилась

                if (mInformator.getChildCount() > mLastItem.getPosition()
                        && mLastItem.getPosition() > TokenizeLayout.INVALID_INDEX) {
                    builder.hide(mLastItem.getPosition());
                }

                int index = mInformator.getChildIndexByToken(token);
                mLastItem = new Setting(token, index);
                if (index != TokenizeLayout.INVALID_INDEX) {
                    builder.show(index);
                }
            }

        } else { // нет настроек

            int index = mInformator.getChildIndexByToken(token);
            if (index != TokenizeLayout.INVALID_INDEX) {
                mLastItem = new Setting(token, index);
                builder.show(index);
            }

        }

        mToken = token;
        return builder.build();
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
         * @return индекс потомка для данного токена
         */
        int getChildIndexByToken(int token);

    }

}
