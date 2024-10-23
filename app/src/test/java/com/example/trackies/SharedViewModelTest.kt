package com.example.trackies

import android.util.Log
import com.example.trackies.isSignedIn.constantValues.CurrentTime
import com.example.trackies.isSignedIn.constantValues.DaysOfWeek
import com.example.trackies.isSignedIn.trackie.TrackieViewState
import junit.framework.TestCase.assertEquals
import org.junit.Test

class SharedViewModelTest {

    @Test
    fun `states of trackies for Today is properly updated when a trackie gets marked as ingested`() {

        val statesOfTrackiesForToday = mapOf<String, Boolean>(
            "a" to false,
            "b" to false,
            "c" to false,
            "d" to false,
            "e" to false,
            "f" to false,
        )

        val trackieViewState = TrackieViewState(
            name = "c",
            totalDose = 100,
            measuringUnit = "g",
            repeatOn = listOf(DaysOfWeek.monday),
            ingestionTime = null
        )

        fun updateStatesOfTrackiesForToday(): Map<String, Boolean> {

            val updatedMap = statesOfTrackiesForToday.toMutableMap()

            updatedMap[trackieViewState.name] = true

            return updatedMap
        }

        val actualValue = updateStatesOfTrackiesForToday()

        val expectedUpdatedStatesOfTrackiesForToday = mapOf<String, Boolean>(
            "a" to false,
            "b" to false,
            "c" to true,
            "d" to false,
            "e" to false,
            "f" to false,
        )

        assertEquals(
            expectedUpdatedStatesOfTrackiesForToday,
            actualValue
        )
    }

    @Test
    fun `weekly regularity is properly updated when a trackie gets marked as ingested`() {

        val weeklyRegularity = mutableMapOf<String, MutableMap<Int, Int>>(
            DaysOfWeek.monday to mutableMapOf(1 to 0),
            DaysOfWeek.tuesday to mutableMapOf(1 to 0),
            DaysOfWeek.wednesday to mutableMapOf(1 to 0),
            DaysOfWeek.thursday to mutableMapOf(1 to 0),
            DaysOfWeek.friday to mutableMapOf(1 to 0),
            DaysOfWeek.saturday to mutableMapOf(1 to 0),
            DaysOfWeek.sunday to mutableMapOf(1 to 0)
        )

        fun updateWeeklyRegularity(): Map<String, Map<Int, Int>> {

            val currentDayOfWeek = CurrentTime.getCurrentDayOfWeek()

            var updatedWeeklyRegularity = mutableMapOf<String, MutableMap<Int, Int>>()

            weeklyRegularity.forEach {

                if (it.key == currentDayOfWeek) {

                    val total = it.value.keys.toIntArray()[0]
                    var ingested = it.value.values.toIntArray()[0]
                    ingested = ingested + 1

                    val value = mutableMapOf(total to ingested)

                    updatedWeeklyRegularity.put(
                        key = it.key,
                        value = value
                    )
                }

                else {

                    updatedWeeklyRegularity.put(
                        key = it.key,
                        value = it.value
                    )
                }
            }

            return updatedWeeklyRegularity
        }

        val actualValue = updateWeeklyRegularity()

        val expectedUpdatedWeeklyRegularity = mutableMapOf<String, MutableMap<Int, Int>>(
            DaysOfWeek.monday to mutableMapOf(1 to 0),
            DaysOfWeek.tuesday to mutableMapOf(1 to 0),
            DaysOfWeek.wednesday to mutableMapOf(1 to 1),
            DaysOfWeek.thursday to mutableMapOf(1 to 0),
            DaysOfWeek.friday to mutableMapOf(1 to 0),
            DaysOfWeek.saturday to mutableMapOf(1 to 0),
            DaysOfWeek.sunday to mutableMapOf(1 to 0)
        )

        assertEquals(
            expectedUpdatedWeeklyRegularity,
            actualValue
        )
    }
}