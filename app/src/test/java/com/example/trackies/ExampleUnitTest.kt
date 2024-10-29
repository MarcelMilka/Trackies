package com.example.trackies

import org.junit.Test

import org.junit.Assert.*

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
    fun x() {

        val correctOrder = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

        val daysOfWeek = mutableListOf("Wednesday", "Monday", "Sunday", "Thursday", "Saturday")

        // Sorting based on the correct order
        daysOfWeek.sortBy { correctOrder.indexOf(it) }

        assertEquals(
            mutableListOf("Moday", "Wednesday", "Thursday", "Saturday", "Sunday"),
            daysOfWeek
        )

    }
}