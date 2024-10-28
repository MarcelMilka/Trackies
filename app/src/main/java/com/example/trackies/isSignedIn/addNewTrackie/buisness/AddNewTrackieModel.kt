package com.example.trackies.isSignedIn.addNewTrackie.buisness

data class AddNewTrackieModel(
    var name: String = "",
    var totalDose: Int = 0,
    var measuringUnit: String = "",
    var repeatOn: MutableList<String> = mutableListOf<String>(),
    var ingestionTime: MutableMap<String, Int>? = null
)