package com.example.trackies.isSignedIn.xTrackie.buisness

import com.example.globalConstants.DaysOfWeek
import com.example.globalConstants.MeasuringUnit
import com.example.trackies.isSignedIn.user.buisness.entities.Trackie

data class TrackieModel(
    val name: String,
    val totalDose: Int,
    val measuringUnit: MeasuringUnit,
    val repeatOn: List<String>,
    val ingestionTime: Map<String, Int>?
)

fun TrackieModel.convertTrackieModelToTrackieEntity(): Trackie =

    Trackie(
        name = this.name,
        totalDose = this.totalDose,
        measuringUnit = this.measuringUnit,
        monday = this.repeatOn.contains(DaysOfWeek.monday),
        tuesday = this.repeatOn.contains(DaysOfWeek.tuesday),
        wednesday = this.repeatOn.contains(DaysOfWeek.wednesday),
        thursday = this.repeatOn.contains(DaysOfWeek.thursday),
        friday = this.repeatOn.contains(DaysOfWeek.friday),
        saturday = this.repeatOn.contains(DaysOfWeek.saturday),
        sunday = this.repeatOn.contains(DaysOfWeek.sunday)
    )