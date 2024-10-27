package com.example.trackies.isSignedIn.addNewTrackie.buisness

import com.example.trackies.isSignedIn.addNewTrackie.presentation.timeOfIngestion.loadedSuccessfully.TimeOfIngestionHints
import com.example.trackies.isSignedIn.addNewTrackie.presentation.timeOfIngestion.loadedSuccessfully.TimeOfIngestionSetOfHeights

data class ScheduleTimeViewState(

    var targetHeightOfTheSurface: Int = TimeOfIngestionSetOfHeights.displayUnactivatedComponent,

    var hint: String = TimeOfIngestionHints.clickToInsertTimeOfIngestion,

    var ingestionTime: IngestionTime? = null,

    var displayContentInTimeComponent: Boolean = false
)