package com.example.trackies.isSignedIn.addNewTrackie.buisness

data class ActivityStatesOfSegments (
    var nameOfTrackieIsActive: Boolean = false,
    var dailyDoseIsActive: Boolean = false,
    var scheduleDaysIsActive: Boolean = false,
    var timeOfIngestionIsActive: Boolean = false,
)