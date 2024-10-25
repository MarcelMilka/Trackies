package com.example.trackies.isSignedIn.addNewTrackie.buisness

import com.example.trackies.isSignedIn.addNewTrackie.presentation.timeOfIngestion.loadedSuccessfully.TimeOfIngestionHints
import com.example.trackies.isSignedIn.addNewTrackie.presentation.timeOfIngestion.loadedSuccessfully.TimeOfIngestionSetOfHeights

data class ScheduleTimeViewState(

    var timeOfIngestion: TimeOfIngestion? = null,

    var targetHeightOfTheSurface: Int = TimeOfIngestionSetOfHeights.displayUnactivatedComponent,

    var displayTheButton: Boolean = false, // The button responsible for adding new/another schedule.

    var hint: String = TimeOfIngestionHints.scheduleTimeOfIngestion
)