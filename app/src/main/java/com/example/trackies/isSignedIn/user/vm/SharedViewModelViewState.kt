package com.example.trackies.isSignedIn.user.vm

import com.example.trackies.isSignedIn.xTrackie.buisness.TrackieModel
import com.example.trackies.isSignedIn.user.buisness.licenseViewState.LicenseViewState

sealed class SharedViewModelViewState {

    object Loading: SharedViewModelViewState()

    data class LoadedSuccessfully(
        var license: LicenseViewState,
        var trackiesForToday: List<TrackieModel>,
        var statesOfTrackiesForToday: Map<String,Boolean>,
        var weeklyRegularity: Map<String, Map<Int, Int>>,
        var namesOfAllTrackies: MutableList<String>?,
        var allTrackies: List<TrackieModel>?
    ): SharedViewModelViewState()

    object FailedToLoadData: SharedViewModelViewState()
}
