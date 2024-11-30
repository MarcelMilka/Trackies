package com.example.trackies.isSignedIn.user.data

import com.example.trackies.isSignedIn.xTrackie.buisness.TrackieModel
import com.example.trackies.isSignedIn.user.buisness.LicenseModel

interface UserRepository {

    suspend fun isFirstTimeInTheApp(): Boolean?

    fun addNewUser()

    suspend fun needToResetPastWeekActivity(): Boolean?

    suspend fun resetWeeklyRegularity(
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    )

    suspend fun fetchUsersLicense(): LicenseModel?

    suspend fun fetchNamesOfTrackies(dayOfWeek: String): List<String>?

    suspend fun fetchNamesOfAllTrackies(): MutableList<String>?

    suspend fun fetchTodayTrackies(): List<TrackieModel>?

    suspend fun addNewTrackie(
        trackieModel: TrackieModel,
        onFailure: (String) -> Unit
    )

    suspend fun fetchTrackiesForToday(
        onFailure: (String) -> Unit
    ): List<TrackieModel>?

    suspend fun fetchStatesOfTrackiesForToday(
        onFailure: (String) -> Unit
    ): Map<String, Boolean>?

    suspend fun deleteTrackie(
        trackieModel: TrackieModel,
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
        currentDayOfWeek: String,
        trackieModel: TrackieModel,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    )

    suspend fun deleteUsersData()
}