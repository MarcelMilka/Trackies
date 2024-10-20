package com.example.trackies.isSignedIn.addNewTrackie.buisness

import com.example.trackies.isSignedIn.addNewTrackie.presentation.scheduleDays.loadedSuccessfully.ScheduleDaysHints
import com.example.trackies.isSignedIn.addNewTrackie.presentation.scheduleDays.loadedSuccessfully.ScheduleDaysSetOfHeights

data class ScheduleDaysViewState(

    var repeatOn: MutableSet<String> = mutableSetOf(),

    var mondayIsSelected: Boolean = false,
    var tuesdayIsSelected: Boolean = false,
    var wednesdayIsSelected: Boolean = false,
    var thursdayIsSelected: Boolean = false,
    var fridayIsSelected: Boolean = false,
    var saturdayIsSelected: Boolean = false,
    var sundayIsSelected: Boolean = false,

    var targetHeightOfTheColumn: Int = ScheduleDaysSetOfHeights.displayUnactivatedComponent,
    var targetHeightOfTheSurface: Int = ScheduleDaysSetOfHeights.displayUnactivatedComponent,

    var displayFieldWithChosenDaysOfWeek: Boolean = false,
    var displayFieldWithSelectableButtons: Boolean = false,

    var hint: String = ScheduleDaysHints.selectDaysOfWeek
)