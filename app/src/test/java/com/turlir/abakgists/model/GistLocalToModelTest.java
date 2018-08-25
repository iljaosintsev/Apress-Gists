package com.turlir.abakgists.model;

import com.turlir.abakgists.Data;
import com.turlir.abakgists.api.data.GistMapper;

import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GistLocalToModelTest {

    private GistMapper.Local mapper;

    @Before
    public void setup() {
        mapper = new GistMapper.Local(Locale.forLanguageTag("ru-RU"));
    }

    @Test
    public void simpleTest() {
        mapper.isLocal = false;
        GistModel actual = mapper.apply(Data.LOCAL_STUB);
        assertNotNull(actual);
        assertEquals(Data.LOCAL_STUB_FALSE, actual);
    }

    @Test
    public void localFlagTest() {
        mapper.isLocal = true;
        GistModel actual = mapper.apply(Data.LOCAL_STUB);
        assertNotNull(actual);
        assertEquals(Data.LOCAL_STUB_TRUE, actual);
    }



}
