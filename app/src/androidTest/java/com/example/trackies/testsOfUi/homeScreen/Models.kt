package com.example.trackies.testsOfUi.homeScreen

import com.example.globalConstants.DaysOfWeek
import com.example.trackies.isSignedIn.xTrackie.buisness.TrackieModel

// This object serves as container of instances used during unit and instrumented tests.

object Models {

    val wholeWeekTrackieModel1 = TrackieModel(
        name = "A",
        totalDose = 3000,
        measuringUnit = "ml",
        repeatOn = listOf(DaysOfWeek.monday, DaysOfWeek.tuesday, DaysOfWeek.wednesday, DaysOfWeek.thursday, DaysOfWeek.friday, DaysOfWeek.saturday, DaysOfWeek.sunday),
        ingestionTime = null
    )

    val wholeWeekTrackieModel2 = TrackieModel(
        name = "B",
        totalDose = 2444,
        measuringUnit = "g",
        repeatOn = listOf(DaysOfWeek.monday, DaysOfWeek.tuesday, DaysOfWeek.wednesday, DaysOfWeek.thursday, DaysOfWeek.friday, DaysOfWeek.saturday, DaysOfWeek.sunday),
        ingestionTime = null
    )

    val wholeWeekTrackieModel3 = TrackieModel(
        name = "C",
        totalDose = 242,
        measuringUnit = "pcs",
        repeatOn = listOf(DaysOfWeek.monday, DaysOfWeek.tuesday, DaysOfWeek.wednesday, DaysOfWeek.thursday, DaysOfWeek.friday, DaysOfWeek.saturday, DaysOfWeek.sunday),
        ingestionTime = null
    )

    val wholeWeekTrackieModel4 = TrackieModel(
        name = "D",
        totalDose = 244234,
        measuringUnit = "ml",
        repeatOn = listOf(DaysOfWeek.monday, DaysOfWeek.tuesday, DaysOfWeek.wednesday, DaysOfWeek.thursday, DaysOfWeek.friday, DaysOfWeek.saturday, DaysOfWeek.sunday),
        ingestionTime = null
    )

    val wholeWeekTrackieModel5 = TrackieModel(
        name = "E",
        totalDose = 3453,
        measuringUnit = "ml",
        repeatOn = listOf(DaysOfWeek.monday, DaysOfWeek.tuesday, DaysOfWeek.wednesday, DaysOfWeek.thursday, DaysOfWeek.friday, DaysOfWeek.saturday, DaysOfWeek.sunday),
        ingestionTime = null
    )

    val wholeWeekTrackieModel6 = TrackieModel(
        name = "F",
        totalDose = 1231,
        measuringUnit = "ml",
        repeatOn = listOf(DaysOfWeek.monday, DaysOfWeek.tuesday, DaysOfWeek.wednesday, DaysOfWeek.thursday, DaysOfWeek.friday, DaysOfWeek.saturday, DaysOfWeek.sunday),
        ingestionTime = null
    )
}