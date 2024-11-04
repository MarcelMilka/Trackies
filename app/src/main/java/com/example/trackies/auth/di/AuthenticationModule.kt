package com.example.trackies.auth.di

import android.database.Cursor
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.trackies.auth.data.AuthenticationService
import com.example.trackies.auth.data.FirebaseAuthenticationService
import com.example.trackies.auth.data.RoomAuthenticationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton

@Module()
@InstallIn(SingletonComponent::class)
class AuthenticationModule {

    @Provides
    fun provideAuthenticationService(): AuthenticationService {

        return if (File("RoomDatabase.db").exists() == true) {

            Log.d("Halla!", "provide room")
            RoomAuthenticationService
        }

        else {

            Log.d("Halla!", "provide firebase")
            FirebaseAuthenticationService
        }
    }
}