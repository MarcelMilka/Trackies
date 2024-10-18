package com.example.trackies.isSignedIn.homeScreen.viewState

import com.example.trackies.isSignedIn.trackie.TrackieViewState
import com.example.trackies.isSignedIn.user.buisness.licenseViewState.LicenseViewState

sealed class HomeScreenViewState {

    object Loading: HomeScreenViewState()

    data class LoadedSuccessfully(
        var license: LicenseViewState,
//        var trackiesForToday: List<TrackieViewState>,
//        var namesOfAllTrackies: List<String>,
//        var allTrackies: List<TrackieViewState>?,
//        var statesOfTrackiesForToday: Map<String,Boolean>,
//        var weeklyRegularity: Map<String, Map<Int, Int>>
    ): HomeScreenViewState()

    object FailedToLoadData: HomeScreenViewState()
}
