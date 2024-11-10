package com.example.trackies.isSignedIn.user.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.trackies.isSignedIn.user.buisness.entities.License
import com.example.trackies.isSignedIn.user.roomDatabase.RoomDatabase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class LicenseDaoTest {

    private val license = License(
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
            .createLicense(license = license)

        val fetchedLicenses =
            licenseDAO
            .isFirstTimeInTheApp()

        assertEquals(
            license,
            fetchedLicenses
        )
    }

    @Test
    fun whenUserSignsInEditLicenseToSignedIn() = runTest {

        licenseDAO.createLicense(license)

        licenseDAO.signInTheUser()

        val expectedLicense = License(first = 1, isSignedIn = true, totalAmountOfTrackies = 0)
        val actualLicense = licenseDAO.getLicense()

        assertEquals(expectedLicense, actualLicense)
    }

    @Test
    fun isFirstTimeInTheApp_onConflictStrategyReplacesEntities() = runTest {

        licenseDAO
            .createLicense(license = license)

        licenseDAO
            .createLicense(license = license)

        val fetchedLicenses =
            licenseDAO
                .isFirstTimeInTheApp()

        assertEquals(
            license,
            fetchedLicenses
        )
    }

    @Test
    fun increasingAndDecreasingWorksProperly() = runTest {

        licenseDAO.createLicense(license = license)

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
    fun whenUserSignsOutEditLicenseToSignedOut() = runTest {

        licenseDAO.createLicense(license)

        licenseDAO.signInTheUser()
        licenseDAO.signOutTheUser()

        val expectedLicense = License(first = 1, isSignedIn = false, totalAmountOfTrackies = 0)
        val actualLicense = licenseDAO.getLicense()

        assertEquals(expectedLicense, actualLicense)
    }

    @Test
    fun userCanBeSignedOutAndSignedInTwiceWithoutInterruption() = runBlocking {

        licenseDAO.createLicense(license)

        licenseDAO.signOutTheUser()
        licenseDAO.signOutTheUser()

        val expectedLicense = License(first = 1, isSignedIn = false, totalAmountOfTrackies = 0)
        val actualLicense = licenseDAO.getLicense()

        assertEquals(expectedLicense, actualLicense)

        licenseDAO.signInTheUser()
        licenseDAO.signInTheUser()

        val expectedLicense1 = License(first = 1, isSignedIn = true, totalAmountOfTrackies = 0)
        val actualLicense1 = licenseDAO.getLicense()

        assertEquals(expectedLicense1, actualLicense1)
    }
}