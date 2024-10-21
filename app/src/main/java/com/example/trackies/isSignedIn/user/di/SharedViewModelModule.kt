package com.example.trackies.isSignedIn.user.di

import com.example.trackies.isSignedIn.user.data.UserRepository
import com.example.trackies.isSignedIn.user.vm.SharedViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SharedViewModelModule {

    @Provides
    @Singleton
    fun provideSharedViewModel(repository: UserRepository): SharedViewModel = SharedViewModel(repository)
}