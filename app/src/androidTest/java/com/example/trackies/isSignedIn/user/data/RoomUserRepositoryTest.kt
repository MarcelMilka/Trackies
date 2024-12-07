package com.example.trackies.isSignedIn.user.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.globalConstants.DaysOfWeek
import com.example.trackies.aRoom.db.RoomDatabase
import com.example.trackies.aRoom.di.RoomDatabaseProvider
import com.example.trackies.isSignedIn.user.buisness.LicenseModel
import com.example.trackies.isSignedIn.user.buisness.entities.License
import com.example.trackies.isSignedIn.user.buisness.entities.Regularity
import com.example.trackies.isSignedIn.user.di.UserRepositoryModule
import com.example.trackies.isSignedIn.xTrackie.buisness.TrackieModel
import com.example.trackies.isSignedIn.xTrackie.buisness.convertTrackieModelToTrackieEntity
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@UninstallModules(UserRepositoryModule::class, RoomDatabaseProvider::class)
class RoomUserRepositoryTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var roomDatabase: RoomDatabase

    @Inject
    lateinit var roomUserRepository: UserRepository

    @Before
    fun beforeTest() {

        hiltRule.inject()
    }

    @After
    fun afterTest() {

        roomDatabase.close()
    }

//////////////////////////////////////////////////////////////////////////////////////////

    @Test
    fun isFirstTimeInTheApp_alwaysReturnsTrue() = runBlocking {

        val latch = CountDownLatch(1)

        val expected = true
        val actual = async {

            latch.countDown()
            roomUserRepository.isFirstTimeInTheApp()
        }.await()

        assertEquals(
            expected,
            actual
        )
    }

    @Test
    fun isFirstTimeInTheApp_addsLicenseWhenItDoesNotExist() = runBlocking {

        CountDownLatch(3)

//      1: making sure users' license does not exist:
        val licenseBefore = async {

            roomDatabase
                .licenseDAO()
                .getLicense()
        }.await()

        assertNull(licenseBefore)

//      2: launching the tested method:
        launch {

            roomUserRepository
                .isFirstTimeInTheApp()
        }.join()

//      3: checking if the license got added:
        val expectedLicense = License(
            first = 1,
            isSignedIn = true,
            totalAmountOfTrackies = 0
        )

        val actualLicense = async {

            roomDatabase
                .licenseDAO()
                .getLicense()
        }.await()

        assertEquals(
            expectedLicense,
            actualLicense
        )
    }

    @Test
    fun isFirstTimeInTheApp_doesNotCauseErrorsWhenCalledMoreThanOnce() = runBlocking {

        CountDownLatch(2)

//      1: calling the tested method more than once to check if OnConflictStrategy.REPLACE works:
        launch {

            roomUserRepository.isFirstTimeInTheApp()
            roomUserRepository.isFirstTimeInTheApp()
            roomUserRepository.isFirstTimeInTheApp()
        }.join()

//      2: checking if the License remains the same:
        val expected = License(
            first = 1,
            isSignedIn = true,
            totalAmountOfTrackies = 0
        )
        val actual = async {

            roomDatabase
                .licenseDAO()
                .getLicense()
        }.await()

        assertEquals(
            expected,
            actual
        )
    }

//////////////////////////////////////////////////////////////////////////////////////////

    @Test
    fun addNewUser_licenseGetsAddedProperly() = runBlocking {

        CountDownLatch(2)

//      1: ensuring license does not exist yet:
        val licenseBefore = async {

            roomDatabase
                .licenseDAO()
                .getLicense()
        }.await()

        assertNull(licenseBefore)

//      2: ensuring license gets added properly
        val expected = License(
            first = 1,
            isSignedIn = true,
            totalAmountOfTrackies = 0
        )
        val actual = async {

            roomUserRepository.addNewUser()

            roomDatabase
                .licenseDAO()
                .getLicense()
        }.await()

        assertEquals(
            expected,
            actual
        )
    }

    @Test
    fun addNewUser_doesNotCauseErrorsWhenCalledMoreThanOnce() = runBlocking {

        CountDownLatch(1)

//      1: ensuring license does not exist yet:
        val licenseBefore = async {

            roomDatabase
                .licenseDAO()
                .getLicense()
        }.await()

        assertNull(licenseBefore)

//      1: calling the tested method more than once to check if OnConflictStrategy.REPLACE works:
        val expected = License(
            first = 1,
            isSignedIn = true,
            totalAmountOfTrackies = 0
        )
        val actual = async {

            roomUserRepository.addNewUser()
            roomUserRepository.addNewUser()
            roomUserRepository.addNewUser()

            roomDatabase
                .licenseDAO()
                .getLicense()
        }.await()

        assertEquals(
            expected,
            actual
        )
    }

//////////////////////////////////////////////////////////////////////////////////////////

    @Test
    fun needToResetPastWeekRegularity_properlyDetectsNeedToResetRegularity_monday1() = runBlocking {

        CountDownLatch(2)

//      1: adding required data to the database
        launch {

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity1)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity2)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity3)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity4)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity5)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity6)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity7)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity8)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity9)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity10)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity11)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity12)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity13)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity14)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity15)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity16)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity17)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity18)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity19)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity20)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity21)

        }.join()

//      2: checking if the tested method works properly:
        val expected = true
        val actual = async {

            roomUserRepository.needToResetPastWeekRegularity()
        }.await()

        assertEquals(
            expected,
            actual
        )
    }

    @Test
    fun needToResetPastWeekRegularity_properlyDetectsNeedToResetRegularity_monday2() = runBlocking {

        CountDownLatch(2)

//      1: adding required data to the database
        launch {

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity1AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity2AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity3AfterReset)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity4)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity5)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity6)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity7)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity8)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity9)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity10)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity11)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity12)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity13)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity14)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity15)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity16)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity17)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity18)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity19)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity20)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity21)

        }.join()

