package com.example.trackies.aRoom.di

import android.content.Context
import androidx.room.Room
import com.example.globalConstants.annotationClasses.Tested
import com.example.trackies.aRoom.db.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Tested
@Module
@InstallIn(SingletonComponent::class)
class RoomDatabaseModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext appContext: Context): RoomDatabase =
        Room.databaseBuilder(
            appContext,
            RoomDatabase::class.java,
            com.example.globalConstants.Room.databaseName,
        ).build()
}