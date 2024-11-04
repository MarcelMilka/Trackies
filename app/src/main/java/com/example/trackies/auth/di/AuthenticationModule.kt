package com.example.trackies.auth.di

import android.database.Cursor
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.trackies.auth.buisness.AuthenticationServices
import com.example.trackies.auth.data.AuthenticationService
import com.example.trackies.auth.data.FirebaseAuthenticationService
import com.example.trackies.auth.data.RoomAuthenticationService
import com.example.trackies.auth.serviceOperator.AuthenticationServiceOperator
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

        Log.d("Halla!", "required authentication service :)")

        if (File("RoomDatabase.db").exists() == true) {

            return when (AuthenticationServiceOperator.service.value) {

                AuthenticationServices.FirebaseAuthenticationService -> {

                    Log.d("Halla!", "provide firebase")
                    FirebaseAuthenticationService
                }

                AuthenticationServices.RoomAuthenticationService -> {

                    Log.d("Halla!", "provide room")
                    RoomAuthenticationService
                }
            }
        }

        else {

            return when (AuthenticationServiceOperator.service.value) {

                AuthenticationServices.FirebaseAuthenticationService -> {

                    Log.d("Halla!", "provide firebase")
                    FirebaseAuthenticationService
                }

                AuthenticationServices.RoomAuthenticationService -> {

                    Log.d("Halla!", "provide room")
                    RoomAuthenticationService
                }
            }
        }
    }
}