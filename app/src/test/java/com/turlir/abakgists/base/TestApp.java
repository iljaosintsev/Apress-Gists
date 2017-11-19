package com.turlir.abakgists.base;


/**
 * Robolectric + runtime tools war (leakcanary)
 * see issue on github
 * @see <a href="https://github.com/square/leakcanary/issues/143">#143</a>
 */
@SuppressWarnings("unused")
public class TestApp extends App {

    @Override
    protected void tooling() {
        // nothing
    }

}
