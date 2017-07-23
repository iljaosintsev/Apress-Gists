package com.turlir.abakgists.model;

import com.turlir.abakgists.api.data.GistLocal;

import org.junit.Test;


public class GistModelTest {

    @Test
    public void testEqualsTrue() {
        GistLocal one = new GistLocal();
        one.url = "url";
        one.note = null;
        one.description = null;
        one.created = "created";
        one.id = "id";
        one.ownerAvatarUrl = null;
        one.ownerLogin = "login";

        GistLocal two = new GistLocal();
        two.url = "url";
        two.note = null;
        two.description = null;
        two.created = "created";
        two.id = "id";
        two.ownerAvatarUrl = null;
        two.ownerLogin = "login";

        org.junit.Assert.assertEquals(two, one);
    }

    @Test
    public void testEqualsDifferentNote() {
        GistLocal one = new GistLocal();
        one.url = "url";
        one.note = null;
        one.description = null;
        one.created = "created";
        one.id = "id";
        one.ownerAvatarUrl = null;
        one.ownerLogin = "login";

        GistLocal two = new GistLocal();
        two.url = "url";
        two.note = "note"; // alarm
        two.description = null;
        two.created = "created";
        two.id = "id";
        two.ownerAvatarUrl = null;
        two.ownerLogin = "login";

        org.junit.Assert.assertNotEquals(two, one);
    }


}
