package com.turlir.abakgists.utils;

import android.os.Build;

import com.turlir.abakgists.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP, packageName = "com.turlir.abakgists")
public class SwitchLayoutTest {

    private Switching mSwitch; // simple interface testing

    @Before
    public void setup() {
        mSwitch = new SwitchLayout(RuntimeEnvironment.application);
    }

    @Test
    public void toContentTest() throws Exception {
        mSwitch.toContent();
        int c = mSwitch.currentGroup();
        assertEquals(c, SwitchLayout.CONTENT);
    }

    @Test
    public void toErrorTest() throws Exception {
        mSwitch.toError();
        int c = mSwitch.currentGroup();
        assertEquals(c, SwitchLayout.ERROR);
    }

    @Test
    public void toLoadingTest() throws Exception {
        mSwitch.toLoading();
        int c = mSwitch.currentGroup();
        assertEquals(c, SwitchLayout.LOADING);
    }

    @Test
    public void defaultTest() throws Exception {
        int c = mSwitch.currentGroup();
        assertEquals(c, SwitchLayout.LOADING);
    }

}