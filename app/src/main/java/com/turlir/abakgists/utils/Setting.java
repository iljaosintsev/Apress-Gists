package com.turlir.abakgists.utils;

/**
 * Олицетворяет пару токен против позиция вью, которой он принадлежит
 * Служит для кеширования и удобного манипулирования значением
 * {@link TokenSwitcher.TokenInformator#doesViewToToken(int, int)}
 */
class Setting {

    private final int mToken, mPosition;

    Setting(int token, int position) {
        mToken = token;
        mPosition = position;
    }

    int getToken() {
        return mToken;
    }

    int getPosition() {
        return mPosition;
    }

}
