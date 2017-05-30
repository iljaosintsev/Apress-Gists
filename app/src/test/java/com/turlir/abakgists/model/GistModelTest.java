package com.turlir.abakgists.model;

import org.junit.Test;


public class GistModelTest {

    @Test
    public void testEquals() {
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

        boolean equals = two.equals(one);
        org.junit.Assert.assertTrue(equals);

        two.note = "note";
        equals = two.equals(one);
        org.junit.Assert.assertFalse(equals);
    }

}
