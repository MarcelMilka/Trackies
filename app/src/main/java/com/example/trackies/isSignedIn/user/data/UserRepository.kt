package com.example.trackies.isSignedIn.user.data

import com.example.trackies.isSignedIn.trackie.TrackieViewState
import com.example.trackies.isSignedIn.user.buisness.licenseViewState.LicenseViewState

interface UserRepository {

    fun firstTimeInTheApp(anErrorOccurred: () -> Unit)

    fun addNewUser()

    suspend fun fetchUsersLicense(): LicenseViewState?

    suspend fun fetchNamesOfTrackies(dayOfWeek: String): List<String>?

    suspend fun fetchNamesOfAllTrackies(): MutableList<String>?

    suspend fun fetchTodayTrackies(): List<TrackieViewState>?

    suspend fun addNewTrackie(
        trackieViewState: TrackieViewState,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    )

    suspend fun fetchTrackiesForToday(
        onFailure: (String) -> Unit
    ): List<TrackieViewState>?

    suspend fun fetchStatesOfTrackiesForToday(
        onFailure: (String) -> Unit
    ): Map<String, Boolean>?

    suspend fun deleteTrackie(
        trackieViewState: TrackieViewState,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    )
}