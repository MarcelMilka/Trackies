package com.example.trackies.isSignedIn.user.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.trackies.aRoom.db.RoomDatabase
import com.example.trackies.isSignedIn.user.buisness.entities.License
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class LicenseDaoTest {

    private val theLicense = License(
        first = 1,
        isSignedIn = true,
        totalAmountOfTrackies = 0
    )

    private val license1 = License(
        first = 1,
        isSignedIn = true,
        totalAmountOfTrackies = 1
    )

    private val license2 = License(
        first = 1,
        isSignedIn = true,
        totalAmountOfTrackies = 2
    )

    private lateinit var roomDatabase: RoomDatabase
    private lateinit var licenseDAO: LicenseDAO

    @Before
    fun setup() {

        roomDatabase = Room.inMemoryDatabaseBuilder(
            context = ApplicationProvider.getApplicationContext(),
            klass = RoomDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        licenseDAO = roomDatabase.licenseDAO()
    }

    @After
    fun afterTest() {

        roomDatabase.close()
    }


    @Test
    fun isFirstTimeInTheApp_nullGetsReturned() = runTest {

        val fetchedLicenses = licenseDAO
            .isFirstTimeInTheApp()

        assertEquals(
            null,
            fetchedLicenses
        )
    }

    @Test
    fun isFirstTimeInTheApp_licenseGetsReturned() = runTest {

        licenseDAO
            .createLicense(license = theLicense)

        val fetchedLicenses =
            licenseDAO
            .isFirstTimeInTheApp()

        assertEquals(
            theLicense,
            fetchedLicenses
        )
    }

    @Test
    fun isFirstTimeInTheApp_onConflictStrategyReplacesEntities() = runTest {

        licenseDAO
            .createLicense(license = theLicense)

        licenseDAO
            .createLicense(license = theLicense)

        val fetchedLicenses =
            licenseDAO
                .isFirstTimeInTheApp()

        assertEquals(
            theLicense,
            fetchedLicenses
        )
    }

    @Test
    fun increasingAndDecreasingWorksProperly() = runTest {

        licenseDAO.createLicense(license = theLicense)

//      Increase by one
        licenseDAO.increaseTotalAmountOfTrackiesByOne(totalAmountOfTrackies = 1)

        val _license1 = licenseDAO.getLicense()

        assertEquals(license1, _license1)

        licenseDAO.increaseTotalAmountOfTrackiesByOne(totalAmountOfTrackies = 2)

        val _license2 = licenseDAO.getLicense()

        assertEquals(license2, _license2)

        licenseDAO.decreaseTotalAmountOfTrackiesByOne(totalAmountOfTrackies = 1)

        val lastLicense = licenseDAO.getLicense()

        assertEquals(license1, lastLicense)
    }

    @Test
    fun deleteLicense_worksProperly() = runBlocking {

        var license: License? = null

        launch {

            licenseDAO
                .createLicense(license = theLicense)

            license =
                licenseDAO.getLicense()
        }.join()

        assertNotNull(license)

        launch {

            licenseDAO
                .deleteLicense()

            license =
                licenseDAO.getLicense()

        }.join()

        assertNull(license)
    }
}