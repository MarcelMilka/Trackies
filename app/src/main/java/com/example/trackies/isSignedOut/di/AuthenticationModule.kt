package com.example.trackies.isSignedOut.di

import com.example.trackies.isSignedOut.data.AuthenticationService
import com.example.trackies.isSignedOut.data.FirebaseAuthenticationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module()
@InstallIn(SingletonComponent::class)
class AuthenticationModule {

    @Provides
    @Singleton
    fun provideAuthenticationService(): AuthenticationService = FirebaseAuthenticationService
}