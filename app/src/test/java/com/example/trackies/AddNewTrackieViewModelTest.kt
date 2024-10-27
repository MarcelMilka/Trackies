package com.example.trackies

import com.example.trackies.isSignedIn.addNewTrackie.buisness.ScheduleTimeViewState
import com.example.trackies.isSignedIn.addNewTrackie.buisness.IngestionTime
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.junit.Test

class AddNewTrackieViewModelTest {

    @Test
    fun `variable timeOfIngestion in ScheduleTimeViewState gets properly set from null to TimeOfIngestion`() {

        var scheduleTimeViewState = MutableStateFlow(ScheduleTimeViewState())

        fun setOrUpdateTimeOfIngestion(timeOfIngestion: IngestionTime) {

            scheduleTimeViewState.update {
                it.copy(
                    ingestionTime = timeOfIngestion,
                )
            }
        }

        setOrUpdateTimeOfIngestion(
            timeOfIngestion = IngestionTime(
                hour = 10,
                minute = 30
            )
        )

        assertEquals(
            IngestionTime(hour = 10, minute = 30),
            scheduleTimeViewState.value.ingestionTime
        )
    }

    @Test
    fun `variable timeOfIngestion in ScheduleTimeViewState gets properly updated`() {

        var scheduleTimeViewState = MutableStateFlow(ScheduleTimeViewState())

        fun setOrUpdateTimeOfIngestion(timeOfIngestion: IngestionTime) {

            scheduleTimeViewState.update {
                it.copy(
                    ingestionTime = timeOfIngestion,
                )
            }
        }

        setOrUpdateTimeOfIngestion(
            timeOfIngestion = IngestionTime(
                hour = 10,
                minute = 30
            )
        )

        assertEquals(
            IngestionTime(hour = 10, minute = 30),
            scheduleTimeViewState.value.ingestionTime
        )

        setOrUpdateTimeOfIngestion(
            timeOfIngestion = IngestionTime(
                hour = 12,
                minute = 15
            )
        )

        assertEquals(
            IngestionTime(hour = 12, minute = 15),
            scheduleTimeViewState.value.ingestionTime
        )
    }

    @Test
    fun `variable timeOfIngestion in ScheduleTimeViewState gets properly deleted`() {

        var scheduleTimeViewState = MutableStateFlow(ScheduleTimeViewState())

        fun setOrUpdateTimeOfIngestion(timeOfIngestion: IngestionTime) {

            scheduleTimeViewState.update {
                it.copy(
                    ingestionTime = timeOfIngestion,
                )
            }
        }
        fun deleteTimeOfIngestion() {

            scheduleTimeViewState.update {
                it.copy(
                    ingestionTime = null,
                )
            }
        }

        setOrUpdateTimeOfIngestion(
            timeOfIngestion = IngestionTime(
                hour = 10,
                minute = 30
            )
        )

        assertEquals(
            IngestionTime(hour = 10, minute = 30),
            scheduleTimeViewState.value.ingestionTime
        )

        deleteTimeOfIngestion()

        assertEquals(
            null,
            scheduleTimeViewState.value.ingestionTime
        )
    }
}