//      2: checking if the tested method works properly:
        val expected = true
        val actual = async {

            roomUserRepository
                .needToResetPastWeekRegularity()
        }.await()

        assertEquals(
            expected,
            actual
        )
    }

    @Test
    fun needToResetPastWeekRegularity_properlyDetectsNeedToResetRegularity_monday3() = runBlocking {

        CountDownLatch(2)

//      1: adding required data to the database
        launch {

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity1AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity2AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity3AfterReset)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity4AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity5AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity6AfterReset)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity7)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity8)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity9)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity10)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity11)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity12)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity13)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity14)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity15)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity16)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity17)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity18)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity19)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity20)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity21)

        }.join()

//      2: checking if the tested method works properly:
        val expected = true
        val actual = async {

            roomUserRepository.needToResetPastWeekRegularity()
        }.await()

        assertEquals(
            expected,
            actual
        )
    }

    @Test
    fun needToResetPastWeekRegularity_properlyDetectsNeedToResetRegularity_monday4() = runBlocking {

        CountDownLatch(2)

//      1: adding required data to the database
        launch {

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity1AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity2AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity3AfterReset)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity4AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity5AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity6AfterReset)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity7AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity8AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity9AfterReset)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity10)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity11)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity12)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity13)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity14)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity15)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity16)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity17)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity18)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity19)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity20)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity21)

        }.join()

//      2: checking if the tested method works properly:
        val expected = true
        val actual = async {

            roomUserRepository.needToResetPastWeekRegularity()
        }.await()

        assertEquals(
            expected,
            actual
        )
    }

    @Test
    fun needToResetPastWeekRegularity_properlyDetectsNeedToResetRegularity_monday5() = runBlocking {

        CountDownLatch(2)

//      1: adding required data to the database
        launch {

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity1AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity2AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity3AfterReset)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity4AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity5AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity6AfterReset)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity7AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity8AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity9AfterReset)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity10AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity11AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity12AfterReset)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity13)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity14)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity15)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity16)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity17)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity18)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity19)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity20)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity21)

        }.join()

//      2: checking if the tested method works properly:
        val expected = true
        val actual = async {

            roomUserRepository.needToResetPastWeekRegularity()
        }.await()

        assertEquals(
            expected,
            actual
        )
    }

    @Test
    fun needToResetPastWeekRegularity_properlyDetectsNeedToResetRegularity_monday6() = runBlocking {

        CountDownLatch(2)

//      1: adding required data to the database
        launch {

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity1AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity2AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity3AfterReset)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity4AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity5AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity6AfterReset)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity7AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity8AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity9AfterReset)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity10AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity11AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity12AfterReset)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity13AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity14AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity15AfterReset)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity16)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity17)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity18)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity19)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity20)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity21)

        }.join()

//      2: checking if the tested method works properly:
        val expected = true
        val actual = async {

            roomUserRepository.needToResetPastWeekRegularity()
        }.await()

        assertEquals(
            expected,
            actual
        )
    }

    @Test
    fun needToResetPastWeekRegularity_properlyDetectsNeedToResetRegularity_monday7() = runBlocking {

        CountDownLatch(2)

//      1: adding required data to the database
        launch {

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity1AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity2AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity3AfterReset)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity4AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity5AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity6AfterReset)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity7AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity8AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity9AfterReset)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity10AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity11AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity12AfterReset)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity13AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity14AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity15AfterReset)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity16AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity17AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity18AfterReset)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity19)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity20)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity21)

        }.join()

//      2: checking if the tested method works properly:
        val expected = true
        val actual = async {

            roomUserRepository.needToResetPastWeekRegularity()
        }.await()

        assertEquals(
            expected,
            actual
        )
    }

    @Test
    fun needToResetPastWeekRegularity_noNeedToResetRegularity_monday7() = runBlocking {

        CountDownLatch(2)

//      1: adding required data to the database
        launch {

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity1AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity2AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity3AfterReset)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity4AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity5AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity6AfterReset)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity7AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity8AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity9AfterReset)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity10AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity11AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity12AfterReset)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity13AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity14AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity15AfterReset)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity16AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity17AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity18AfterReset)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity19AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity20AfterReset)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity21AfterReset)

        }.join()

//      2: checking if the tested method works properly:
        val expected = false
        val actual = async {

            roomUserRepository.needToResetPastWeekRegularity()
        }.await()

        assertEquals(
            expected,
            actual
        )
    }

//////////////////////////////////////////////////////////////////////////////////////////

    @Test
    fun resetWeeklyRegularity_weeklyRegularityGetsResetProperly() = runBlocking {

        CountDownLatch(2)

//      1: adding required data to the database:
        launch {

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity1)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity2)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity3)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity4)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity5)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity6)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity7)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity8)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity9)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity10)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity11)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity12)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity13)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity14)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity15)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity16)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity17)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity18)

            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity19)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity20)
            roomDatabase.regularityDAO().addTrackieToRegularity(regularity = regularity21)

        }.join()

//      2: making sure reset of regularity is required:
        val expectedBoolean = true
        val actualBoolean = async{

            roomUserRepository.needToResetPastWeekRegularity()
        }.await()

        assertEquals(
            expectedBoolean,
            actualBoolean
        )

