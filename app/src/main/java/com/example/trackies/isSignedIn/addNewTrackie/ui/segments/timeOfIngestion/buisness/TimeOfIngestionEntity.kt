package com.example.trackies.isSignedIn.addNewTrackie.ui.segments.timeOfIngestion.buisness

data class TimeOfIngestionEntity(
    val hour: Int,
    val minute: Int
)

fun TimeOfIngestionEntity.convertIntoTimeOfIngestion(): TimeOfIngestion {

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

    return TimeOfIngestion(
        hour = hour,
        minute = minute
    )
}