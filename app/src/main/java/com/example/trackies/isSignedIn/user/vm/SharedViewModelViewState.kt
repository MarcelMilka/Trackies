package com.example.trackies.isSignedIn.user.vm

import com.example.trackies.isSignedIn.user.buisness.licenseViewState.LicenseViewState

sealed class SharedViewModelViewState {

    object Loading: SharedViewModelViewState()

    data class LoadedSuccessfully(
        var license: LicenseViewState,
//        var trackiesForToday: List<TrackieViewState>,
        var namesOfAllTrackies: List<String>?,
//        var allTrackies: List<TrackieViewState>?,
//        var statesOfTrackiesForToday: Map<String,Boolean>,
//        var weeklyRegularity: Map<String, Map<Int, Int>>
    ): SharedViewModelViewState()

    object FailedToLoadData: SharedViewModelViewState()
}
