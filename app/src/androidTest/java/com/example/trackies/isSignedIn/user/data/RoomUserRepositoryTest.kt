package com.example.trackies.isSignedIn.user.data

import com.example.trackies.aRoom.db.RoomDatabase
import com.example.trackies.aRoom.di.RoomDatabaseProvider
import com.example.trackies.isSignedIn.user.buisness.entities.License
import com.example.trackies.isSignedIn.user.di.UserRepositoryModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

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
}