package com.example.trackies.isSignedIn.user.buisness.licenseViewState

data class LicenseViewStateEntity(
    val active: Boolean? = null,
    val validUntil: String? = null,
    val totalAmountOfTrackies: Int? = null
)

fun LicenseViewStateEntity.convertEntityToLicenseViewState(): LicenseViewState =
    LicenseViewState(
        active = this.active!!,
        validUntil = this.validUntil,
        totalAmountOfTrackies = this.totalAmountOfTrackies!!
    )