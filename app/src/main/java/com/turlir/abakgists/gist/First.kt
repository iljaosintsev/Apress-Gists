package com.turlir.abakgists.gist

class FirstClass(private val str: String) {

    private var count: Int = 0

    fun compute():String {
        return "$str-${count++}"
    }

}