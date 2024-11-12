package com.example.trackies.isSignedIn.user.di

import android.util.Log
import com.example.trackies.aRoom.db.RoomDatabase
import com.example.trackies.auth.buisness.AuthenticationMethod
import com.example.trackies.auth.data.FirebaseAuthenticationService
import com.example.trackies.auth.providerOfAuthenticationMethod.AuthenticationMethodProvider
import com.example.trackies.di.Named
import com.example.trackies.isSignedIn.user.data.FirebaseUserRepository
import com.example.trackies.isSignedIn.user.data.RoomUserRepository
import com.example.trackies.isSignedIn.user.data.UserRepository
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class UserRepositoryModule {

    @Provides
    @Named("uniqueIdentifier")
    fun provideUniqueIdentifier(): String? =
        FirebaseAuthenticationService.getSignedInUser()

    @Provides
    fun provideUserRepository(
        authenticationMethodProvider: AuthenticationMethodProvider,
        @Named("uniqueIdentifier") lazyUniqueIdentifier: Lazy<String>,
        lazyRoomDatabase: Lazy<RoomDatabase>
    ): UserRepository {

        Log.d("Magnetic Man", "$this provides user repository")

        val authenticationMethod =
            authenticationMethodProvider.getAuthenticationMethod()

        when(authenticationMethod) {

            AuthenticationMethod.Firebase -> {

                val uniqueIdentifier = lazyUniqueIdentifier.get()
                return FirebaseUserRepository(uniqueIdentifier = uniqueIdentifier)
            }

            AuthenticationMethod.Room -> {

                val roomDatabase = lazyRoomDatabase.get()
                return RoomUserRepository(roomDatabase = roomDatabase)
            }
        }
    }
}