package com.turlir.abakgists.model;

import org.junit.Test;


public class GistTest {

    @Test
    public void testEquals() {
        Gist one = new Gist();
        one.url = "url";
        one.note = null;
        one.description = null;
        one.created = "created";
        one.id = "id";
        one.ownerAvatarUrl = null;
        one.ownerLogin = "login";

        Gist two = new Gist();
        two.url = "url";
        two.note = null;
        two.description = null;
        two.created = "created";
        two.id = "id";
        two.ownerAvatarUrl = null;
        two.ownerLogin = "login";

        boolean equals = two.equals(one);
        org.junit.Assert.assertTrue(equals);
    }

}
