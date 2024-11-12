package com.example.trackies.auth.providerOfAuthenticationMethod

import com.example.trackies.aRoom.db.RoomDatabase
import com.example.trackies.auth.data.AuthenticationService
import com.example.trackies.auth.data.FirebaseAuthenticationService
import com.example.trackies.auth.data.RoomAuthenticationService
import com.example.trackies.di.FirebaseAuthenticator
import com.example.trackies.di.RoomAuthenticator
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AuthenticationMethodProviderModule {

    @Provides
//    @Singleton
    @FirebaseAuthenticator
    fun provideFirebaseAuthenticationService(): AuthenticationService =
        FirebaseAuthenticationService


    @Provides
//    @Singleton
    @RoomAuthenticator
    fun provideRoomAuthenticationService(
        lazyRoomDatabase: Lazy<RoomDatabase>
    ): AuthenticationService {

        val roomDatabase = lazyRoomDatabase.get()

        return RoomAuthenticationService(
            roomDatabase = roomDatabase
        )
    }

    @Provides
    @Singleton
    fun provideAuthenticationMethodProvider(
        @FirebaseAuthenticator firebaseAuthenticationService: AuthenticationService,
        @RoomAuthenticator roomAuthenticationService: AuthenticationService,
    ): AuthenticationMethodProvider =
        AuthenticationMethodProvider(
            firebaseAuthService = firebaseAuthenticationService,
            roomAuthService = roomAuthenticationService
        )
}