package com.example.trackies.isSignedIn.homeScreen.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.trackies.isSignedIn.user.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private var repository: UserRepository
): ViewModel() {

    init {
        repository.firstTimeInTheApp {}
    }
}