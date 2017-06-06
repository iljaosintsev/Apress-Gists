package com.turlir.abakgists.utils;

import android.util.Pair;

/**
 * Класс описывает логику переключения видимости потомков во ViewGroup
 */
class TokenSwitcher {

    private Setting mLastItem;
    private final int mToken;
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
    Pair<Integer, Boolean>[] invalidateToken() {
        return setToken(mToken);
    }

    /**
     * Установить токен для уже имеющихся потомков
     * @param group токен
     * @return набор размерности 2 diff инструкций по отображению/скрытию определенных потомков.
     * Элементы набора могут быть null
     */
    Pair<Integer, Boolean>[] setToken(int group) {

        Pair<Integer, Boolean>[] arr = new Pair[2];

        if (mLastItem != null) { // есть настройки

            if (group != mLastItem.getToken()) { // группа изменилась

                if (mInformator.getChildCount() > mLastItem.getPosition()
                        && mLastItem.getPosition() > TokenizeLayout.INVALID_INDEX) {
                    arr[0] = new Pair<>(mLastItem.getPosition(), false);
                }

                int index = mInformator.getChildIndexByToken(group);
                mLastItem = new Setting(group, index);
                if (index != TokenizeLayout.INVALID_INDEX) {
                    arr[1] = new Pair<>(index, true);
                }
            }

        } else { // нет настроек

            int index = mInformator.getChildIndexByToken(group);
            if (index != TokenizeLayout.INVALID_INDEX) {
                mLastItem = new Setting(group, index);
                arr[0] = new Pair<>(index, true);
            }

        }

        return arr;
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
