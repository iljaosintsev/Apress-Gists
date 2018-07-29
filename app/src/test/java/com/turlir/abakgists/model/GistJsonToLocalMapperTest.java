package com.turlir.abakgists.model;

import com.turlir.abakgists.api.data.GistJson;
import com.turlir.abakgists.api.data.GistLocal;
import com.turlir.abakgists.api.data.GistMapper;
import com.turlir.abakgists.api.data.GistOwnerJson;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GistJsonToLocalMapperTest {

    private GistMapper.Json mapper;

    @Before
    public void setup() {
        mapper = new GistMapper.Json();
    }

    @Test
    public void callWithOwnerTest() throws Exception {
        GistJson gist = new GistJson("id", "url", "created", "desc", new GistOwnerJson("login", "avatarurl"));
        GistLocal model = mapper.apply(gist);
        GistLocal expected = new GistLocal("id", "url", "created", "desc", "login", "avatarurl");
        assertEquals(expected, model);
    }

    @Test
    public void callWithoutOwnerTest() throws Exception {
        GistJson gist = new GistJson("id", "url", "created", "desc");
        GistLocal model = mapper.apply(gist);
        GistLocal expected = new GistLocal("id", "url", "created", "desc", "", null, null);
        assertEquals(expected, model);
    }


}