//      3: actual test of the method:
        val expected = listOf(
            regularity1AfterReset,
            regularity2AfterReset,
            regularity3AfterReset,
            regularity4AfterReset,
            regularity5AfterReset,
            regularity6AfterReset,
            regularity7AfterReset,
            regularity8AfterReset,
            regularity9AfterReset,
            regularity10AfterReset,
            regularity11AfterReset,
            regularity12AfterReset,
            regularity13AfterReset,
            regularity14AfterReset,
            regularity15AfterReset,
            regularity16AfterReset,
            regularity17AfterReset,
            regularity18AfterReset,
            regularity19AfterReset,
            regularity20AfterReset,
            regularity21AfterReset
        )
        val actual = async {

            roomUserRepository.resetWeeklyRegularity()

            roomDatabase
                .regularityDAO()
                .fetchWeeklyRegularity()
        }.await()

        assertEquals(
            expected,
            actual
        )
    }

//////////////////////////////////////////////////////////////////////////////////////////

    @Test
    fun fetchUsersLicense_returnsNullWhenLicenseDoesNotExist() = runBlocking {

        CountDownLatch(2)

//      1: making sure license does not exist
        val license = async {

            roomDatabase
                .licenseDAO()
                .getLicense()
        }.await()

        assertNull(license)

//      2: test of the method:
        val expected = null
        val actual = async {

            roomUserRepository
                .fetchUsersLicense()
        }.await()

        assertEquals(
            expected,
            actual
        )
    }

    @Test
    fun fetchUsersLicense_returnsLicenseProperly() = runBlocking {

        CountDownLatch(2)

//      1: making sure license exists:
        val license = launch {

            roomUserRepository
                .addNewUser()

            roomDatabase
                .licenseDAO()
                .getLicense()
        }.join()

        assertNotNull(license)

//      2: test of the method:
        val expectedLicense = LicenseModel(
            active = false,
            validUntil = null,
            totalAmountOfTrackies = 0
        )

        val actualLicense = async {

            roomUserRepository
                .fetchUsersLicense()
        }.await()

        assertEquals(
            expectedLicense,
            actualLicense
        )
    }

//////////////////////////////////////////////////////////////////////////////////////////

    @Test
    fun fetchTrackiesForToday_properlyReturnsEmptyList() = runBlocking {

        CountDownLatch(2)

//      1: creating license and adding new trackies:
        launch {

            roomUserRepository
                .addNewUser()
        }.join()

//      2: making sure there are not any Trackies in the database:
        val expectedListOfTrackies = listOf<TrackieModel>()
        val actualListOfTrackies = async {

            roomDatabase
                .trackiesDAO()
                .getAllTrackies()
        }.await()

        assertEquals(
            expectedListOfTrackies,
            actualListOfTrackies
        )

//      3: test of the method:
        val expectedListOfTrackiesForToday = listOf<TrackieModel>()
        val actualListOfTrackiesForToday = async {

            roomUserRepository.fetchTrackiesForToday()
        }.await()

        assertEquals(
            expectedListOfTrackiesForToday,
            actualListOfTrackiesForToday
        )
    }

    @Test
    fun fetchTrackiesForToday_properlyReturnsListOfTrackies() = runBlocking {

        CountDownLatch(3 )

//      1: creating license and adding new trackies:
        launch {

            roomUserRepository
                .addNewUser()

            roomUserRepository
                .addNewTrackie(
                    trackieModel = trackie1,
                )

            roomUserRepository
                .addNewTrackie(
                    trackieModel = trackie2,
                )

            roomUserRepository
                .addNewTrackie(
                    trackieModel = trackie3,
                )
        }.join()

//      2: making sure trackies get added properly:
        val expectedListOfTrackies = listOf(
            trackie1.convertTrackieModelToTrackieEntity(),
            trackie2.convertTrackieModelToTrackieEntity(),
            trackie3.convertTrackieModelToTrackieEntity()
        )
        val actualListOfTrackies = async {

            roomDatabase
                .trackiesDAO()
                .getAllTrackies()
        }.await()

        assertEquals(
            expectedListOfTrackies,
            actualListOfTrackies
        )

//      3: test of the method:
        val expectedListOfTrackiesForToday = listOf(
            trackie1,
            trackie2
        )
        val actualListOfTrackiesForToday = async {

            roomUserRepository.fetchTrackiesForToday()
        }.await()

        assertEquals(
            expectedListOfTrackiesForToday,
            actualListOfTrackiesForToday
        )
    }

//////////////////////////////////////////////////////////////////////////////////////////

    @Test
    fun fetchAllTrackies_properlyReturnsEmptyList() = runBlocking {

        CountDownLatch(2)

//      1: creating license and adding new trackies:
        launch {

            roomUserRepository
                .addNewUser()
        }.join()

//      2: making sure there are not any Trackies in the database:
        val expectedListOfTrackies = listOf<TrackieModel>()
        val actualListOfTrackies = async {

            roomDatabase
                .trackiesDAO()
                .getAllTrackies()
        }.await()

        assertEquals(
            expectedListOfTrackies,
            actualListOfTrackies
        )

//      3: test of the method:
        val expectedListOfAllTrackies = listOf<TrackieModel>()
        val actualListOfAllTrackies = async {

            roomUserRepository.fetchAllTrackies()
        }.await()

        assertEquals(
            expectedListOfAllTrackies,
            actualListOfAllTrackies
        )
    }

    @Test
    fun fetchAllTrackies_properlyReturnsListOfTrackies() = runBlocking {

        CountDownLatch(3)

//      1: creating license and adding new trackies:
        launch {

            roomUserRepository
                .addNewUser()

            roomUserRepository
                .addNewTrackie(
                    trackieModel = trackie1,
                )

            roomUserRepository
                .addNewTrackie(
                    trackieModel = trackie2,
                )

            roomUserRepository
                .addNewTrackie(
                    trackieModel = trackie3,
                )
        }.join()

//      2: making sure trackies get added properly:
        val expectedListOfTrackies = listOf(
            trackie1.convertTrackieModelToTrackieEntity(),
            trackie2.convertTrackieModelToTrackieEntity(),
            trackie3.convertTrackieModelToTrackieEntity()
        )
        val actualListOfTrackies = async {

            roomDatabase
                .trackiesDAO()
                .getAllTrackies()
        }.await()

        assertEquals(
            expectedListOfTrackies,
            actualListOfTrackies
        )

//      3: test of the method:
        val expectedListOfTrackiesForToday = listOf(
            trackie1,
            trackie2,
            trackie3
        )
        val actualListOfTrackiesForToday = async {

            roomUserRepository.fetchAllTrackies()
        }.await()

        assertEquals(
            expectedListOfTrackiesForToday,
            actualListOfTrackiesForToday
        )
    }

