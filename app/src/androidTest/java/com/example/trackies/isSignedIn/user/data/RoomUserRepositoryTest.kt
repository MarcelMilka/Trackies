package com.example.trackies.isSignedIn.user.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.trackies.aRoom.db.RoomDatabase
import com.example.trackies.aRoom.di.RoomDatabaseModule
import com.example.trackies.isSignedIn.user.buisness.entities.License
import com.example.trackies.isSignedIn.user.di.UserRepositoryModule
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
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
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@UninstallModules(UserRepositoryModule::class, RoomDatabaseModule::class)
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
}