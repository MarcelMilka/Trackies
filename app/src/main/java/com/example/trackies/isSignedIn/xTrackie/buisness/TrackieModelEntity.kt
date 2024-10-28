package com.example.trackies.isSignedIn.xTrackie.buisness

data class TrackieModelEntity(
    val name: String? = null,
    val totalDose: Int? = null,
    val measuringUnit: String? = null,
    val repeatOn: List<String>? = null,
    val ingestionTime: Map<String, Int>? = null
)

fun TrackieModelEntity.convertEntityToTrackieModel(): TrackieModel =
    TrackieModel(
        name = this.name!!,
        totalDose = this.totalDose!!,
        measuringUnit = this.measuringUnit!!,
        repeatOn = this.repeatOn!!,
        ingestionTime = this.ingestionTime
    )
