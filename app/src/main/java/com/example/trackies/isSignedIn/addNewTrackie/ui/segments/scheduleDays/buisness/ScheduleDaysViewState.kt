package com.example.trackies.isSignedIn.addNewTrackie.ui.segments.scheduleDays.buisness

import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.scheduleDays.staticValues.ScheduleDaysHintOptions
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.scheduleDays.staticValues.ScheduleDaysHeightOptions

data class ScheduleDaysViewState(

    var targetHeightOfTheSurface: Int = ScheduleDaysHeightOptions.displayDeactivated,

    var displayScheduledDaysOfWeek: Boolean = false,
    var displayChips: Boolean = false,

    var hint: String = ScheduleDaysHintOptions.scheduleDaysOfIngestion
)