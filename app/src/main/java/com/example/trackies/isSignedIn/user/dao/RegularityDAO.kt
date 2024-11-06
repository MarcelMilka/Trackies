package com.example.trackies.isSignedIn.user.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.trackies.isSignedIn.user.buisness.entities.Regularity

@Dao
interface RegularityDAO {

    @Query("SELECT * FROM regularity WHERE dayOfWeek = :currentDayOfWeek")
    suspend fun fetchStatesOfTrackiesForToday(currentDayOfWeek: String): List<Regularity>?

    @Query("SELECT * FROM regularity")
    suspend fun fetchWeeklyRegularity(): List<Regularity>?

    @Insert()
    suspend fun addTrackieToRegularity(regularity: Regularity)

    @Query("DELETE FROM Regularity WHERE name = :nameOfTrackie")
    suspend fun deleteTrackieFromRegularity(nameOfTrackie: String)

    @Update
    suspend fun markTrackieAsIngested(rowToUpdate: Regularity)

    @Query("UPDATE Regularity SET ingested = 0 WHERE dayOfWeek = :dayOfWeek")
    suspend fun resetRegularity(dayOfWeek: String)
}