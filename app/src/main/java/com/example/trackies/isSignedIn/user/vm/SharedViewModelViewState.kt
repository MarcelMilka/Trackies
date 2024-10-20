package com.example.trackies.isSignedIn.user.vm

import com.example.trackies.isSignedIn.user.buisness.licenseViewState.LicenseViewState

sealed class SharedViewModelViewState {

    object Loading: SharedViewModelViewState()

    data class LoadedSuccessfully(
        var license: LicenseViewState,
        var namesOfAllTrackies: MutableList<String>?,
    ): SharedViewModelViewState()

    object FailedToLoadData: SharedViewModelViewState()
}
