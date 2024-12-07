package com.example.trackies.isSignedIn.user.data

import com.example.trackies.isSignedIn.xTrackie.buisness.TrackieModel
import com.example.trackies.isSignedIn.user.buisness.LicenseModel

interface UserRepository {

    suspend fun isFirstTimeInTheApp(): Boolean?

    fun addNewUser()

    suspend fun needToResetPastWeekRegularity(): Boolean?

    suspend fun resetWeeklyRegularity(): Boolean

    suspend fun fetchUsersLicense(): LicenseModel?

    suspend fun fetchNamesOfTrackies(dayOfWeek: String): List<String>?

    suspend fun fetchNamesOfAllTrackies(): MutableList<String>?

    suspend fun fetchTodayTrackies(): List<TrackieModel>?

    suspend fun addNewTrackie(trackieModel: TrackieModel): Boolean

    suspend fun fetchTrackiesForToday(currentDayOfWeek: String): List<TrackieModel>?

    suspend fun fetchStatesOfTrackiesForToday(currentDayOfWeek: String): Map<String, Boolean>?

    suspend fun deleteTrackie(trackieModel: TrackieModel): Boolean

    suspend fun fetchAllTrackies(): List<TrackieModel>?

    suspend fun fetchWeeklyRegularity(): Map<String, Map<Int, Int>>?

    suspend fun markTrackieAsIngested(
        currentDayOfWeek: String,
        trackieModel: TrackieModel
    ): Boolean

    suspend fun deleteUsersData()
}