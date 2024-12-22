package com.example.trackies.isSignedIn.addNewTrackie.ui.segments.dailyDose.buisness

import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.dailyDose.staticValues.DailyDoseHintOptions
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.dailyDose.staticValues.DailyDoseHeightOptions

data class DailyDoseViewState(

    var targetHeightOfTheSurface: Int = DailyDoseHeightOptions.displayUnactivatedSegment,

    var displayFieldWithInsertedDose: Boolean = false,
    var displayFieldWithMeasuringUnits: Boolean = false,
    var displayFieldWithTextField: Boolean = false,
    var hint: String = DailyDoseHintOptions.insertDailyDose,

    var error: Boolean = false
)