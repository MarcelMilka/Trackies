package com.example.trackies.isSignedIn.user.buisness.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    primaryKeys = ["name", "dayOfWeek"]
)
data class Regularity(
    val name: String,
    val dayOfWeek: String,
    var ingested: Boolean
)