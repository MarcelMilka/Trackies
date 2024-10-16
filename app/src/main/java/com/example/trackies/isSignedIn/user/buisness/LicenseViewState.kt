package com.example.trackies.isSignedIn.user.buisness

data class LicenseViewState(
    var active: Boolean,
    var validUntil: String?,
    var totalAmountOfTrackies: Int
)
