package com.turlir.abakgists.templater;

import android.view.ViewGroup;

import com.turlir.abakgists.templater.base.Group;
import com.turlir.abakgists.templater.base.Grouper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MixerTest {

    private Group firstGroup, secondGroup;

    private Iterator<ViewGroup> iter;

    @Mock
    private ViewGroup root;

    @Mock
    private Grouper hack;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        assertNotNull(root);
        assertNotNull(hack);

        firstGroup = new Group(0, 1);
        firstGroup.close(3);
        firstGroup.shift(); // 1 - 3, 3 элемента

        secondGroup = new Group(1, 5);
        secondGroup.close(6);
        secondGroup.shift(); // 5 - 6, 2 элемента

        HashMap<Integer, Group> groups = new LinkedHashMap<>();
        groups.put(0, firstGroup);
        groups.put(4, secondGroup);

        Mixer mixer = new Mixer(groups);

        iter = mixer.iterator(root, hack);
    }

    @Test
    public void simpleTest() {
        ViewGroup now;

        final ViewGroup mockOne = Mockito.mock(ViewGroup.class);
        Mockito.when(hack.changeRoot(firstGroup.number)).thenReturn(mockOne);
        now = iter.next();
        assertTrue(mockOne == now);

        now = iter.next();
        assertTrue(mockOne == now);

        now = iter.next();
        assertTrue(mockOne == now);

        //

        now = iter.next();
        assertTrue(root == now);

        //

        final ViewGroup mockTwo = Mockito.mock(ViewGroup.class);
        Mockito.when(hack.changeRoot(secondGroup.number)).thenReturn(mockTwo);
        now = iter.next();
        assertTrue(mockTwo == now);

        now = iter.next();
        assertTrue(mockTwo == now);
    }

}