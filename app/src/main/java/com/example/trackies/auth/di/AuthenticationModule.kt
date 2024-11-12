package com.example.trackies.auth.di

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
    @Reusable
    fun provideAuthenticationService(
        authenticationMethodProvider: AuthenticationMethodProvider
    ): AuthenticationService {

        val authenticationService =
            authenticationMethodProvider.getAuthenticationService()

        return authenticationService
    }
}