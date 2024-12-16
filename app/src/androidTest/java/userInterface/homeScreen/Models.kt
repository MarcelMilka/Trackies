package userInterface.homeScreen

import com.example.globalConstants.DaysOfWeek
import com.example.globalConstants.MeasuringUnit
import com.example.trackies.isSignedIn.xTrackie.buisness.TrackieModel

// This object serves as container of instances used during unit and instrumented tests.

object Models {

    val wholeWeekTrackieModel1 = TrackieModel(
        name = "A",
        totalDose = 3000,
        measuringUnit = MeasuringUnit.ml,
        repeatOn = listOf(DaysOfWeek.monday, DaysOfWeek.tuesday, DaysOfWeek.wednesday, DaysOfWeek.thursday, DaysOfWeek.friday, DaysOfWeek.saturday, DaysOfWeek.sunday),
        ingestionTime = null
    )

    val wholeWeekTrackieModel2 = TrackieModel(
        name = "B",
        totalDose = 2444,
        measuringUnit = MeasuringUnit.g,
        repeatOn = listOf(DaysOfWeek.monday, DaysOfWeek.tuesday, DaysOfWeek.wednesday, DaysOfWeek.thursday, DaysOfWeek.friday, DaysOfWeek.saturday, DaysOfWeek.sunday),
        ingestionTime = null
    )

    val wholeWeekTrackieModel3 = TrackieModel(
        name = "C",
        totalDose = 242,
        measuringUnit = MeasuringUnit.pcs,
        repeatOn = listOf(DaysOfWeek.monday, DaysOfWeek.tuesday, DaysOfWeek.wednesday, DaysOfWeek.thursday, DaysOfWeek.friday, DaysOfWeek.saturday, DaysOfWeek.sunday),
        ingestionTime = null
    )

    val wholeWeekTrackieModel4 = TrackieModel(
        name = "D",
        totalDose = 244234,
        measuringUnit = MeasuringUnit.ml,
        repeatOn = listOf(DaysOfWeek.monday, DaysOfWeek.tuesday, DaysOfWeek.wednesday, DaysOfWeek.thursday, DaysOfWeek.friday, DaysOfWeek.saturday, DaysOfWeek.sunday),
        ingestionTime = null
    )

    val wholeWeekTrackieModel5 = TrackieModel(
        name = "E",
        totalDose = 3453,
        measuringUnit = MeasuringUnit.ml,
        repeatOn = listOf(DaysOfWeek.monday, DaysOfWeek.tuesday, DaysOfWeek.wednesday, DaysOfWeek.thursday, DaysOfWeek.friday, DaysOfWeek.saturday, DaysOfWeek.sunday),
        ingestionTime = null
    )

    val weekendTrackieModel1 = TrackieModel(
        name = "Aaa",
        totalDose = 1231,
        measuringUnit = MeasuringUnit.pcs,
        repeatOn = listOf(DaysOfWeek.saturday, DaysOfWeek.sunday),
        ingestionTime = null
    )

    val weekendTrackieModel2 = TrackieModel(
        name = "Bbb",
        totalDose = 4564,
        measuringUnit = MeasuringUnit.pcs,
        repeatOn = listOf(DaysOfWeek.saturday, DaysOfWeek.sunday),
        ingestionTime = null
    )

    val weekendTrackieModel3 = TrackieModel(
        name = "Ccc",
        totalDose = 4564,
        measuringUnit = MeasuringUnit.pcs,
        repeatOn = listOf(DaysOfWeek.saturday, DaysOfWeek.sunday),
        ingestionTime = null
    )

    val fridayTrackieModel = TrackieModel(
        name = "Ddd",
        totalDose = 222,
        measuringUnit = MeasuringUnit.g,
        repeatOn = listOf(DaysOfWeek.friday),
        ingestionTime = null
    )
}