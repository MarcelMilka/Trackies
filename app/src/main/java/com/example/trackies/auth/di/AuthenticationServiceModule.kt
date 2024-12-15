package com.example.trackies.auth.di

import com.example.trackies.auth.data.AuthenticationService
import com.example.trackies.auth.authenticationMethodProvider.AuthenticationMethodProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module()
@InstallIn(SingletonComponent::class)
class AuthenticationServiceModule {

    @Provides
    fun provideAuthenticationService(
        authenticationMethodProvider: AuthenticationMethodProvider
    ): AuthenticationService {

        val authenticationService =
            authenticationMethodProvider.getAuthenticationService()

        return authenticationService
    }
}