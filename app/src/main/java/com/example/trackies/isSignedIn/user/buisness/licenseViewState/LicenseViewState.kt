package com.example.trackies.isSignedIn.user.buisness.licenseViewState

data class LicenseViewState(
    var active: Boolean,
    var validUntil: String?,
    var totalAmountOfTrackies: Int
)
