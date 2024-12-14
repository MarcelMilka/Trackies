package com.example.globalConstants

enum class MeasuringUnit {
    ml,
    g,
    pcs
}

fun MeasuringUnit.turnMeasuringUnitToString(): String = this.toString()

fun String.turnStringToMeasuringUnit(): MeasuringUnit =
    when(this) {

        "ml" -> MeasuringUnit.ml

        "g" -> MeasuringUnit.g

        "pcs" -> MeasuringUnit.pcs

        else -> MeasuringUnit.g
    }