package com.turlir.abakgists.gist

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*

class FirstClassTest {

    private lateinit var mFirst: FirstClass

    @Before
    fun setup() {
        mFirst = FirstClass("begin")

    }

    @Test
    fun testOne() {
        assertEquals("begin-0", mFirst.compute())
        assertEquals("begin-1", mFirst.compute())
        assertEquals("begin-2", mFirst.compute())
    }

    @Test
    fun testTwo() {
        val mutable = mutableListOf(1, 2, 3, 4, 5)
        mutable.add(6)

        val safe = listOf(1, 2, 3, 4, 5, 6)
        assertEquals(safe, mutable)

        val z = ArrayList<String>()
        z.add("123")
        z.add("456")
        z.add("789")
    }

}

