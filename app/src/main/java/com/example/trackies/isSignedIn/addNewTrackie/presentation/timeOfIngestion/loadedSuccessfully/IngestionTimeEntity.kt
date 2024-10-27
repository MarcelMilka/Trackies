package com.example.trackies.isSignedIn.addNewTrackie.presentation.timeOfIngestion.loadedSuccessfully

import com.example.trackies.isSignedIn.addNewTrackie.buisness.IngestionTime

data class IngestionTimeEntity(
    val hour: Int,
    val minute: Int
)

fun IngestionTimeEntity.convertIntoTimeOfIngestion(): IngestionTime {

    val hour =
        if (this.hour.toString().length == 1) {
            "0${this.hour}"
        }
        else {
            "${this.hour}"
        }

    val minute =
        if (this.minute.toString().length == 1) {
            "0${this.minute}"
        }
        else {
            "${this.minute}"
        }

    return IngestionTime(
        hour = hour,
        minute = minute
    )
}
