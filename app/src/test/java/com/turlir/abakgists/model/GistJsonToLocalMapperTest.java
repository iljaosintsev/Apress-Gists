package com.turlir.abakgists.model;

import com.turlir.abakgists.api.GistJsonToLocalMapper;
import com.turlir.abakgists.api.data.GistJson;
import com.turlir.abakgists.api.data.GistLocal;
import com.turlir.abakgists.api.data.GistOwnerJson;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GistJsonToLocalMapperTest {

    private GistJsonToLocalMapper mapper;

    @Before
    public void setup() {
        mapper = new GistJsonToLocalMapper();
    }

    @Test
    public void callWithOwnerTest() throws Exception {
        GistJson gist = new GistJson("id", "url", "created", "desc", new GistOwnerJson("login", "avatarurl"));
        GistLocal model = mapper.call(gist);
        GistLocal expected = new GistLocal("id", "url", "created", "desc", "login", "avatarurl");
        assertEquals(expected, model);
    }

    @Test
    public void callWithoutOwnerTest() throws Exception {
        GistJson gist = new GistJson("id", "url", "created", "desc");
        GistLocal model = mapper.call(gist);
        GistLocal expected = new GistLocal("id", "url", "created", "desc");
        assertEquals(expected, model);
    }


}