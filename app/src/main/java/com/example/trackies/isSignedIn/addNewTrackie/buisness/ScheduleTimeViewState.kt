package com.example.trackies.isSignedIn.addNewTrackie.buisness

import com.example.trackies.isSignedIn.addNewTrackie.presentation.scheduleDays.loadedSuccessfully.ScheduleDaysHints
import com.example.trackies.isSignedIn.addNewTrackie.presentation.scheduleDays.loadedSuccessfully.ScheduleDaysSetOfHeights
import com.example.trackies.isSignedIn.addNewTrackie.presentation.timeOfIngestion.loadedSuccessfully.TimeOfIngestionHints

data class ScheduleTimeViewState(

    var targetHeightOfTheColumn: Int = ScheduleDaysSetOfHeights.displayUnactivatedComponent,

    var displayFieldWithButton: Boolean = false,

    var hint: String = TimeOfIngestionHints.scheduleTimeOfIngestion
)