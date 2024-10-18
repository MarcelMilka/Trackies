package com.example.trackies.isSignedIn.trackie

data class TrackieViewStateEntity(
    val name: String? = null,
    val totalDose: Int? = null,
    val measuringUnit: String? = null,
    val repeatOn: List<String>? = null,
    val ingestionTime: Map<String, Int>? = null
)

fun TrackieViewStateEntity.convertEntityToTrackieViewState(): TrackieViewState =
    TrackieViewState(
        name = this.name!!,
        totalDose = this.totalDose!!,
        measuringUnit = this.measuringUnit!!,
        repeatOn = this.repeatOn!!,
        ingestionTime = this.ingestionTime
    )
