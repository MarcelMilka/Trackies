package com.example.trackies.isSignedIn.addNewTrackie.buisness

import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.dailyDose.buisness.EnumMeasuringUnits

data class AddNewTrackieModel(
    var name: String = "",
    var dose: Int = 0,
    var measuringUnit: EnumMeasuringUnits? = null,
    var repeatOn: MutableList<String> = mutableListOf<String>(),
    var ingestionTime: MutableMap<String, Int>? = null
)