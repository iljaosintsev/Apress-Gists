package com.turlir.abakgists.model;

import org.junit.Test;


public class GistModelTest {

    @Test
    public void testEqualsTrue() {
        GistModel one = new GistModel();
        one.url = "url";
        one.note = null;
        one.description = null;
        one.created = "created";
        one.id = "id";
        one.ownerAvatarUrl = null;
        one.ownerLogin = "login";

        GistModel two = new GistModel();
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
        GistModel one = new GistModel();
        one.url = "url";
        one.note = null;
        one.description = null;
        one.created = "created";
        one.id = "id";
        one.ownerAvatarUrl = null;
        one.ownerLogin = "login";

        GistModel two = new GistModel();
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
