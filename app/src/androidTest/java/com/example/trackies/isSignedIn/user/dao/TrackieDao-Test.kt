package com.example.trackies.isSignedIn.user.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.globalConstants.MeasuringUnit
import com.example.trackies.aRoom.db.RoomDatabase
import com.example.trackies.isSignedIn.user.buisness.entities.Trackie
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class) // ensuring all test cases will be executed on emulator.
@SmallTest
class TrackieDaoTest {

    private lateinit var database: RoomDatabase
    private lateinit var trackieDAO: TrackiesDAO

    @Before
    fun setUp() {

        database = Room.inMemoryDatabaseBuilder(
            context = ApplicationProvider.getApplicationContext(), // explanation is in the previous video
            klass = RoomDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        trackieDAO = database.trackiesDAO()
    }

    @After
    fun afterTest() {

        database.close()
    }

    private val wholeWeekTrackie1 = Trackie(
        name = "Whole week trackie 1",
        totalDose = 5,
        measuringUnit = MeasuringUnit.g,
        monday = true,
        tuesday = true,
        wednesday = true,
        thursday = true,
        friday = true,
        saturday = true,
        sunday = true
    )
    private val wholeWeekTrackie2 = Trackie(
        name = "WholeweekTrackie 2",
        totalDose = 100,
        measuringUnit = MeasuringUnit.pcs,
        monday = true,
        tuesday = true,
        wednesday = true,
        thursday = true,
        friday = true,
        saturday = true,
        sunday = true
    )
    private val weekendTrackie = Trackie(
        name = "WeekendTrackie",
        totalDose = 100,
        measuringUnit = MeasuringUnit.pcs,
        monday = false,
        tuesday = false,
        wednesday = false,
        thursday = false,
        friday = true,
        saturday = true,
        sunday = true
    )
    private val mondayTrackie = Trackie(
        name = "Monday Trackie",
        totalDose = 100,
        measuringUnit = MeasuringUnit.ml,
        monday = true,
        tuesday = false,
        wednesday = false,
        thursday = false,
        friday = false,
        saturday = false,
        sunday = false
    )

    @Test
    fun addingTrackieWorksProperly() = runTest {

        // run test works similarly to runBlockingTest - skips any delays saving testing time.

        trackieDAO.addNewTrackie(
            trackie = wholeWeekTrackie1
        )

        val allTrackies = trackieDAO.getAllTrackies()

        assertEquals(
            listOf(wholeWeekTrackie1),
            allTrackies
        )
    }

    @Test
    fun deletingTrackieWorksProperly() = runTest {

        trackieDAO.addNewTrackie(
            trackie = wholeWeekTrackie1
        )

        trackieDAO.addNewTrackie(
            trackie = wholeWeekTrackie2
        )

        trackieDAO.addNewTrackie(
            trackie = weekendTrackie
        )

        trackieDAO.addNewTrackie(
            trackie = mondayTrackie
        )


        trackieDAO.deleteTrackie(
            nameOfTrackie = mondayTrackie.name
        )

        val allTrackies = trackieDAO.getAllTrackies()

        assertEquals(
            listOf(wholeWeekTrackie1, wholeWeekTrackie2, weekendTrackie),
            allTrackies
        )
    }

    @Test
    fun gettingTrackiesForTodayWorksProperly() = runTest {

        trackieDAO.addNewTrackie(
            trackie = wholeWeekTrackie1
        )

        trackieDAO.addNewTrackie(
            trackie = wholeWeekTrackie2
        )

        trackieDAO.addNewTrackie(
            trackie = weekendTrackie
        )

        trackieDAO.addNewTrackie(
            trackie = mondayTrackie
        )


        val trackiesForToday = trackieDAO.getTrackiesForToday(
            currentDayOfWeek = "wednesday"
        )


        assertEquals(
            listOf(wholeWeekTrackie1, wholeWeekTrackie2),
            trackiesForToday
        )
    }

    @Test
    fun trackiesOfTheSameNameGetReplaced() = runTest {

        val theSameTrackieOne = Trackie(
            name = "Trackie",
            totalDose = 1,
            measuringUnit = MeasuringUnit.g,
            monday = true,
            tuesday = true,
            wednesday = true,
            thursday = true,
            friday = true,
            saturday = true,
            sunday = true
        )

        trackieDAO.addNewTrackie(
            trackie = theSameTrackieOne
        )

        val allTrackiesOne = trackieDAO.getAllTrackies()

        assertEquals(
            listOf(theSameTrackieOne),
            allTrackiesOne
        )

        val theSameTrackieTwo = Trackie(
            name = "Trackie",
            totalDose = 1000,
            measuringUnit = MeasuringUnit.g,
            monday = true,
            tuesday = true,
            wednesday = true,
            thursday = true,
            friday = true,
            saturday = true,
            sunday = true
        )

        trackieDAO.addNewTrackie(
            trackie = theSameTrackieTwo
        )

        val allTrackiesTwo = trackieDAO.getAllTrackies()

        assertEquals(
            listOf(theSameTrackieTwo),
            allTrackiesTwo
        )
    }
}