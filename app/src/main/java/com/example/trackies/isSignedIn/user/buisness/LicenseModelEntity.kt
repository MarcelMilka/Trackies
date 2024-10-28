package com.example.trackies.isSignedIn.user.buisness

data class LicenseModelEntity(
    val active: Boolean? = null,
    val validUntil: String? = null,
    val totalAmountOfTrackies: Int? = null
)

fun LicenseModelEntity.convertEntityToLicenseModel(): LicenseModel =
    LicenseModel(
        active = this.active!!,
        validUntil = this.validUntil,
        totalAmountOfTrackies = this.totalAmountOfTrackies!!
    )