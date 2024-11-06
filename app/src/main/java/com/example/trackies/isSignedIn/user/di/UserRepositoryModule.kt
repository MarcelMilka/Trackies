package com.example.trackies.isSignedIn.user.di

import android.content.Context
import androidx.room.Room
import com.example.trackies.auth.buisness.AuthenticationServices
import com.example.trackies.auth.data.FirebaseAuthenticationService
import com.example.trackies.auth.serviceOperator.AuthenticationServiceOperator
import com.example.trackies.di.Named
import com.example.trackies.isSignedIn.user.data.FirebaseUserRepository
import com.example.trackies.isSignedIn.user.data.RoomUserRepository
import com.example.trackies.isSignedIn.user.data.UserRepository
import com.example.trackies.isSignedIn.user.roomDatabase.RoomDatabase
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class UserRepositoryModule {

    @Provides
    @Named("uniqueIdentifier")
    fun provideUniqueIdentifier(): String? = FirebaseAuthenticationService.getSignedInUser()

    @Provides
    @Reusable
    fun provideUserRepository(
        @Named("uniqueIdentifier") lazyUniqueIdentifier: Lazy<String>,
        @ApplicationContext appContext: Lazy<Context>
    ): UserRepository {

        when (AuthenticationServiceOperator.service.value) {

            AuthenticationServices.FirebaseAuthenticationService -> {

                val uniqueIdentifier = lazyUniqueIdentifier.get()
                return FirebaseUserRepository(uniqueIdentifier = uniqueIdentifier)
            }

            AuthenticationServices.RoomAuthenticationService -> {

                val roomDatabase = Room.databaseBuilder(
                    appContext.get(),
                    RoomDatabase::class.java, com.example.globalConstants.Room.databaseName,
                ).build()

                return RoomUserRepository(
                    roomDatabase = roomDatabase
                )
            }
        }
    }
}