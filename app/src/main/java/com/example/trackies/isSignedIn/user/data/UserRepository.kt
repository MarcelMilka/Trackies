package com.example.trackies.isSignedIn.user.data

import com.example.trackies.isSignedIn.trackie.TrackieViewState
import com.example.trackies.isSignedIn.user.buisness.licenseViewState.LicenseViewState
import kotlinx.coroutines.flow.MutableStateFlow

interface UserRepository {

    fun firstTimeInTheApp(anErrorOccurred: () -> Unit)

    fun addNewUser()

    suspend fun fetchUsersLicense(): LicenseViewState?

    suspend fun fetchNamesOfTrackies(dayOfWeek: String): List<String>?

    suspend fun fetchNamesOfAllTrackies(): MutableList<String>?

    suspend fun fetchTodayTrackies(): List<TrackieViewState>?
}