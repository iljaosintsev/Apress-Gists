package com.turlir.abakgists.model;

import com.turlir.abakgists.Data;
import com.turlir.abakgists.api.data.GistMapper;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GistLocalToModelTest {

    private GistMapper.Local mapper;

    @Before
    public void setup() {
        mapper = new GistMapper.Local();
    }

    @Test
    public void simpleTest() {
        GistModel actual = mapper.call(Data.LOCAL_STUB);
        assertNotNull(actual);

        assertEquals(Data.USER_STUB_F, actual);
    }

    @Test
    public void localFlagTest() {
        mapper.setLocal(true);
        GistModel actual = mapper.call(Data.LOCAL_STUB);
        assertNotNull(actual);

        assertEquals(Data.USER_STUB_T, actual);
    }



}
