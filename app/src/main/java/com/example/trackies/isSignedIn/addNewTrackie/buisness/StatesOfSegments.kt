package com.example.trackies.isSignedIn.addNewTrackie.buisness

data class StatesOfSegments (
    var insertNameIsActive: Boolean = false,
    var insertTotalDoseIsActive: Boolean = false,
    var scheduleDaysIsActive: Boolean = false,
    var timeOfIngestionIsActive: Boolean = false,
)