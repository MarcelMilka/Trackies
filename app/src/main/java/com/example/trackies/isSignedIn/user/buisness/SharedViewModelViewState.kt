package com.example.trackies.isSignedIn.user.buisness

import com.example.trackies.isSignedIn.xTrackie.buisness.TrackieModel

sealed class SharedViewModelViewState {

    object Loading: SharedViewModelViewState()

    data class LoadedSuccessfully(
        var license: LicenseModel,
        var trackiesForToday: List<TrackieModel>,
        var statesOfTrackiesForToday: Map<String,Boolean>,
        var weeklyRegularity: Map<String, Map<Int, Int>>,
        var namesOfAllTrackies: MutableList<String>?,
        var allTrackies: List<TrackieModel>?
    ): SharedViewModelViewState()

    object FailedToLoadData: SharedViewModelViewState()
}
