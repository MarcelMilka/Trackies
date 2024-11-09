package com.example.trackies.isSignedIn.user.data

import android.util.Log
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteCompat.Api16Impl.deleteDatabase
import androidx.test.core.app.ApplicationProvider
import com.example.globalConstants.DaysOfWeek
import com.example.trackies.isSignedIn.user.buisness.entities.License
import com.example.trackies.isSignedIn.user.buisness.entities.Trackie
import com.example.trackies.isSignedIn.user.di.UserRepositoryModule
import com.example.trackies.isSignedIn.user.roomDatabase.RoomDatabase
import com.example.trackies.isSignedIn.xTrackie.buisness.TrackieModel
import com.example.trackies.isSignedIn.xTrackie.buisness.convertTrackieModelToTrackieEntity
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Singleton


@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@UninstallModules(UserRepositoryModule::class)
class RoomUserRepositoryTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var roomUserRepository: UserRepository

    @Inject
    lateinit var roomDatabase: RoomDatabase

    @Before
    fun setUp() {

        hiltRule.inject()

        roomDatabase.clearAllTables()
    }

    @After
    fun closeDB() = runBlocking{

        roomDatabase.testHelperDAO().deleteLicenseTable()
        roomDatabase.testHelperDAO().deleteTrackiesTable()
        roomDatabase.testHelperDAO().deleteRegularityTable()

        roomDatabase.clearAllTables()
        roomDatabase.close()
    }

    private val license = License(
        first = 1,
        isSignedIn = true,
        totalAmountOfTrackies = 0
    )

    private val firstTrackieModel = TrackieModel(
        name = "FirstTrackie",
        totalDose = 10,
        measuringUnit = "ml",
        repeatOn = listOf(DaysOfWeek.monday, DaysOfWeek.tuesday, DaysOfWeek.wednesday, DaysOfWeek.thursday, DaysOfWeek.friday, DaysOfWeek.saturday, DaysOfWeek.sunday,),
        ingestionTime = null,
    )

    private val secondTrackieModel = TrackieModel(
        name = "SecondTrackie",
        totalDose = 330,
        measuringUnit = "g",
        repeatOn = listOf(DaysOfWeek.monday, DaysOfWeek.tuesday, DaysOfWeek.wednesday, DaysOfWeek.thursday, DaysOfWeek.friday, DaysOfWeek.saturday, DaysOfWeek.sunday,),
        ingestionTime = mapOf("10:00" to 100),
    )

    @Test
    fun repositoryProperlyAddsLicenseOfNewUsers() = runBlocking {

//      1: Checking if there's any License in the table 'License'
        this.launch {
            val expectedLicense1 = null
            val actualLicense1 = roomDatabase.licenseDAO().getLicense()
            assertEquals(expectedLicense1, actualLicense1)
        }.join()

//      2: Launching the method responsible for adding license for a new user
//         and checking if the license gets added.
        roomUserRepository.isFirstTimeInTheApp {  }

        delay(2000)

        val expectedLicense2 = License(first = 1, isSignedIn = true, totalAmountOfTrackies = 0)
        val actualLicense2 = roomDatabase.licenseDAO().getLicense()
        assertEquals(expectedLicense2, actualLicense2)
    }

    @Test
    fun trackieGetsAddedProperly() = runBlocking {

        this.launch {
            roomUserRepository.isFirstTimeInTheApp {  }
        }.join()

        this.launch {

            roomUserRepository.addNewTrackie(
                trackieModel = firstTrackieModel,
                onFailure = {}
            )
        }.join()

        val expectedListOfTrackies = listOf<Trackie>(firstTrackieModel.convertTrackieModelToTrackieEntity())

        val actualListOfTrackies = this.async {
            roomDatabase.trackiesDAO().getAllTrackies()
        }

        assertEquals(expectedListOfTrackies, actualListOfTrackies.await())
    }


//  Tests of extension functions used in RoomUserRepository
    @Test
    fun trackieModelIsProperlyConvertedIntoRoomTrackieEntity() = runTest {

        val actualTrackieEntity = firstTrackieModel.convertTrackieModelToTrackieEntity()
        val expectedTrackieEntity = Trackie(
            name = "First trackie",
            totalDose = 10,
            measuringUnit = "ml",
            monday = true,
            tuesday = true,
            wednesday = true,
            thursday = true,
            friday = true,
            saturday = true,
            sunday = true
        )

        assertEquals(
            expectedTrackieEntity,
            actualTrackieEntity
        )
    }

    @Test
    fun trackieModelWithScheduledTimeIsProperlyConvertedIntoRoomTrackieEntity() = runTest {

        val actualTrackieEntity = secondTrackieModel.convertTrackieModelToTrackieEntity()
        val expectedTrackieEntity = Trackie(
            name = "SecondTrackie",
            totalDose = 330,
            measuringUnit = "g",
            monday = true,
            tuesday = true,
            wednesday = true,
            thursday = true,
            friday = true,
            saturday = true,
            sunday = true
        )

        assertEquals(
            expectedTrackieEntity,
            actualTrackieEntity
        )
    }

    @Module
    @InstallIn(SingletonComponent::class)
    class UserRepositoryModule {

        @Provides
        @Singleton
        fun provideRoomDatabase(): RoomDatabase =
            Room.inMemoryDatabaseBuilder(
                context = ApplicationProvider.getApplicationContext(),
                klass = RoomDatabase::class.java
            )
                .allowMainThreadQueries()
                .build()

        @Provides
        @Reusable
        fun provideUserRepository(roomDatabase: RoomDatabase): UserRepository =
            RoomUserRepository(roomDatabase = roomDatabase)
    }
}