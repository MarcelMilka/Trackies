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
    fun provideUniqueIdentifier(): String? {

        val uid =
            FirebaseAuthenticationService.getSignedInUser()

        Log.d("MSxxx", "$uid")

        return uid
    }

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

                val puid = this.provideUniqueIdentifier()
                Log.d("MSxxx", "uid = $puid")

                try {
                    val uniqueIdentifier = lazyUniqueIdentifier.get()
                    return FirebaseUserRepository(uniqueIdentifier = uniqueIdentifier)
                }

                catch (e: Exception) {

                    Log.d("MSxxx", "$e")
                    return FirebaseUserRepository(uniqueIdentifier = "null")
                }
            }

            AuthenticationMethod.Room -> {

                val roomDatabase = lazyRoomDatabase.get()
                return RoomUserRepository(roomDatabase = roomDatabase)
            }
        }
    }
}