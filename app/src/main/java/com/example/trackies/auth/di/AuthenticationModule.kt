package com.example.trackies.auth.di

import android.util.Log
import com.example.trackies.auth.data.AuthenticationService
import com.example.trackies.auth.providerOfAuthenticationMethod.AuthenticationMethodProvider
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module()
@InstallIn(SingletonComponent::class)
class AuthenticationModule {

    @Provides
    fun provideAuthenticationService(
        authenticationMethodProvider: AuthenticationMethodProvider
    ): AuthenticationService {

        Log.d("Halla!", "Authentication service is requested.")

        val authenticationService =
            authenticationMethodProvider.getAuthenticationService()

        return authenticationService
    }
}