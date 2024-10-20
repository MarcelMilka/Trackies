package com.example.trackies.isSignedIn.addNewTrackie.buisness

import com.example.trackies.isSignedIn.addNewTrackie.presentation.insertDailyDosage.loadedSuccessfully.DailyDosageHints
import com.example.trackies.isSignedIn.addNewTrackie.presentation.insertDailyDosage.loadedSuccessfully.InsertDailyDosageFixedHeightValues

data class InsertDailyDosageViewState(
    var measuringUnit: EnumMeasuringUnits? = null,
    var totalDailyDose: Int = 1,

    var mlIsChosen: Boolean = false,
    var gIsChosen: Boolean = false,
    var pcsIsChosen: Boolean = false,

    var targetHeightOfTheColumn: Int = InsertDailyDosageFixedHeightValues.displayUnactivatedComponent,
    var targetHeightOfTheSurface: Int = InsertDailyDosageFixedHeightValues.displayUnactivatedComponent,

    var displayFieldWithInsertedDose: Boolean = false,
    var displayFieldWithMeasuringUnits: Boolean = false,
    var displayFieldWithTextField: Boolean = false,
    var hint: String = DailyDosageHints.insertDailyDosage,

    var error: Boolean = false
)