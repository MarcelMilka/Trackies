package com.example.trackies.isSignedIn.user.vm

import com.example.trackies.isSignedIn.trackie.TrackieViewState
import com.example.trackies.isSignedIn.user.buisness.licenseViewState.LicenseViewState

sealed class SharedViewModelViewState {

    object Loading: SharedViewModelViewState()

    data class LoadedSuccessfully(
        var license: LicenseViewState,
        var trackiesForToday: List<TrackieViewState>,
        var statesOfTrackiesForToday: Map<String,Boolean>,
        var namesOfAllTrackies: MutableList<String>?,
        var allTrackies: List<TrackieViewState>?,
    ): SharedViewModelViewState()

    object FailedToLoadData: SharedViewModelViewState()
}
