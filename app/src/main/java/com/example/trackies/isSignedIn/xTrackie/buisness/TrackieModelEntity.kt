package com.example.trackies.isSignedIn.xTrackie.buisness

import com.example.globalConstants.MeasuringUnit

data class TrackieModelEntity(
    val name: String? = null,
    val totalDose: Int? = null,
    val measuringUnit: MeasuringUnit? = null,
    val repeatOn: List<String>? = null,
    val ingestionTime: Map<String, Int>? = null
)

fun TrackieModelEntity.convertEntityToTrackieModel(): TrackieModel {

    return TrackieModel(
        name = this.name!!,
        totalDose = this.totalDose!!,
        measuringUnit = this.measuringUnit!!,
        repeatOn = this.repeatOn!!,
        ingestionTime = this.ingestionTime
    )
}

