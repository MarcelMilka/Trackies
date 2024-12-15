package com.example.trackies.aRoom.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.trackies.aRoom.db.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertSame
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Singleton

@HiltAndroidTest
@UninstallModules(RoomDatabaseModule::class)
class RoomDatabaseProviderTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var roomDatabase: RoomDatabase

    @Inject
    lateinit var roomDatabase1: RoomDatabase

    @Inject
    lateinit var roomDatabase2: RoomDatabase

    @Before
    fun beforeTest() {

        hiltRule.inject()
    }

    @After
    fun afterTest() {

        roomDatabase.close()
        roomDatabase1.close()
        roomDatabase2.close()
    }

    @Test
    fun roomDatabaseIsProvidedProperly() = runBlocking {

        assertNotNull(roomDatabase)
    }

    @Test
    fun roomDatabaseIsProvidedAsSingleton() = runBlocking {

        assertSame(
            roomDatabase,
            roomDatabase1
        )

        Log.d("Halla!", "$roomDatabase")
        Log.d("Halla!", "$roomDatabase1")
        Log.d("Halla!", "$roomDatabase2")

        assertSame(
            roomDatabase1,
            roomDatabase2
        )
    }


    @Module
    @InstallIn(SingletonComponent::class)
    class RoomDatabaseProvider {

        @Provides
        @Singleton
        fun provideRoomDatabase(@ApplicationContext appContext: Context): RoomDatabase =
            Room.databaseBuilder(
                appContext,
                RoomDatabase::class.java,
                com.example.globalConstants.Room.databaseName
            )
                .build()
    }
}