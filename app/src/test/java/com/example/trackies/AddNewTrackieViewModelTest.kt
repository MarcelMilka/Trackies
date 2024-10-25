package com.example.trackies

import com.example.trackies.isSignedIn.addNewTrackie.buisness.ScheduleTimeViewState
import com.example.trackies.isSignedIn.addNewTrackie.buisness.TimeOfIngestion
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.junit.Test

class AddNewTrackieViewModelTest {

    @Test
    fun `variable timeOfIngestion in ScheduleTimeViewState gets properly set from null to TimeOfIngestion`() {

        var scheduleTimeViewState = MutableStateFlow(ScheduleTimeViewState())

        fun setOrUpdateTimeOfIngestion(timeOfIngestion: TimeOfIngestion) {

            scheduleTimeViewState.update {
                it.copy(
                    timeOfIngestion = timeOfIngestion,
                )
            }
        }

        setOrUpdateTimeOfIngestion(
            timeOfIngestion = TimeOfIngestion(
                hour = 10,
                minute = 30
            )
        )

        assertEquals(
            TimeOfIngestion(hour = 10, minute = 30),
            scheduleTimeViewState.value.timeOfIngestion
        )
    }

    @Test
    fun `variable timeOfIngestion in ScheduleTimeViewState gets properly updated`() {

        var scheduleTimeViewState = MutableStateFlow(ScheduleTimeViewState())

        fun setOrUpdateTimeOfIngestion(timeOfIngestion: TimeOfIngestion) {

            scheduleTimeViewState.update {
                it.copy(
                    timeOfIngestion = timeOfIngestion,
                )
            }
        }

        setOrUpdateTimeOfIngestion(
            timeOfIngestion = TimeOfIngestion(
                hour = 10,
                minute = 30
            )
        )

        assertEquals(
            TimeOfIngestion(hour = 10, minute = 30),
            scheduleTimeViewState.value.timeOfIngestion
        )

        setOrUpdateTimeOfIngestion(
            timeOfIngestion = TimeOfIngestion(
                hour = 12,
                minute = 15
            )
        )

        assertEquals(
            TimeOfIngestion(hour = 12, minute = 15),
            scheduleTimeViewState.value.timeOfIngestion
        )
    }

    @Test
    fun `variable timeOfIngestion in ScheduleTimeViewState gets properly deleted`() {

        var scheduleTimeViewState = MutableStateFlow(ScheduleTimeViewState())

        fun setOrUpdateTimeOfIngestion(timeOfIngestion: TimeOfIngestion) {

            scheduleTimeViewState.update {
                it.copy(
                    timeOfIngestion = timeOfIngestion,
                )
            }
        }
        fun deleteTimeOfIngestion() {

            scheduleTimeViewState.update {
                it.copy(
                    timeOfIngestion = null,
                )
            }
        }

        setOrUpdateTimeOfIngestion(
            timeOfIngestion = TimeOfIngestion(
                hour = 10,
                minute = 30
            )
        )

        assertEquals(
            TimeOfIngestion(hour = 10, minute = 30),
            scheduleTimeViewState.value.timeOfIngestion
        )

        deleteTimeOfIngestion()

        assertEquals(
            null,
            scheduleTimeViewState.value.timeOfIngestion
        )
    }
}