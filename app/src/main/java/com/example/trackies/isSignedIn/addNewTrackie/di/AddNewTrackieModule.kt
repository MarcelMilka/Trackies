package com.example.trackies.isSignedIn.addNewTrackie.di

import com.example.trackies.isSignedIn.addNewTrackie.vm.AddNewTrackieViewModel
import com.example.trackies.isSignedIn.user.data.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AddNewTrackieModule {

    @Provides
    fun provideAddNewTrackieViewModel(repository: UserRepository): AddNewTrackieViewModel = AddNewTrackieViewModel(repository = repository)
}