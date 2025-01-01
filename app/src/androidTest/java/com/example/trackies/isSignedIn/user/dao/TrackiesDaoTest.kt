package com.example.trackies.isSignedIn.user.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.globalConstants.DaysOfWeek
import com.example.globalConstants.MeasuringUnit
import com.example.trackies.aRoom.db.RoomDatabase
import com.example.trackies.isSignedIn.user.buisness.entities.Trackie
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class TrackiesDaoTest {

    private lateinit var database: RoomDatabase
    private lateinit var trackiesDAO: TrackiesDAO

    @Before
    fun beforeTest() {

        database = Room.inMemoryDatabaseBuilder(
            context = ApplicationProvider.getApplicationContext(), // explanation is in the previous video
            klass = RoomDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        trackiesDAO = database.trackiesDAO()
    }

    @After
    fun afterTest() {

        database.close()
    }

    @Test
    fun getTrackiesForToday_noTrackies_returnsEmptyList() = runTest {

//      Assertion:
        val expected = listOf<Trackie>()
        val actual = trackiesDAO.getTrackiesForToday(
            currentDayOfWeek = DaysOfWeek.monday
        )

        assertEquals(expected, actual)
    }

    @Test
    fun getTrackiesForToday_trackiesExist_returnsListWithTrackie() = runTest {

//      Preparation:
        trackiesDAO.addNewTrackie(TrackieDaoTestHelpingObject.wholeWeekTrackie1)
        trackiesDAO.addNewTrackie(TrackieDaoTestHelpingObject.wholeWeekTrackie2)
        trackiesDAO.addNewTrackie(TrackieDaoTestHelpingObject.weekendTrackie)

//      Assertions:
        val trackiesForToday = trackiesDAO.getTrackiesForToday(DaysOfWeek.monday)
        assertTrue(trackiesForToday!!.contains(TrackieDaoTestHelpingObject.wholeWeekTrackie1))
        assertTrue(trackiesForToday.contains(TrackieDaoTestHelpingObject.wholeWeekTrackie2))
    }


    @Test
    fun getNamesOfAllTrackies_noTrackies_returnsEmptyList() = runTest {

//      Assertion:
        val expected = listOf<String>()
        val actual = trackiesDAO.getNamesOfAllTrackies()

        assertEquals(expected, actual)
    }

    @Test
    fun getNamesOfAllTrackies_trackiesExist_returnsListWithStringValues() = runTest {

//      Preparation:
        trackiesDAO.addNewTrackie(TrackieDaoTestHelpingObject.wholeWeekTrackie1)
        trackiesDAO.addNewTrackie(TrackieDaoTestHelpingObject.wholeWeekTrackie2)
        trackiesDAO.addNewTrackie(TrackieDaoTestHelpingObject.weekendTrackie)

//      Assertion:
        val listOfStringValues = trackiesDAO.getNamesOfAllTrackies()
        assertTrue(listOfStringValues.contains("wholeWeekTrackie1"))
        assertTrue(listOfStringValues.contains("wholeWeekTrackie2"))
        assertTrue(listOfStringValues.contains("weekendTrackie"))
    }


    @Test
    fun addNewTrackie_trackieOfUniqueNameIsAddedProperly() = runTest {

//      Preparation:
        trackiesDAO.addNewTrackie(
            trackie = TrackieDaoTestHelpingObject.wholeWeekTrackie1
        )

//      Assertion:
        val expected = listOf(TrackieDaoTestHelpingObject.wholeWeekTrackie1)
        val actual = trackiesDAO.getAllTrackies()

        assertEquals(expected, actual)
    }

    @Test
    fun addNewTrackie_trackieOfAlreadyExistingNameIsReplacedWithThePreviousTrackie() = runTest {

//      Preparation 1:
        trackiesDAO.addNewTrackie(
            trackie = TrackieDaoTestHelpingObject.wholeWeekTrackie1ToReplace
        )

//      Assertion 1:
        val expected1 = listOf(TrackieDaoTestHelpingObject.wholeWeekTrackie1ToReplace)
        val actual1 = trackiesDAO.getAllTrackies()

        assertEquals(expected1, actual1)

//      Preparation 2:
        trackiesDAO.addNewTrackie(
            trackie = TrackieDaoTestHelpingObject.wholeWeekTrackie1
        )

//      Assertion 2:
        val expected2 = listOf(TrackieDaoTestHelpingObject.wholeWeekTrackie1)
        val actual2 = trackiesDAO.getAllTrackies()

        assertEquals(expected2, actual2)
    }


    @Test
    fun deleteTrackie_trackieIsDeletedProperly_noTrackiesLeft() = runTest {

//      Preparation:
        trackiesDAO.addNewTrackie(TrackieDaoTestHelpingObject.wholeWeekTrackie1)
        trackiesDAO.deleteTrackie("wholeWeekTrackie1")

//      Assertion:
        assertTrue(trackiesDAO.getAllTrackies().isEmpty())
    }

    @Test
    fun deleteTrackie_trackieIsDeletedProperly_oneTrackieLeft() = runTest {

//      Preparation:
        trackiesDAO.addNewTrackie(TrackieDaoTestHelpingObject.wholeWeekTrackie1)
        trackiesDAO.addNewTrackie(TrackieDaoTestHelpingObject.wholeWeekTrackie2)

        trackiesDAO.deleteTrackie("wholeWeekTrackie1")

//      Assertion:
        val expectedListOfStringValues = listOf(TrackieDaoTestHelpingObject.wholeWeekTrackie2)
        val actualListOfStringValues = trackiesDAO.getAllTrackies()
        assertEquals(expectedListOfStringValues, actualListOfStringValues)
    }

    @Test
    fun getAllTrackies_thereAreNotAnyTrackies_emptyListIsReturned() = runTest {

//      Assertion:
        assertTrue(trackiesDAO.getAllTrackies().isEmpty())
    }

    @Test
    fun getAllTrackies_listOfTrackiesIsReturned() = runTest {

//      Preparation:
        trackiesDAO.addNewTrackie(TrackieDaoTestHelpingObject.wholeWeekTrackie1)
        trackiesDAO.addNewTrackie(TrackieDaoTestHelpingObject.wholeWeekTrackie2)
        trackiesDAO.addNewTrackie(TrackieDaoTestHelpingObject.mondayTrackie)

//      Assertions:
        val actualListOfTrackies = trackiesDAO.getAllTrackies()
        assertTrue(actualListOfTrackies.contains(TrackieDaoTestHelpingObject.wholeWeekTrackie1))
        assertTrue(actualListOfTrackies.contains(TrackieDaoTestHelpingObject.wholeWeekTrackie2))
        assertTrue(actualListOfTrackies.contains(TrackieDaoTestHelpingObject.mondayTrackie))
    }
}

private object TrackieDaoTestHelpingObject {

    val wholeWeekTrackie1 = Trackie(
        name = "wholeWeekTrackie1",
        totalDose = 3000,
        measuringUnit = MeasuringUnit.ml,
        monday = true,
        tuesday = true,
        wednesday = true,
        thursday = true,
        friday = true,
        saturday = true,
        sunday = true
    )

    val wholeWeekTrackie1ToReplace = Trackie(
        name = "wholeWeekTrackie1",
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

    val wholeWeekTrackie2 = Trackie(
        name = "wholeWeekTrackie2",
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

    val weekendTrackie = Trackie(
        name = "weekendTrackie",
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

    val mondayTrackie = Trackie(
        name = "mondayTrackie",
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
}