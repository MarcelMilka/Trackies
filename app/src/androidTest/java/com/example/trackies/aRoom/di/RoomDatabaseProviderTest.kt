package com.example.trackies.aRoom.di

import android.content.Context
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
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Singleton

@HiltAndroidTest
@UninstallModules(RoomDatabaseProvider::class)
class RoomDatabaseProviderTest {

    @Inject
    lateinit var roomDatabase: RoomDatabase

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun beforeTest() {

        hiltRule.inject()
    }

    @Test
    fun roomDatabaseGetsProvidedCorrectly() = runBlocking {

        assertNotNull("$roomDatabase is the provided dependency.", roomDatabase)
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