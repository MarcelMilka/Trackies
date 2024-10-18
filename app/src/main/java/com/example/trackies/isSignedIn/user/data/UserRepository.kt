package com.example.trackies.isSignedIn.user.data

import com.example.trackies.isSignedIn.user.buisness.licenseViewState.LicenseViewState

interface UserRepository {

    fun firstTimeInTheApp(anErrorOccurred: () -> Unit)

    suspend fun fetchUsersLicenseInformation(): LicenseViewState?
}