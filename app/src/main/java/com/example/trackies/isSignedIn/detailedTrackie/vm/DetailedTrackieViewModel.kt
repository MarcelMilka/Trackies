package com.example.trackies.isSignedIn.detailedTrackie.vm

import androidx.lifecycle.ViewModel
import com.example.trackies.isSignedIn.trackie.TrackieViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DetailedTrackieViewModel: ViewModel() {

    private var _uiState = MutableStateFlow<TrackieViewState?>(null)
    val uiState = _uiState.asStateFlow()

    fun setTrackieToDisplayDetailsOf(trackieViewState: TrackieViewState) {

        _uiState.update {
            trackieViewState
        }
    }
}