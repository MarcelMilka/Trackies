package com.example.trackies.isSignedIn.user.buisness.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.globalConstants.Room
import com.example.trackies.isSignedIn.user.buisness.LicenseModel

@Entity(tableName = "License")
data class License(
    @PrimaryKey val first: Int,
    var isSignedIn: Boolean,
    var totalAmountOfTrackies: Int
)

fun License.convertLicenseToLicenseModel(): LicenseModel =

    LicenseModel(
        active = false,
        validUntil = null,
        totalAmountOfTrackies = this.totalAmountOfTrackies
    )