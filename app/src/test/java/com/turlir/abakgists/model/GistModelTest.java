package com.turlir.abakgists.model;

import com.turlir.abakgists.Data;
import com.turlir.abakgists.api.data.GistLocal;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;


public class GistModelTest {

    @Test
    public void testEqualsTrue() {
        GistLocal one = new GistLocal();
        one.url = "url";
        one.note = "note";
        one.description = "desc";
        one.created = "created";
        one.id = "id";
        one.ownerAvatarUrl = null;
        one.ownerLogin = "login";

        GistLocal two = new GistLocal();
        two.url = "url";
        two.note = "note";
        two.description = "desc";
        two.created = "created";
        two.id = "id";
        two.ownerAvatarUrl = null;
        two.ownerLogin = "login";

        assertEquals(two, one);
        assertEquals(one, two);
    }

    @Test
    public void testEqualsFalse() {
        GistLocal one = new GistLocal();
        one.url = "url";
        one.note = "note";
        one.description = "desc";
        one.created = "created";
        one.id = "id";
        one.ownerAvatarUrl = null;
        one.ownerLogin = "turlir";

        GistLocal two = new GistLocal();
        two.url = "url";
        two.note = "note";
        two.description = "desc";
        two.created = "created";
        two.id = "id";
        two.ownerAvatarUrl = null;
        two.ownerLogin = "login";

        assertNotEquals(two, one);
        assertNotEquals(one, two);
    }

    @Test
    public void notChangeBoth() {
        GistModel a = new GistModel(Data.LOCAL_STUB_TRUE, "", "");
        GistModel b = new GistModel(Data.LOCAL_STUB_TRUE, "", "");
        assertTrue(a.equals(b));
        assertTrue(b.equals(a));
    }

    @Test
    public void changeDesc() {
        GistModel a = new GistModel(Data.LOCAL_STUB_TRUE, "", "");
        GistModel b = new GistModel(Data.LOCAL_STUB_TRUE, "2", "");
        assertFalse(a.equals(b));
        assertFalse(b.equals(a));
    }

    @Test
    public void changeNote() {
        GistModel a = new GistModel(Data.LOCAL_STUB_TRUE, "", "");
        GistModel b = new GistModel(Data.LOCAL_STUB_TRUE, "", "2");
        assertFalse(a.equals(b));
        assertFalse(b.equals(a));
    }

    @Test
    public void changeBoth() {
        GistModel a = new GistModel(Data.LOCAL_STUB_TRUE, "1", "1");
        GistModel b = new GistModel(Data.LOCAL_STUB_TRUE, "2", "2");
        assertFalse(a.equals(b));
        assertFalse(b.equals(a));
    }
}
