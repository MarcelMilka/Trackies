package com.example.trackies.isSignedIn.addNewTrackie.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackies.isSignedIn.user.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNewTrackieViewModel @Inject constructor(
    private var userRepository: UserRepository
): ViewModel() {

    init {
        viewModelScope.launch {
            val it = userRepository.fetchNamesOfAllTrackies()
            Log.d("Halla!", "$it")
        }
    }
}