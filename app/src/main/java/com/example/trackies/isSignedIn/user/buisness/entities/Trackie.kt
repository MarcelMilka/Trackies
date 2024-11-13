package com.example.trackies.isSignedIn.user.buisness.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.globalConstants.DaysOfWeek
import com.example.trackies.isSignedIn.xTrackie.buisness.TrackieModel

@Entity(tableName = "Trackies")
data class Trackie(
    @PrimaryKey val name: String,
    val totalDose: Int,
    val measuringUnit: String,
    val monday: Boolean,
    val tuesday: Boolean,
    val wednesday: Boolean,
    val thursday: Boolean,
    val friday: Boolean,
    val saturday: Boolean,
    val sunday: Boolean,
)

fun Trackie.convertLicenseToLicenseModel(): TrackieModel {

    var repeatOn = mutableListOf<String>()

    if (this.monday) {

        repeatOn.add(DaysOfWeek.monday)
    }
    if (this.tuesday) {

        repeatOn.add(DaysOfWeek.tuesday)
    }
    if (this.wednesday) {

        repeatOn.add(DaysOfWeek.wednesday)
    }
    if (this.thursday) {

        repeatOn.add(DaysOfWeek.thursday)
    }
    if (this.friday) {

        repeatOn.add(DaysOfWeek.friday)
    }
    if (this.saturday) {

        repeatOn.add(DaysOfWeek.saturday)
    }
    if (this.sunday) {

        repeatOn.add(DaysOfWeek.sunday)
    }

    return TrackieModel(
        name = this.name,
        totalDose = this.totalDose,
        measuringUnit = this.measuringUnit,
        repeatOn = repeatOn.toList(),
        ingestionTime = null

    )
}