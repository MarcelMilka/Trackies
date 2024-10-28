package com.example.trackies.isSignedIn.detailedTrackie.vm

import androidx.lifecycle.ViewModel
import com.example.trackies.isSignedIn.xTrackie.buisness.TrackieModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DetailedTrackieViewModel: ViewModel() {

    private var _uiState = MutableStateFlow<TrackieModel?>(null)
    val uiState = _uiState.asStateFlow()

    fun setTrackieToDisplayDetailsOf(trackieViewState: TrackieModel) {

        _uiState.update {
            trackieViewState
        }
    }
}