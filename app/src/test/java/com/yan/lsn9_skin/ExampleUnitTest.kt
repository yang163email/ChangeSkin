package com.yan.lsn9_skin

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun test() {
        Back.init()
    }

    private val mPerson = Person("tom")
    private lateinit var mNewPerson: Person

    @Test
    fun testAddress() {
        clonePerson(mPerson)
    }

    fun clonePerson(person: Person) {
        mNewPerson = person

        mPerson.name = "jerry"
        println(person)
        println(mPerson)
        println(mNewPerson)
    }

    companion object Back {

        lateinit var instance: String

        fun init() {
            val initialized = ::instance.isInitialized
            println(initialized)
        }
    }

    data class Person(var name: String)
}