//////////////////////////////////////////////////////////////////////////////////////////

    @Test
    fun fetchStatesOfTrackiesForToday_statesOfTrackiesForTodayGetReturnedProperly() = runBlocking {

        CountDownLatch(3)

//      1: creating license and adding trackies to the database:
        launch {

            roomUserRepository
                .addNewUser()

            roomUserRepository
                .addNewTrackie(
                    trackieModel = trackie1,
                )

            roomUserRepository
                .addNewTrackie(
                    trackieModel = trackie2,
                )

            roomUserRepository
                .addNewTrackie(
                    trackieModel = trackie3,
                )
        }.join()

//      2: marking trackie1 as ingested
        launch {

            roomUserRepository
                .markTrackieAsIngested(
                    currentDayOfWeek = DaysOfWeek.wednesday,
                    trackieModel = trackie1,
                    onSuccess = {},
                    onFailure = {}
                )
        }.join()

//      3: making sure states of trackies for today get returned properly
        val expected = mapOf<String, Boolean>(
            "lorem ipsum dolor" to true,
            "sit amet" to false
        )
        val actual = async {

            roomUserRepository.fetchStatesOfTrackiesForToday()
        }.await()

        assertEquals(
            expected,
            actual
        )
    }

//////////////////////////////////////////////////////////////////////////////////////////

    @Test
    fun fetchWeeklyRegularity_weeklyRegularityGetReturnedProperly() = runBlocking {

        CountDownLatch(6)

//      1: creating license and adding trackies to the database:
        launch{

            roomUserRepository
                .addNewUser()

            roomUserRepository
                .addNewTrackie(
                    trackieModel = trackie1,
                )

            roomUserRepository
                .addNewTrackie(
                    trackieModel = trackie2,
                )

            roomUserRepository
                .addNewTrackie(
                    trackieModel = trackie3,
                )
        }.join()

//      2: marking trackie1 as ingested
        launch {

            roomUserRepository
                .markTrackieAsIngested(
                    currentDayOfWeek = DaysOfWeek.wednesday,
                    trackieModel = trackie1,
                    onSuccess = {},
                    onFailure = {}
                )
        }.join()

//      3: making sure states of trackies for today get returned properly
        val expected = mapOf<String, Map<Int, Int>>(
            "monday" to mapOf(3 to 0),
            "tuesday" to mapOf(2 to 0),
            "wednesday" to mapOf(2 to 1),
            "thursday" to mapOf(2 to 0),
            "friday" to mapOf(2 to 0),
            "saturday" to mapOf(1 to 0),
            "sunday" to mapOf(1 to 0),
        )
        val actual = async {

            roomUserRepository
                .fetchWeeklyRegularity()
        }.await()

        assertEquals(
            expected,
            actual
        )
    }

//////////////////////////////////////////////////////////////////////////////////////////

    @Test
    fun addNewTrackie_returnsFalseWhenLicenseDoesNotExist() = runBlocking {

//      1: making sure license does not exist:
        val licenseBefore = async {

            roomDatabase
                .licenseDAO()
                .getLicense()
        }.await()

        assertNull(licenseBefore)

//      2: adding new Trackie:
        val expected = false
        val actual = async {

            roomUserRepository
                .addNewTrackie(
                    trackieModel = trackieModel1,
                )
        }.await()

        assertEquals(
            expected,
            actual
        )
    }

    @Test
    fun addNewTrackie_properlyAddsDataToColumns_returnsTrue() = runBlocking {

        CountDownLatch(5)

//      1: making sure license exists:
        val licenseBefore = async {

            roomUserRepository
                .addNewUser()

            roomDatabase
                .licenseDAO()
                .getLicense()
        }.await()

        assertNotNull(licenseBefore)

//      Making sure true is returned:
        val expected = true
        val actual = async {

            roomUserRepository
                .addNewTrackie(
                    trackieModel = trackieModel1,
                )
        }.await()

        assertEquals(
            expected,
            actual
        )

//      3: verifying whether license table gets updated properly:
        val expectedLicense = License(
            first = 1,
            isSignedIn = true,
            totalAmountOfTrackies = 1
        )
        val actualLicense = async {

            roomDatabase
                .licenseDAO()
                .getLicense()
        }.await()

        assertEquals(
            expectedLicense,
            actualLicense
        )

//      4: verifying whether trackie table updated properly:
        val expectedTrackieTableContent = listOf(
            trackieModel1.convertTrackieModelToTrackieEntity()
        )

        val actualTrackieTableContent = async {

            roomDatabase
                .trackiesDAO()
                .getAllTrackies()
        }.await()

        assertEquals(
            expectedTrackieTableContent,
            actualTrackieTableContent
        )

//      5: verifying whether regularity table gets updated properly:
        val expectedRegularityTableContent = listOf(
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.monday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.tuesday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.wednesday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.thursday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.friday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.saturday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.sunday, ingested = false),
        )

        val actualRegularityTableContent = async {

            roomDatabase
                .regularityDAO()
                .fetchWeeklyRegularity()
        }.await()

        assertEquals(
            expectedRegularityTableContent,
            actualRegularityTableContent
        )
    }

    @Test
    fun addNewTrackie_properlyAddsDataToColumnsSecondApproach_returnsTrue() = runBlocking {

        CountDownLatch(9)

//      1: making sure license exists:
        val licenseBefore = async {

            roomUserRepository
                .addNewUser()

            roomDatabase
                .licenseDAO()
                .getLicense()
        }.await()

        assertNotNull(licenseBefore)

//      2: making sure on failure error does not occur:
        val expected = true
        val actual = async {

            roomUserRepository
                .addNewTrackie(
                    trackieModel = trackieModel1,

                )
        }.await()

        assertEquals(
            expected,
            actual
        )

//      3: verifying whether license table gets updated properly:
        val expectedLicense = License(
            first = 1,
            isSignedIn = true,
            totalAmountOfTrackies = 1
        )
        val actualLicense = async {

            roomDatabase
                .licenseDAO()
                .getLicense()
        }.await()

        assertEquals(
            expectedLicense,
            actualLicense
        )

//      4: verifying whether trackie table updated properly:
        val expectedTrackieTableContent = listOf(
            trackieModel1.convertTrackieModelToTrackieEntity()
        )

        val actualTrackieTableContent = async {

            roomDatabase
                .trackiesDAO()
                .getAllTrackies()
        }.await()

        assertEquals(
            expectedTrackieTableContent,
            actualTrackieTableContent
        )

//      5: verifying whether regularity table gets updated properly:
        val expectedRegularityTableContent = listOf(
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.monday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.tuesday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.wednesday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.thursday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.friday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.saturday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.sunday, ingested = false),
        )

        val actualRegularityTableContent = async {

            roomDatabase
                .regularityDAO()
                .fetchWeeklyRegularity()
        }.await()

        assertEquals(
            expectedRegularityTableContent,
            actualRegularityTableContent
        )

