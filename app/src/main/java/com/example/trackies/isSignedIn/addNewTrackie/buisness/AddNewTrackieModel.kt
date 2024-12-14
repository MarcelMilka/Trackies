package com.example.trackies.isSignedIn.addNewTrackie.buisness

import com.example.globalConstants.MeasuringUnit
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.timeOfIngestion.buisness.TimeOfIngestion
import com.example.trackies.isSignedIn.xTrackie.buisness.TrackieModel

data class AddNewTrackieModel(
    var name: String = "",
    var dose: Int = 0,
    var measuringUnit: MeasuringUnit? = null,
    var repeatOn: MutableSet<String> = mutableSetOf<String>(),
    var ingestionTime: TimeOfIngestion? = null
)

fun AddNewTrackieModel.convertIntoTrackieModel(): TrackieModel =

    TrackieModel(
        name = this.name,
        totalDose = this.dose,
        measuringUnit = this.measuringUnit ?: MeasuringUnit.g,
        repeatOn = this.repeatOn.toList(),
        ingestionTime = null
    )