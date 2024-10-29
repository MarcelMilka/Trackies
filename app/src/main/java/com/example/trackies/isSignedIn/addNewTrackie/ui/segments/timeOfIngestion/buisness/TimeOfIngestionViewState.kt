package com.example.trackies.isSignedIn.addNewTrackie.ui.segments.timeOfIngestion.buisness

import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.timeOfIngestion.staticValues.TimeOfIngestionHintOptions
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.timeOfIngestion.staticValues.TimeOfIngestionHeightOptions

data class TimeOfIngestionViewState(

    var targetHeightOfTheSurface: Int = TimeOfIngestionHeightOptions.displayUnactivatedComponent,

    var hint: String = TimeOfIngestionHintOptions.clickToInsertTimeOfIngestion,

    var displayContentInTimeComponent: Boolean = false
)