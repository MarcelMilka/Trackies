package com.example.trackies.testsOfAuthenticationServices

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.globalConstants.Destinations
import com.example.globalConstants.MeasuringUnit
import com.example.trackies.aRoom.db.RoomDatabase
import com.example.trackies.aRoom.di.RoomDatabaseModule
import com.example.trackies.auth.data.AuthenticationService
import com.example.trackies.auth.data.FirebaseAuthenticationService
import com.example.trackies.auth.data.RoomAuthenticationService
import com.example.trackies.auth.authenticationMethodProvider.AuthenticationMethodProviderModule
import com.example.trackies.di.FirebaseAuthenticator
import com.example.trackies.di.RoomAuthenticator
import com.example.trackies.isSignedIn.user.buisness.entities.License
import com.example.trackies.isSignedIn.user.buisness.entities.Regularity
import com.example.trackies.isSignedIn.user.buisness.entities.Trackie
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

@UninstallModules(RoomDatabaseModule::class, AuthenticationMethodProviderModule::class)
@HiltAndroidTest
class RoomAuthenticationServiceTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var roomDatabase: RoomDatabase

    @Inject
    @FirebaseAuthenticator
    lateinit var firebaseAuthenticationService: AuthenticationService

    @Inject
    @RoomAuthenticator
    lateinit var roomAuthenticationService: AuthenticationService

    @Before
    fun beforeTest() {

        hiltRule.inject()
    }

    @After
    fun afterTest() {

        roomDatabase.close()
    }

    @Test
    fun dependenciesAreInjectedProperly() = runBlocking {

        assertNotNull(roomDatabase)
        assertNotNull(roomAuthenticationService)
    }

    @Test
    fun getSignedInUser_LicenseIsNull_nullIsReturned() = runBlocking {

        CountDownLatch(2)

//      Assertions:
        val license = roomDatabase.licenseDAO().getLicense()
        assertNull(license)

        val expectedReturnedValue = null
        val actualReturnedValue = async {

            roomAuthenticationService
                .getSignedInUser()
        }.await()
        assertEquals(
            expectedReturnedValue,
            actualReturnedValue
        )
    }

    @Test
    fun getSignedInUser_LicenseExists_UserIsNotSignedIn_falseIsReturned() = runBlocking {

        CountDownLatch(3)

//      Preparations:
        this.launch {

            roomDatabase
                .licenseDAO()
                .createLicense(
                    license = License(
                        first = 1,
                        isSignedIn = false,
                        totalAmountOfTrackies = 0
                    )
                )
        }.join()

//      Assertions:
        val license = roomDatabase.licenseDAO().getLicense()
        assertNotNull(license)

        val expectedReturnedValue = "false"
        val actualReturnedValue = async {

            roomAuthenticationService
                .getSignedInUser()
        }.await()
        assertEquals(
            expectedReturnedValue,
            actualReturnedValue
        )
    }

    @Test
    fun getSignedInUser_LicenseExists_UserIsSignedIn_trueIsReturned() = runBlocking {

        CountDownLatch(3)

//      Preparations:
        this.launch {

            roomDatabase
                .licenseDAO()
                .createLicense(
                    license = License(
                        first = 1,
                        isSignedIn = false,
                        totalAmountOfTrackies = 0
                    )
                )

            roomDatabase
                .licenseDAO()
                .signIn()
        }.join()

//      Assertions:
        val license = roomDatabase.licenseDAO().getLicense()
        assertNotNull(license)

        val expectedReturnedValue = "true"
        val actualReturnedValue = async {

            roomAuthenticationService
                .getSignedInUser()
        }.await()
        assertEquals(
            expectedReturnedValue,
            actualReturnedValue
        )
    }


    @Test
    fun initialDestination_getSignedInUserReturnsNull_IsSignedOut() = runBlocking {

//      Assertions:
        val expectedInitialDestination = Destinations.IsSignedOut
        val actualInitialDestination = roomAuthenticationService.initialDestination

        assertEquals(
            expectedInitialDestination,
            actualInitialDestination
        )
    }

    @Test
    fun initialDestination_getSignedInUserReturnsFalse_IsSignedOut() = runBlocking {

        CountDownLatch(2)

//      Preparations:
        this.launch {

            roomDatabase
                .licenseDAO()
                .createLicense(
                    license = License(
                        first = 1,
                        isSignedIn = false,
                        totalAmountOfTrackies = 0
                    )
                )
        }.join()

//      Assertions:
        val expectedInitialDestination = Destinations.IsSignedOut
        val actualInitialDestination = roomAuthenticationService.initialDestination

        assertEquals(
            expectedInitialDestination,
            actualInitialDestination
        )
    }

    @Test
    fun initialDestination_getSignedInUserReturnsTrue_IsSignedIn() = runBlocking {

        CountDownLatch(2)

//      Preparations:
        this.launch {

            roomDatabase
                .licenseDAO()
                .createLicense(
                    license = License(
                        first = 1,
                        isSignedIn = false,
                        totalAmountOfTrackies = 0
                    )
                )

            roomDatabase
                .licenseDAO()
                .signIn()
        }.join()

//      Assertions:
        val expectedInitialDestination = Destinations.IsSignedIn
        val actualInitialDestination = roomAuthenticationService.initialDestination

        assertEquals(
            expectedInitialDestination,
            actualInitialDestination
        )
    }


    @Test
    fun signInWithEmailAndPassword_processOfSigningInWorksProperly() = runBlocking {

//      Preparations:
        launch {

            roomDatabase
                .licenseDAO()
                .createLicense(
                    license = License(
                        first = 1,
                        isSignedIn = false,
                        totalAmountOfTrackies = 0
                    )
                )
        }.join()

        roomAuthenticationService.signInWithEmailAndPassword(
            email = "_",
            password = "_",
            onFailedToSignIn = {},
            onSucceededToSignIn = {}
        )

//      Assertions:
        val expectedLicense = License(
            first = 1,
            isSignedIn = true,
            totalAmountOfTrackies = 0
        )
        val actualLicense = roomDatabase.licenseDAO().getLicense()
        assertEquals(
            expectedLicense,
            actualLicense
        )
    }

    @Test
    fun getEmailAddress_properlyReturnsString() = runBlocking {

        val expectedMessage = "Local database user"
        val actualMessage = roomAuthenticationService.getEmailAddress()

        assertEquals(
            expectedMessage,
            actualMessage
        )
    }

    @Test
    fun signOut_processOfSigningOutWorksProperly() = runBlocking {

//      Preparations:
        launch {

            roomDatabase
                .licenseDAO()
                .createLicense(
                    license = License(
                        first = 1,
                        isSignedIn = true,
                        totalAmountOfTrackies = 0
                    )
                )
        }.join()

//      Assertions:
        val expectedOnCompleteMessage = "ok"
        var actualOnCompleteMessage = ""

        val expectedOnFailureMessage = ""
        var actualOnFailureMessage = ""

        roomAuthenticationService.signOut(
            onComplete = {
                actualOnCompleteMessage = "ok"
            },
            onFailure = {
                actualOnFailureMessage = "ok"
            }
        )

        val expectedLicense = License(
            first = 1,
            isSignedIn = false,
            totalAmountOfTrackies = 0
        )
        val actualLicence = roomDatabase.licenseDAO().getLicense()

        assertEquals(
            expectedOnCompleteMessage,
            actualOnCompleteMessage
        )

        assertEquals(
            expectedOnFailureMessage,
            actualOnFailureMessage
        )

        assertEquals(
            expectedLicense,
            actualLicence
        )
    }
    
    @Test
    fun deleteAccount_processOfDeletingAccountWorksProperly() = runBlocking {
        
//      Preparations:
        launch {

            roomDatabase
                .licenseDAO()
                .createLicense(
                    license = License(
                        first = 1,
                        isSignedIn = true,
                        totalAmountOfTrackies = 1
                    )
                )
            
            roomDatabase
                .trackiesDAO()
                .addNewTrackie(
                    trackie = Trackie(
                        name = "TODO()",
                        totalDose = 1,
                        measuringUnit = MeasuringUnit.g,
                        monday = true,
                        tuesday = true ,
                        wednesday = true,
                        thursday = true,
                        friday = true,
                        saturday = true,
                        sunday = true
                    )
                )

            listOf("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunay").forEach {

                roomDatabase
                    .regularityDAO()
                    .addTrackieToRegularity(
                        regularity = Regularity(
                            name = "TODO()",
                            dayOfWeek = it,
                            ingested = false
                        )
                    )
            }
        }.join()

//      Assertions:
        val expectedOnComplete = "ok"
        var actualOnComplete = ""

        val expectedOnFailure = ""
        var actualOnFailure = ""

        roomAuthenticationService.deleteAccount(
            password = "_",
            onComplete = {
                actualOnComplete = "ok"
            },
            onFailure = {
                actualOnFailure = "ok"
            }
        )

        assertEquals(
            expectedOnComplete,
            actualOnComplete
        )

        assertEquals(
            expectedOnFailure,
            actualOnFailure
        )

        val license = roomDatabase
            .licenseDAO()
            .getLicense()

        assertNull(license)

        val expectedWeeklyRegularity = listOf<Regularity>()
        val actualWeeklyRegularity = roomDatabase
            .regularityDAO()
            .fetchWeeklyRegularity()

        assertEquals(
            expectedWeeklyRegularity,
            actualWeeklyRegularity
        )

        val expectedListOfAllTrackies = listOf<Trackie>()
        val actualListOfAllTrackies = roomDatabase
            .trackiesDAO()
            .getAllTrackies()

        assertEquals(
            expectedListOfAllTrackies,
            actualListOfAllTrackies
        )
    }

    @Module
    @InstallIn(SingletonComponent::class)
    class DaggerModule {

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

        @Provides
        @Singleton
        @FirebaseAuthenticator
        fun provideFirebaseAuthenticationService(): AuthenticationService =
            FirebaseAuthenticationService

        @Provides
        @Singleton
        @RoomAuthenticator
        fun provideRoomAuthenticationService(roomDatabase: RoomDatabase): AuthenticationService =
            RoomAuthenticationService(
                roomDatabase = roomDatabase
            )
    }
}