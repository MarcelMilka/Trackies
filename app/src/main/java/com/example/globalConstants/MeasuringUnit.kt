package com.example.globalConstants

enum class MeasuringUnit {
    ml,
    g,
    pcs,
}

fun MeasuringUnit.turnIntoString(): String = this.toString()