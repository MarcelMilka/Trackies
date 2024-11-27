package com.example.trackies.isSignedIn.user.dao

import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.globalConstants.DaysOfWeek
import com.example.trackies.aRoom.db.RoomDatabase
import com.example.trackies.isSignedIn.user.buisness.entities.Regularity
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class RegularityDaoTest {

    private lateinit var roomDatabase: RoomDatabase
    private lateinit var regularityDao: RegularityDAO

    @Before
    fun setUp() {

        roomDatabase = Room.inMemoryDatabaseBuilder(
            context = ApplicationProvider.getApplicationContext(),
            klass = RoomDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        regularityDao = roomDatabase.regularityDAO()
    }

    @After
    fun afterTest() {

        roomDatabase.close()
    }

    @Test
    fun fetchStatesOfTrackiesForToday_monday_returnsEmptyListByDefault() = runBlocking {

        CountDownLatch(1)

        val expectedData = listOf<Regularity>()

        val actualData = async {

            regularityDao
                .fetchStatesOfTrackiesForToday(
                    currentDayOfWeek = DaysOfWeek.monday
                )
        }.await()

        assertEquals(
            expectedData,
            actualData
        )
    }

    @Test
    fun fetchStatesOfTrackiesForToday_tuesday_returnsEmptyListByDefault() = runBlocking {

        CountDownLatch(1)

        val expectedData = listOf<Regularity>()

        val actualData = async {

            regularityDao
                .fetchStatesOfTrackiesForToday(
                    currentDayOfWeek = DaysOfWeek.tuesday
                )
        }.await()

        assertEquals(
            expectedData,
            actualData
        )
    }

    @Test
    fun fetchStatesOfTrackiesForToday_wednesday_returnsEmptyListByDefault() = runBlocking {

        CountDownLatch(1)

        val expectedData = listOf<Regularity>()

        val actualData = async {

            regularityDao
                .fetchStatesOfTrackiesForToday(
                    currentDayOfWeek = DaysOfWeek.wednesday
                )
        }.await()

        assertEquals(
            expectedData,
            actualData
        )
    }

    @Test
    fun fetchStatesOfTrackiesForToday_thursday_returnsEmptyListByDefault() = runBlocking {

        CountDownLatch(1)

        val expectedData = listOf<Regularity>()

        val actualData = async {

            regularityDao
                .fetchStatesOfTrackiesForToday(
                    currentDayOfWeek = DaysOfWeek.thursday
                )
        }.await()

        assertEquals(
            expectedData,
            actualData
        )
    }

    @Test
    fun fetchStatesOfTrackiesForToday_friday_returnsEmptyListByDefault() = runBlocking {

        CountDownLatch(1)

        val expectedData = listOf<Regularity>()

        val actualData = async {

            regularityDao
                .fetchStatesOfTrackiesForToday(
                    currentDayOfWeek = DaysOfWeek.friday
                )
        }.await()

        assertEquals(
            expectedData,
            actualData
        )
    }

    @Test
    fun fetchStatesOfTrackiesForToday_saturday_returnsEmptyListByDefault() = runBlocking {

        CountDownLatch(1)

        val expectedData = listOf<Regularity>()

        val actualData = async {

            regularityDao
                .fetchStatesOfTrackiesForToday(
                    currentDayOfWeek = DaysOfWeek.saturday
                )
        }.await()

        assertEquals(
            expectedData,
            actualData
        )
    }

    @Test
    fun fetchStatesOfTrackiesForToday_sunday_returnsEmptyListByDefault() = runBlocking {

        CountDownLatch(1)

        val expectedData = listOf<Regularity>()

        val actualData = async {

            regularityDao
                .fetchStatesOfTrackiesForToday(
                    currentDayOfWeek = DaysOfWeek.sunday
                )
        }.await()

        assertEquals(
            expectedData,
            actualData
        )
    }

    @Test
    fun fetchStatesOfTrackiesForToday_returnsAppropriateObjects() = runBlocking {

        CountDownLatch(1)


        val toReturn1 = Regularity(
            name = "regularity1",
            dayOfWeek = "monday",
            ingested = false
        )

        val toReturn2 = Regularity(
            name = "regularity2",
            dayOfWeek = "monday",
            ingested = false
        )

        val toReturn3 = Regularity(
            name = "regularity3",
            dayOfWeek = "monday",
            ingested = false
        )

        val regularity1 = Regularity(
            name = "111",
            dayOfWeek = "tuesday",
            ingested = false
        )

        val regularity2 = Regularity(
            name = "222",
            dayOfWeek = "wednesday",
            ingested = false
        )

        val regularity3 = Regularity(
            name = "333",
            dayOfWeek = "wednesday",
            ingested = false
        )

        val regularity4 = Regularity(
            name = "444",
            dayOfWeek = "wednesday",
            ingested = false
        )

        val regularity5 = Regularity(
            name = "555",
            dayOfWeek = "wednesday",
            ingested = false
        )

        val regularity6 = Regularity(
            name = "666",
            dayOfWeek = "wednesday",
            ingested = false
        )

        val regularity7 = Regularity(
            name = "777",
            dayOfWeek = "wednesday",
            ingested = false
        )

        val expectedData = listOf<Regularity>(toReturn1, toReturn2, toReturn3)

        val actualData = async {

            regularityDao.addTrackieToRegularity(regularity =  toReturn1)
            regularityDao.addTrackieToRegularity(regularity =  toReturn2)
            regularityDao.addTrackieToRegularity(regularity =  toReturn3)
            regularityDao.addTrackieToRegularity(regularity =  regularity1)
            regularityDao.addTrackieToRegularity(regularity =  regularity2)
            regularityDao.addTrackieToRegularity(regularity =  regularity3)
            regularityDao.addTrackieToRegularity(regularity =  regularity4)
            regularityDao.addTrackieToRegularity(regularity =  regularity5)
            regularityDao.addTrackieToRegularity(regularity =  regularity6)
            regularityDao.addTrackieToRegularity(regularity =  regularity7)

            regularityDao
                .fetchStatesOfTrackiesForToday(
                    currentDayOfWeek = DaysOfWeek.monday
                )
        }.await()

        Log.d("Halla!", "$actualData")

        assertEquals(
            expectedData,
            actualData
        )
    }

    @Test
    fun fetchWeeklyRegularity_returnsEmptyListByDefault() = runBlocking {

        CountDownLatch(1)

        val expected = listOf<Regularity>()

        val actual = async {

            regularityDao
                .fetchWeeklyRegularity()
        }.await()

        assertEquals(
            expected,
            actual
        )
    }

    @Test
    fun addTrackieToRegularity_trackieGetsAddedProperly() = runBlocking {

        CountDownLatch(1)

        val regularity1 = Regularity(
            name = "regularity1",
            dayOfWeek = "monday",
            ingested = false
        )

        val expected = listOf<Regularity>(regularity1)

        val actual = async {

            regularityDao
                .addTrackieToRegularity(regularity1)

            regularityDao
                .fetchWeeklyRegularity()
        }.await()

        assertEquals(
            expected,
            actual
        )
    }

    @Test
    fun addTrackieToRegularity_trackieGetsReplacedAvoidingConflicts() = runBlocking {

        CountDownLatch(1)

        val regularity1 = Regularity(
            name = "regularity1",
            dayOfWeek = "monday",
            ingested = false
        )

        val regularity2 = Regularity(
            name = "regularity1",
            dayOfWeek = "monday",
            ingested = true
        )

        val expected = listOf<Regularity>(regularity2)

        val actual = async {

            regularityDao
                .addTrackieToRegularity(regularity1)

            regularityDao
                .addTrackieToRegularity(regularity2)

            regularityDao
                .fetchWeeklyRegularity()
        }.await()

        assertEquals(
            expected,
            actual
        )
    }

    @Test
    fun deleteTrackieFromRegularity_deletesObjectAppropriately() = runBlocking {

        CountDownLatch(1)

        val regularity1 = Regularity(
            name = "regularity1",
            dayOfWeek = "monday",
            ingested = false
        )

        val regularity2 = Regularity(
            name = "regularity2",
            dayOfWeek = "monday",
            ingested = false
        )

        val expectedData = listOf<Regularity>(regularity2)

        val actualData = async {

            regularityDao.addTrackieToRegularity(regularity = regularity1)
            regularityDao.addTrackieToRegularity(regularity = regularity2)

            regularityDao.deleteTrackieFromRegularity(nameOfTrackie = "regularity1")

            regularityDao.fetchWeeklyRegularity()
        }.await()

        Log.d("Halla!", "$actualData")

        assertEquals(
            expectedData,
            actualData
        )
    }

    @Test
    fun markTrackieAsIngested_worksProperly() = runBlocking {

        CountDownLatch(1)

        val regularityBefore = Regularity(
            name = "regularity1",
            dayOfWeek = "monday",
            ingested = false
        )

        val regularityAfter = Regularity(
            name = "regularity1",
            dayOfWeek = "monday",
            ingested = false
        )

        val expectedData = listOf<Regularity>(regularityAfter)

        val actualData = async {

            regularityDao.addTrackieToRegularity(regularity = regularityBefore)

            regularityDao.markTrackieAsIngested(rowToUpdate = regularityBefore)

            regularityDao.fetchWeeklyRegularity()
        }.await()

        assertEquals(
            expectedData,
            actualData
        )
    }

    @Test
    fun resetRegularity_worksProperly() = runBlocking {

        CountDownLatch(1)

//      Regularity variables for Monday
        val regularityMonday1 = Regularity(
            name = "lorem ipsum dolor",
            dayOfWeek = "monday",
            ingested = true
        )
        val regularityMonday1AfterReset = Regularity(
            name = "lorem ipsum dolor",
            dayOfWeek = "monday",
            ingested = false
        )

        val regularityMonday2 = Regularity(
            name = "ques es una",
            dayOfWeek = "monday",
            ingested = false
        )
        val regularityMonday2AfterReset = Regularity(
            name = "ques es una",
            dayOfWeek = "monday",
            ingested = false
        )

        val regularityMonday3 = Regularity(
            name = "un pollo con pescado",
            dayOfWeek = "monday",
            ingested = true
        )
        val regularityMonday3AfterReset = Regularity(
            name = "un pollo con pescado",
            dayOfWeek = "monday",
            ingested = false
        )

//      Regularity variables for Tuesday
        val regularityTuesday1 = Regularity(
            name = "sed ut perspiciatis",
            dayOfWeek = "tuesday",
            ingested = true
        )
        val regularityTuesday1AfterReset = Regularity(
            name = "sed ut perspiciatis",
            dayOfWeek = "tuesday",
            ingested = false
        )

        val regularityTuesday2 = Regularity(
            name = "doloremque laudantium",
            dayOfWeek = "tuesday",
            ingested = false
        )
        val regularityTuesday2AfterReset = Regularity(
            name = "doloremque laudantium",
            dayOfWeek = "tuesday",
            ingested = false
        )

        val regularityTuesday3 = Regularity(
            name = "totam rem aperiam",
            dayOfWeek = "tuesday",
            ingested = true
        )
        val regularityTuesday3AfterReset = Regularity(
            name = "totam rem aperiam",
            dayOfWeek = "tuesday",
            ingested = false
        )

        val expectedData = listOf<Regularity>(
            regularityMonday1AfterReset,
            regularityMonday2AfterReset,
            regularityMonday3AfterReset,
            regularityTuesday1AfterReset,
            regularityTuesday2AfterReset,
            regularityTuesday3AfterReset,
        )

        val actualData = async {

            regularityDao.addTrackieToRegularity(regularityMonday1)
            regularityDao.addTrackieToRegularity(regularityMonday2)
            regularityDao.addTrackieToRegularity(regularityMonday3)

            regularityDao.addTrackieToRegularity(regularityTuesday1)
            regularityDao.addTrackieToRegularity(regularityTuesday2)
            regularityDao.addTrackieToRegularity(regularityTuesday3)

            regularityDao.resetRegularity("monday")
            regularityDao.resetRegularity("tuesday")

            regularityDao.fetchWeeklyRegularity()
        }.await()

        assertEquals(
            expectedData,
            actualData
        )
    }

    @Test
    fun deleteUsersRegularity_worksProperly() = runBlocking {

        CountDownLatch(1)

//      Regularity variables for Monday
        val regularityMonday1 = Regularity(
            name = "lorem ipsum dolor",
            dayOfWeek = "monday",
            ingested = true
        )

        val regularityMonday2 = Regularity(
            name = "ques es una",
            dayOfWeek = "monday",
            ingested = false
        )

        val regularityMonday3 = Regularity(
            name = "un pollo con pescado",
            dayOfWeek = "monday",
            ingested = true
        )

//      Regularity variables for Tuesday
        val regularityTuesday1 = Regularity(
            name = "sed ut perspiciatis",
            dayOfWeek = "tuesday",
            ingested = true
        )

        val regularityTuesday2 = Regularity(
            name = "doloremque laudantium",
            dayOfWeek = "tuesday",
            ingested = false
        )

        val regularityTuesday3 = Regularity(
            name = "totam rem aperiam",
            dayOfWeek = "tuesday",
            ingested = true
        )

        val expectedData = listOf<Regularity>()

        val actualData = async {

            regularityDao.addTrackieToRegularity(regularityMonday1)
            regularityDao.addTrackieToRegularity(regularityMonday2)
            regularityDao.addTrackieToRegularity(regularityMonday3)

            regularityDao.addTrackieToRegularity(regularityTuesday1)
            regularityDao.addTrackieToRegularity(regularityTuesday2)
            regularityDao.addTrackieToRegularity(regularityTuesday3)

            regularityDao.deleteUsersRegularity()

            regularityDao.fetchWeeklyRegularity()
        }.await()

        assertEquals(
            expectedData,
            actualData
        )
    }
}