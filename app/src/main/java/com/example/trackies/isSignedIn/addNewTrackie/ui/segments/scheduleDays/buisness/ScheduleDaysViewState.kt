package com.example.trackies.isSignedIn.addNewTrackie.ui.segments.scheduleDays.buisness

import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.scheduleDays.staticValues.ScheduleDaysHintOptions
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.scheduleDays.staticValues.ScheduleDaysHeightOptions

data class ScheduleDaysViewState(

    var repeatOn: MutableSet<String> = mutableSetOf(),

    var mondayIsSelected: Boolean = false,
    var tuesdayIsSelected: Boolean = false,
    var wednesdayIsSelected: Boolean = false,
    var thursdayIsSelected: Boolean = false,
    var fridayIsSelected: Boolean = false,
    var saturdayIsSelected: Boolean = false,
    var sundayIsSelected: Boolean = false,

    var targetHeightOfTheColumn: Int = ScheduleDaysHeightOptions.displayUnactivatedComponent,
    var targetHeightOfTheSurface: Int = ScheduleDaysHeightOptions.displayUnactivatedComponent,

    var displayFieldWithChosenDaysOfWeek: Boolean = false,
    var displayFieldWithSelectableButtons: Boolean = false,

    var hint: String = ScheduleDaysHintOptions.selectDaysOfWeek
)