//      6: adding another Trackie, making sure on failure error does not occur:
        val expectedV2 = true
        val actualV2 = async {

            roomUserRepository
                .addNewTrackie(
                    trackieModel = trackieModel2,

                )
        }.await()

        assertEquals(
            expectedV2,
            actualV2
        )

//      7: verifying whether license table gets updated properly:
        val expectedLicense2 = License(
            first = 1,
            isSignedIn = true,
            totalAmountOfTrackies = 2
        )
        val actualLicense2 = async {

            roomDatabase
                .licenseDAO()
                .getLicense()
        }.await()

        assertEquals(
            expectedLicense2,
            actualLicense2
        )

//      8: verifying whether trackie table updated properly:
        val expectedTrackieTableContent2 = listOf(
            trackieModel1.convertTrackieModelToTrackieEntity(),
            trackieModel2.convertTrackieModelToTrackieEntity()
        )
        val actualTrackieTableContent2 = async {

            roomDatabase
                .trackiesDAO()
                .getAllTrackies()
        }.await()

        assertEquals(
            expectedTrackieTableContent2,
            actualTrackieTableContent2
        )

//      9: verifying whether regularity table gets updated properly:
        val expectedRegularityTableContent2 = listOf(
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.monday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.tuesday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.wednesday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.thursday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.friday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.saturday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.sunday, ingested = false),

            Regularity(name = "only weekend", dayOfWeek = DaysOfWeek.friday, ingested = false),
            Regularity(name = "only weekend", dayOfWeek = DaysOfWeek.saturday, ingested = false),
            Regularity(name = "only weekend", dayOfWeek = DaysOfWeek.sunday, ingested = false),
        )
        val actualRegularityTableContent2 = async {

            roomDatabase
                .regularityDAO()
                .fetchWeeklyRegularity()
        }.await()

        assertEquals(
            expectedRegularityTableContent2,
            actualRegularityTableContent2
        )
    }

//////////////////////////////////////////////////////////////////////////////////////////

    @Test
    fun deleteTrackie_givesReturnsFalse_licenseIsNull() = runBlocking {

//      making sure license is null:
        val license = async{

            roomDatabase
                .licenseDAO()
                .getLicense()
        }.await()

        assertNull(license)

//      test of the method:
        val expected = false
        val actual = async {

            roomUserRepository
                .addNewTrackie(trackieModel = trackieWholeWeek1)
        }.await()

        assertEquals(
            expected,
            actual
        )
    }

    @Test
    fun deleteTrackie_returnsTrue_properlyDeletesSpecificTrackie() = runBlocking {

        CountDownLatch(9)

//      1: making sure license exists:
        val licenseBefore = async {

            roomUserRepository
                .addNewUser()

            roomDatabase
                .licenseDAO()
                .getLicense()
        }.await()

        assertNotNull(licenseBefore)

//      2: adding new Trackie
        launch{

            roomUserRepository
                .addNewTrackie(
                    trackieModel = trackieWholeWeek1,
                )
        }.join()

//      3: adding another Trackie
        launch{

            roomUserRepository
                .addNewTrackie(
                    trackieModel = trackieWeekend1,
                )
        }.join()

//      4: verifying whether license table gets updated properly:
        val expectedLicense = License(
            first = 1,
            isSignedIn = true,
            totalAmountOfTrackies = 2
        )
        val actualLicense = async {

            roomDatabase
                .licenseDAO()
                .getLicense()
        }.await()

        assertEquals(
            expectedLicense,
            actualLicense
        )

//      4: verifying whether trackie table gets updated properly:
        val expectedTrackieTableContent = listOf(
            trackieWholeWeek1.convertTrackieModelToTrackieEntity(),
            trackieWeekend1.convertTrackieModelToTrackieEntity()
        )
        val actualTrackieTableContent = async {

            roomDatabase
                .trackiesDAO()
                .getAllTrackies()
        }.await()

        assertEquals(
            expectedTrackieTableContent,
            actualTrackieTableContent
        )

//      5: verifying whether regularity table gets updated properly:
        val expectedRegularityTableContent = listOf(
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.monday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.tuesday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.wednesday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.thursday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.friday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.saturday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.sunday, ingested = false),

            Regularity(name = "only weekend", dayOfWeek = DaysOfWeek.friday, ingested = false),
            Regularity(name = "only weekend", dayOfWeek = DaysOfWeek.saturday, ingested = false),
            Regularity(name = "only weekend", dayOfWeek = DaysOfWeek.sunday, ingested = false),
        )
        val actualRegularityTableContent = async {

            roomDatabase
                .regularityDAO()
                .fetchWeeklyRegularity()
        }.await()

        assertEquals(
            expectedRegularityTableContent,
            actualRegularityTableContent
        )

