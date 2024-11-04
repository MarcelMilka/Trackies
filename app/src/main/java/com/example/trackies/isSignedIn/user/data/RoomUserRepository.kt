package com.example.trackies.isSignedIn.user.data

import androidx.room.Dao
import com.example.trackies.isSignedIn.user.buisness.LicenseModel
import com.example.trackies.isSignedIn.xTrackie.buisness.TrackieModel
import javax.inject.Inject

@Dao
class RoomUserRepository @Inject constructor(): UserRepository {

    override suspend fun isFirstTimeInTheApp(onFailure: (String) -> Unit): Boolean? {

        // TODO: check if required columns exist
        return null
    }

    override fun addNewUser() {

        // TODO: implement code which adds columns
    }

    override suspend fun needToResetPastWeekActivity(
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ): Boolean? {
        TODO("Not yet implemented")
    }

    override suspend fun resetWeeklyRegularity(
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {}

    override suspend fun fetchUsersLicense(): LicenseModel? {

        // TODO: when LicenseModel.active == true, it means the user's logged in
        return null
    }

    override suspend fun fetchNamesOfTrackies(dayOfWeek: String): List<String>? = null

    override suspend fun fetchNamesOfAllTrackies(): MutableList<String>? = null

    override suspend fun fetchTodayTrackies(): List<TrackieModel>? = null

    override suspend fun addNewTrackie(
        trackieViewState: TrackieModel,
        onFailure: (String) -> Unit
    ) {}

    override suspend fun fetchTrackiesForToday(onFailure: (String) -> Unit): List<TrackieModel>? = null

    override suspend fun fetchStatesOfTrackiesForToday(onFailure: (String) -> Unit): Map<String, Boolean>? = null

    override suspend fun deleteTrackie(
        trackieViewState: TrackieModel,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {}

    override suspend fun fetchAllTrackies(
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ): List<TrackieModel>? = null

    override suspend fun fetchWeeklyRegularity(
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ): Map<String, Map<Int, Int>>? = null

    override suspend fun markTrackieAsIngested(
        trackieViewState: TrackieModel,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {}

    override suspend fun deleteUsersData() {}
}