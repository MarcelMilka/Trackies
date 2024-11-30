package com.example.trackies.isSignedIn.user.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.trackies.aRoom.db.RoomDatabase
import com.example.trackies.isSignedIn.user.buisness.entities.License
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
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
class LicenseDaoTest {

    private lateinit var roomDatabase: RoomDatabase
    private lateinit var licenseDao: LicenseDAO

    @Before
    fun setUp() {

        roomDatabase = Room.inMemoryDatabaseBuilder(
            context = ApplicationProvider.getApplicationContext(),
            klass = RoomDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        licenseDao = roomDatabase.licenseDAO()
    }

    @After
    fun afterTest() {

        roomDatabase.close()
    }

    private val license = License(
        first = 1,
        isSignedIn = true,
        totalAmountOfTrackies = 0
    )

    @Test
    fun isFirstTimeInTheApp_returnsNullProperly() = runBlocking {

        CountDownLatch(1)

        val retrievedData = async {

            licenseDao.isFirstTimeInTheApp()

        }.await()

        assertNull(retrievedData)
    }

    @Test
    fun isFirstTimeInTheApp_returnsLicenseProperly() = runBlocking {

        CountDownLatch(1)

        val retrievedData = async {

            licenseDao
                .createLicense(license = license)

            licenseDao.isFirstTimeInTheApp()
        }.await()

        assertEquals(
            retrievedData,
            license
        )
    }

    @Test
    fun createLicense_createsLicenseProperly() = runBlocking {

        CountDownLatch(1)

        val retrievedData = async {

            licenseDao.createLicense(license = license)

            licenseDao.getLicense()
        }.await()

        assertEquals(
            retrievedData,
            license
        )
    }

    @Test
    fun createLicense_replacesLicensesProperly() = runBlocking {

        CountDownLatch(1)

        val replacingLicense = License(
            first = 1,
            isSignedIn = false,
            totalAmountOfTrackies = 10
        )

        val retrievedData = async {

            licenseDao.createLicense(license = license)

            licenseDao.createLicense(license = replacingLicense)

            licenseDao.getLicense()
        }.await()

        assertEquals(
            retrievedData,
            replacingLicense
        )
    }

    @Test
    fun getLicense_returnsNullProperly() = runBlocking {

    CountDownLatch(1)

    val retrievedData = async {

        licenseDao.getLicense()
    }.await()

        assertNull(retrievedData)
    }

    @Test
    fun getLicense_returnsLicenseProperly() = runBlocking {

        CountDownLatch(1)

        val retrievedData = async {

            licenseDao
                .createLicense(license = license)


            licenseDao.getLicense()
        }.await()

        assertEquals(
            retrievedData,
            license
        )
    }

    @Test
    fun signIn_properlySignsInTheUser() = runBlocking {

        CountDownLatch(1)

        val expectedLicense = License(
            first = 1,
            isSignedIn = true,
            totalAmountOfTrackies = 0
        )

        val retrievedData = async {

            licenseDao
                .createLicense(license = license)

            licenseDao.signOut()

            licenseDao.signIn()

            licenseDao.getLicense()
        }.await()

        assertEquals(
            expectedLicense,
            retrievedData
        )
    }

    @Test
    fun signOut_properlySignsOutTheUser() = runBlocking {

        CountDownLatch(1)

        val expectedLicense = License(
            first = 1,
            isSignedIn = false,
            totalAmountOfTrackies = 0
        )

        val retrievedData = async {

            licenseDao
                .createLicense(license = license)

            licenseDao.signOut()

            licenseDao.getLicense()
        }.await()

        assertEquals(
            expectedLicense,
            retrievedData
        )
    }

    @Test
    fun increaseTotalAmountOfTrackiesByOne_properlyIncreasesAmountOfTrackiesByOne_v1() = runBlocking {

        CountDownLatch(1)

        val expectedLicense = License(
            first = 1,
            isSignedIn = true,
            totalAmountOfTrackies = 1
        )

        val retrievedData = async {

            licenseDao.createLicense(license = license)

            licenseDao.increaseTotalAmountOfTrackiesByOne(
                totalAmountOfTrackies = 1
            )

            licenseDao.getLicense()
        }.await()

        assertEquals(
            expectedLicense,
            retrievedData
        )
    }

    @Test
    fun increaseTotalAmountOfTrackiesByOne_properlyIncreasesAndDecreasesAmountOfTrackiesByOne() = runBlocking {

        CountDownLatch(1)

        val expectedLicense = License(
            first = 1,
            isSignedIn = true,
            totalAmountOfTrackies = 0
        )

        val retrievedData = async {

            licenseDao.createLicense(license = license)

            licenseDao.increaseTotalAmountOfTrackiesByOne(
                totalAmountOfTrackies = 1
            )

            licenseDao.decreaseTotalAmountOfTrackiesByOne(
                totalAmountOfTrackies = 0
            )

            licenseDao.getLicense()
        }.await()

        assertEquals(
            expectedLicense,
            retrievedData
        )
    }

    @Test
    fun deleteUsersLicense_properlyDeletesUsersLicense() = runBlocking {

        CountDownLatch(1)

        val retrievedData = async {

            licenseDao.createLicense(license = license)

            licenseDao.deleteUsersLicense()

            licenseDao.getLicense()
        }.await()

        assertNull(retrievedData)
    }
}