//      6: deleting one of the trackies
        val returnedBoolean = async {

            roomUserRepository
                .deleteTrackie(trackieModel = trackieWeekend1)
        }.await()

        assertTrue(
            returnedBoolean
        )

//      7: making sure license gets updated
        val expectedLicense2 = License(
            first = 1,
            isSignedIn = true,
            totalAmountOfTrackies = 1
        )
        val actualLicense2 = async {

            roomDatabase
                .licenseDAO()
                .getLicense()
        }.await()

        assertEquals(
            expectedLicense2,
            actualLicense2
        )

//      8: making sure Trackie gets deleted from the Trackie table
        val expectedTrackieTableContent2 = listOf(
            trackieWholeWeek1.convertTrackieModelToTrackieEntity(),
        )
        val actualTrackieTableContent2 = async {

            roomDatabase
                .trackiesDAO()
                .getAllTrackies()
        }.await()

        assertEquals(
            expectedTrackieTableContent2,
            actualTrackieTableContent2
        )

//      9: verifying whether regularity table gets updated properly:
        val expectedRegularityTableContent2 = listOf(
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.monday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.tuesday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.wednesday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.thursday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.friday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.saturday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.sunday, ingested = false),
        )
        val actualRegularityTableContent2 = async {

            roomDatabase
                .regularityDAO()
                .fetchWeeklyRegularity()
        }.await()

        assertEquals(
            expectedRegularityTableContent2,
            actualRegularityTableContent2
        )
    }

//////////////////////////////////////////////////////////////////////////////////////////

    @Test
    fun markTrackieAsIngested_trackieProperlyGetsMarkedAsIngested() = runBlocking {

        CountDownLatch(6)

//      1: making sure license exists:
        val licenseBefore = async {

            roomUserRepository
                .addNewUser()

            roomDatabase
                .licenseDAO()
                .getLicense()
        }.await()

        assertNotNull(licenseBefore)

//      2: adding new trackie
        val expectedErrorMessage = ""
        val actualErrorMessage = async {

            var onFailureMessage = ""

            roomUserRepository
                .addNewTrackie(
                    trackieModel = trackieModel1,

                )

            onFailureMessage
        }.await()

        assertEquals(
            expectedErrorMessage,
            actualErrorMessage
        )

//      3: marking Trackie as ingested
        val expectedOnFailureMessage = ""
        val actualOnFailureMessage = async{

            var onFailure = ""

            roomUserRepository
                .markTrackieAsIngested(
                    currentDayOfWeek = DaysOfWeek.monday,
                    trackieModel = trackieModel1,
                    onSuccess = {},
                    onFailure = {
                        onFailure = it
                    }
                )

            onFailure
        }.await()

        assertEquals(
            expectedOnFailureMessage,
            actualOnFailureMessage
        )

//      4: making sure content in the license table does not get affected:
        val expectedLicense = License(
            first = 1,
            isSignedIn = true,
            totalAmountOfTrackies = 1
        )
        val actualLicense = async {

            roomDatabase
                .licenseDAO()
                .getLicense()
        }.await()

        assertEquals(
            expectedLicense,
            actualLicense
        )

//      5: making sure content in the trackie table does not get affected:
        val expectedTrackieTableContent = listOf(
            trackieModel1.convertTrackieModelToTrackieEntity()
        )
        val actualTrackieTableContent = async {

            roomDatabase
                .trackiesDAO()
                .getAllTrackies()
        }.await()

        assertEquals(
            expectedTrackieTableContent,
            actualTrackieTableContent
        )

//      6: verifying whether regularity table gets updated properly:
        val expectedRegularityTableContent = listOf(
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.monday, ingested = true),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.tuesday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.wednesday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.thursday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.friday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.saturday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.sunday, ingested = false),
        )
        val actualRegularityTableContent = async {

            roomDatabase
                .regularityDAO()
                .fetchWeeklyRegularity()
        }.await()

        assertEquals(
            expectedRegularityTableContent,
            actualRegularityTableContent
        )
    }

    @Test
    fun markTrackieAsIngested_trackieProperlyGetsMarkedAsIngestedCaseWithSeveralTrackies() = runBlocking {

        CountDownLatch(8)

//      creating the license
        launch {

            roomUserRepository
                .addNewUser()
        }.join()

//      1: adding first trackie:
        launch{

            roomUserRepository
                .addNewTrackie(
                    trackieModel = trackieModel1,
                )
        }.join()

//      2: adding second trackie:
        launch{

            roomUserRepository
                .addNewTrackie(
                    trackieModel = trackieModel2,
                )
        }.join()

//      3: making sure license got updated:
        val expectedLicense = License(
            first = 1,
            isSignedIn = true,
            totalAmountOfTrackies = 2
        )
        val actualLicense = async{

            roomDatabase
                .licenseDAO()
                .getLicense()
        }.await()

        assertEquals(
            expectedLicense,
            actualLicense
        )

//      4: making sure all trackies are in the database:
        val expectedListOfTrackies = listOf(
            trackieModel1.convertTrackieModelToTrackieEntity(),
            trackieModel2.convertTrackieModelToTrackieEntity()
        )
        val actualListOfTrackies = async{

            roomDatabase
                .trackiesDAO()
                .getAllTrackies()
        }.await()

        assertEquals(
            expectedListOfTrackies,
            actualListOfTrackies
        )

//      5: making sure all regularity instances are in the database
        val expectedListOfRegularities = listOf(
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.monday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.tuesday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.wednesday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.thursday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.friday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.saturday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.sunday, ingested = false),

            Regularity(name = "only weekend", dayOfWeek = DaysOfWeek.friday, ingested = false),
            Regularity(name = "only weekend", dayOfWeek = DaysOfWeek.saturday, ingested = false),
            Regularity(name = "only weekend", dayOfWeek = DaysOfWeek.sunday, ingested = false),
        )
        val actualListOfRegularities = async {

            roomDatabase
                .regularityDAO()
                .fetchWeeklyRegularity()
        }.await()

        assertEquals(
            expectedListOfRegularities,
            actualListOfRegularities
        )

