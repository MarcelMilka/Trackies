package com.example.trackies.isSignedIn.user.buisness

data class LicenseModel(
    var active: Boolean,
    var validUntil: String?,
    var totalAmountOfTrackies: Int
)
