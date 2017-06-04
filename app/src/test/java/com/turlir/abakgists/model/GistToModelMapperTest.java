package com.turlir.abakgists.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GistToModelMapperTest {

    private GistToModelMapper mapper;

    @Before
    public void setup() {
        mapper = new GistToModelMapper();
    }

    @Test
    public void callWithOwnerTest() throws Exception {
        Gist gist = new Gist("id", "url", "created", "desc", new GistOwner("login", "avatarurl"));
        GistModel model = mapper.call(gist);
        GistModel expected = new GistModel("id", "url", "created", "desc", "login", "avatarurl");
        assertEquals(expected, model);
    }

    @Test
    public void callWithoutOwnerTest() throws Exception {
        Gist gist = new Gist("id", "url", "created", "desc");
        GistModel model = mapper.call(gist);
        GistModel expected = new GistModel("id", "url", "created", "desc");
        assertEquals(expected, model);
    }


}