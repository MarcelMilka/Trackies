package com.example.trackies.isSignedIn.addNewTrackie.buisness

import com.example.globalConstants.MeasuringUnit
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.timeOfIngestion.buisness.TimeOfIngestion
import com.example.trackies.isSignedIn.xTrackie.buisness.TrackieModel

data class AddNewTrackieModel(
    var name: String = "",
    var dose: Int = 0,
    var measuringUnit: MeasuringUnit? = null,
    var repeatOn: Set<String> = setOf<String>(),
    var ingestionTime: TimeOfIngestion? = null
)

fun AddNewTrackieModel.convertIntoTrackieModel(): TrackieModel {

    val measuringUnit = this.measuringUnit ?: MeasuringUnit.g

    return TrackieModel(
        name = this.name,
        totalDose = this.dose,
        measuringUnit = measuringUnit,
        repeatOn = this.repeatOn.toList(),
        ingestionTime = null
    )
}