//      6: marking trackie as ingested:
        launch {

            roomUserRepository
                .markTrackieAsIngested(
                    currentDayOfWeek = "friday",
                    trackieModel = trackieModel2,
                    onSuccess = {},
                    onFailure = {}
                )
        }.join()

//      7: making sure the regularity instance got updated
        val expectedListOfRegularities2 = listOf(
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.monday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.tuesday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.wednesday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.thursday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.friday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.saturday, ingested = false),
            Regularity(name = "trackie model 1", dayOfWeek = DaysOfWeek.sunday, ingested = false),

            Regularity(name = "only weekend", dayOfWeek = DaysOfWeek.friday, ingested = true),
            Regularity(name = "only weekend", dayOfWeek = DaysOfWeek.saturday, ingested = false),
            Regularity(name = "only weekend", dayOfWeek = DaysOfWeek.sunday, ingested = false),
        )
        val actualListOfRegularities2 = async {

            roomDatabase
                .regularityDAO()
                .fetchWeeklyRegularity()
        }.await()

        assertEquals(
            expectedListOfRegularities2,
            actualListOfRegularities2
        )
    }

//////////////////////////////////////////////////////////////////////////////////////////

    @Module
    @InstallIn(SingletonComponent::class)
    class RoomDatabaseProvider {

        @Provides
        @Singleton
        fun provideRoomDatabase(): RoomDatabase =
            Room
                .inMemoryDatabaseBuilder(
                    context = ApplicationProvider.getApplicationContext(),
                    klass = RoomDatabase::class.java
                )
                .allowMainThreadQueries()
                .build()
    }

    @Module
    @InstallIn(SingletonComponent::class)
    class UserRepositoryModule {

        @Provides
        fun provideUserRepository(
            lazyRoomDatabase: Lazy<RoomDatabase>
        ): UserRepository {

            val roomDatabase = lazyRoomDatabase.get()
            return RoomUserRepository(roomDatabase = roomDatabase)
        }
    }

    private val trackieWholeWeek1 = TrackieModel(
        name = "trackie model 1",
        totalDose = 10,
        measuringUnit = "g",
        repeatOn = listOf(
            DaysOfWeek.monday,
            DaysOfWeek.tuesday,
            DaysOfWeek.wednesday,
            DaysOfWeek.thursday,
            DaysOfWeek.friday,
            DaysOfWeek.saturday,
            DaysOfWeek.sunday,
        ),
        ingestionTime = null
    )

    private val trackieWeekend1 = TrackieModel(
        name = "only weekend",
        totalDose = 1,
        measuringUnit = "ml",
        repeatOn = listOf(
            DaysOfWeek.friday,
            DaysOfWeek.saturday,
            DaysOfWeek.sunday,
        ),
        ingestionTime = null
    )

    private val trackieModel1 = TrackieModel(
        name = "trackie model 1",
        totalDose = 10,
        measuringUnit = "g",
        repeatOn = listOf(
            DaysOfWeek.monday,
            DaysOfWeek.tuesday,
            DaysOfWeek.wednesday,
            DaysOfWeek.thursday,
            DaysOfWeek.friday,
            DaysOfWeek.saturday,
            DaysOfWeek.sunday,
        ),
        ingestionTime = null
    )

    private val trackieModel2 = TrackieModel(
        name = "only weekend",
        totalDose = 1,
        measuringUnit = "ml",
        repeatOn = listOf(
            DaysOfWeek.friday,
            DaysOfWeek.saturday,
            DaysOfWeek.sunday,
        ),
        ingestionTime = null
    )

    //  whole week
    val trackie1 = TrackieModel(
        name = "lorem ipsum dolor",
        totalDose = 1,
        measuringUnit = "g",
        repeatOn = listOf(
            DaysOfWeek.monday,
            DaysOfWeek.tuesday,
            DaysOfWeek.wednesday,
            DaysOfWeek.thursday,
            DaysOfWeek.friday,
            DaysOfWeek.saturday,
            DaysOfWeek.sunday
        ),
        ingestionTime = null
    )

    //  mon-fri
    val trackie2 = TrackieModel(
        name = "sit amet",
        totalDose = 3500,
        measuringUnit = "ml",
        repeatOn = listOf(
            DaysOfWeek.monday,
            DaysOfWeek.tuesday,
            DaysOfWeek.wednesday,
            DaysOfWeek.thursday,
            DaysOfWeek.friday,
        ),
        ingestionTime = null
    )

    //  mon
    val trackie3 = TrackieModel(
        name = "consectetur adipiscing elit",
        totalDose = 1,
        measuringUnit = "pcs",
        repeatOn = listOf(DaysOfWeek.monday),
        ingestionTime = null
    )

    val regularity1 = Regularity(
        name = "was an English",
        dayOfWeek = DaysOfWeek.monday,
        ingested = true
    )

    val regularity1AfterReset = Regularity(
        name = "was an English",
        dayOfWeek = DaysOfWeek.monday,
        ingested = false
    )

    val regularity2 = Regularity(
        name = "learned to bake",
        dayOfWeek = DaysOfWeek.monday,
        ingested = true
    )

    val regularity2AfterReset = Regularity(
        name = "learned to bake",
        dayOfWeek = DaysOfWeek.monday,
        ingested = false
    )

    val regularity3 = Regularity(
        name = "explored the hills",
        dayOfWeek = DaysOfWeek.monday,
        ingested = true
    )

    val regularity3AfterReset = Regularity(
        name = "explored the hills",
        dayOfWeek = DaysOfWeek.monday,
        ingested = false
    )

    val regularity4 = Regularity(
        name = "read a new book",
        dayOfWeek = DaysOfWeek.tuesday,
        ingested = true
    )

    val regularity4AfterReset = Regularity(
        name = "read a new book",
        dayOfWeek = DaysOfWeek.tuesday,
        ingested = false
    )

    val regularity5 = Regularity(
        name = "went cycling",
        dayOfWeek = DaysOfWeek.tuesday,
        ingested = false
    )

    val regularity5AfterReset = Regularity(
        name = "went cycling",
        dayOfWeek = DaysOfWeek.tuesday,
        ingested = false
    )

    val regularity6 = Regularity(
        name = "painted landscapes",
        dayOfWeek = DaysOfWeek.tuesday,
        ingested = true
    )

    val regularity6AfterReset = Regularity(
        name = "painted landscapes",
        dayOfWeek = DaysOfWeek.tuesday,
        ingested = false
    )

    val regularity7 = Regularity(
        name = "studied programming",
        dayOfWeek = DaysOfWeek.wednesday,
        ingested = false
    )

    val regularity7AfterReset = Regularity(
        name = "studied programming",
        dayOfWeek = DaysOfWeek.wednesday,
        ingested = false
    )

    val regularity8 = Regularity(
        name = "tended the garden",
        dayOfWeek = DaysOfWeek.wednesday,
        ingested = true
    )

    val regularity8AfterReset = Regularity(
        name = "tended the garden",
        dayOfWeek = DaysOfWeek.wednesday,
        ingested = false
    )

    val regularity9 = Regularity(
        name = "practiced yoga",
        dayOfWeek = DaysOfWeek.wednesday,
        ingested = true
    )

    val regularity9AfterReset = Regularity(
        name = "practiced yoga",
        dayOfWeek = DaysOfWeek.wednesday,
        ingested = false
    )

    val regularity10 = Regularity(
        name = "played chess",
        dayOfWeek = DaysOfWeek.thursday,
        ingested = false
    )

    val regularity10AfterReset = Regularity(
        name = "played chess",
        dayOfWeek = DaysOfWeek.thursday,
        ingested = false
    )

    val regularity11 = Regularity(
        name = "watched a movie",
        dayOfWeek = DaysOfWeek.thursday,
        ingested = true
    )

    val regularity11AfterReset = Regularity(
        name = "watched a movie",
        dayOfWeek = DaysOfWeek.thursday,
        ingested = false
    )

    val regularity12 = Regularity(
        name = "practiced piano",
        dayOfWeek = DaysOfWeek.thursday,
        ingested = false
    )

    val regularity12AfterReset = Regularity(
        name = "practiced piano",
        dayOfWeek = DaysOfWeek.thursday,
        ingested = false
    )

    val regularity13 = Regularity(
        name = "went hiking",
        dayOfWeek = DaysOfWeek.friday,
        ingested = true
    )

    val regularity13AfterReset = Regularity(
        name = "went hiking",
        dayOfWeek = DaysOfWeek.friday,
        ingested = false
    )

    val regularity14 = Regularity(
        name = "experimented in the kitchen",
        dayOfWeek = DaysOfWeek.friday,
        ingested = true
    )

    val regularity14AfterReset = Regularity(
        name = "experimented in the kitchen",
        dayOfWeek = DaysOfWeek.friday,
        ingested = false
    )

    val regularity15 = Regularity(
        name = "took a long walk",
        dayOfWeek = DaysOfWeek.friday,
        ingested = false
    )

    val regularity15AfterReset = Regularity(
        name = "took a long walk",
        dayOfWeek = DaysOfWeek.friday,
        ingested = false
    )

    val regularity16 = Regularity(
        name = "went to the farmer's market",
        dayOfWeek = DaysOfWeek.saturday,
        ingested = true
    )

    val regularity16AfterReset = Regularity(
        name = "went to the farmer's market",
        dayOfWeek = DaysOfWeek.saturday,
        ingested = false
    )

    val regularity17 = Regularity(
        name = "attended a workshop",
        dayOfWeek = DaysOfWeek.saturday,
        ingested = true
    )

    val regularity17AfterReset = Regularity(
        name = "attended a workshop",
        dayOfWeek = DaysOfWeek.saturday,
        ingested = false
    )

    val regularity18 = Regularity(
        name = "had a picnic",
        dayOfWeek = DaysOfWeek.saturday,
        ingested = true
    )

    val regularity18AfterReset = Regularity(
        name = "had a picnic",
        dayOfWeek = DaysOfWeek.saturday,
        ingested = false
    )

    val regularity19 = Regularity(
        name = "enjoyed a family dinner",
        dayOfWeek = DaysOfWeek.sunday,
        ingested = false
    )

    val regularity19AfterReset = Regularity(
        name = "enjoyed a family dinner",
        dayOfWeek = DaysOfWeek.sunday,
        ingested = false
    )

    val regularity20 = Regularity(
        name = "went fishing",
        dayOfWeek = DaysOfWeek.sunday,
        ingested = true
    )

    val regularity20AfterReset = Regularity(
        name = "went fishing",
        dayOfWeek = DaysOfWeek.sunday,
        ingested = false
    )

    val regularity21 = Regularity(
        name = "played board games",
        dayOfWeek = DaysOfWeek.sunday,
        ingested = false
    )

    val regularity21AfterReset = Regularity(
        name = "played board games",
        dayOfWeek = DaysOfWeek.sunday,
        ingested = false
    )
}