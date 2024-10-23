package com.example.trackies.isSignedIn.allTrackies.vm

import androidx.lifecycle.ViewModel
import com.example.trackies.isSignedIn.allTrackies.buisness.WhatToDisplay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AllTrackiesViewModel @Inject constructor(): ViewModel() {

    private var _listToDisplay = MutableStateFlow<WhatToDisplay>(WhatToDisplay.TrackiesForToday)
    val listToDisplay = _listToDisplay.asStateFlow()

    fun switchListToDisplay(listToDisplay: WhatToDisplay) {

        _listToDisplay.update {
            listToDisplay
        }
    }
}