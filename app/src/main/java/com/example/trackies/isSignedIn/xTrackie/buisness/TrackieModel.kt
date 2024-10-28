package com.example.trackies.isSignedIn.xTrackie.buisness

data class TrackieModel(
    val name: String,
    val totalDose: Int,
    val measuringUnit: String,
    val repeatOn: List<String>,
    val ingestionTime: Map<String, Int>?
)