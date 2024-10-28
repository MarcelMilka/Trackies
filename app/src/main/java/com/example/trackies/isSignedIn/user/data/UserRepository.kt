package com.example.trackies.isSignedIn.user.data

import com.example.trackies.isSignedIn.xTrackie.buisness.TrackieModel
import com.example.trackies.isSignedIn.user.buisness.licenseViewState.LicenseViewState

interface UserRepository {

    fun firstTimeInTheApp(anErrorOccurred: () -> Unit)

    fun addNewUser()

    suspend fun fetchUsersLicense(): LicenseViewState?

    suspend fun fetchNamesOfTrackies(dayOfWeek: String): List<String>?

    suspend fun fetchNamesOfAllTrackies(): MutableList<String>?

    suspend fun fetchTodayTrackies(): List<TrackieModel>?

    suspend fun addNewTrackie(
        trackieViewState: TrackieModel,
        onFailure: (String) -> Unit
    )

    suspend fun fetchTrackiesForToday(
        onFailure: (String) -> Unit
    ): List<TrackieModel>?

    suspend fun fetchStatesOfTrackiesForToday(
        onFailure: (String) -> Unit
    ): Map<String, Boolean>?

    suspend fun deleteTrackie(
        trackieViewState: TrackieModel,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    )

    suspend fun fetchAllTrackies(
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ): List<TrackieModel>?

    suspend fun fetchWeeklyRegularity(
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ): Map<String, Map<Int, Int>>?

    suspend fun markTrackieAsIngested(
        trackieViewState: TrackieModel,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    )
}