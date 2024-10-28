package com.example.globalConstants

import java.time.LocalDate
import java.time.LocalTime

object CurrentTime {

    fun getCurrentDayOfWeek(): String {

        val localDate = LocalDate.now()
        return localDate.dayOfWeek.toString().lowercase()
    }

    fun getCurrentDate(): String {

        val localDate = LocalDate.now()

        val month = localDate.month
        val dayOfMonth = localDate.dayOfMonth
        val year = localDate.year
        return "$dayOfMonth.$month.$year"
    }

    fun getCurrentTime(): String{

        val localTime = LocalTime.now()
        val hour = if (localTime.hour in 0..9) {"0${localTime.hour}"} else {localTime.hour}
        val minute = if (localTime.minute in 0..9) {"0${localTime.minute}"} else {localTime.minute}
        return "$hour:$minute"
    }
}