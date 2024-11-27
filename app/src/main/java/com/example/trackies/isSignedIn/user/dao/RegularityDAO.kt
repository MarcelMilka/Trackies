package com.example.trackies.isSignedIn.user.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.globalConstants.annotationClasses.Tested
import com.example.trackies.isSignedIn.user.buisness.entities.Regularity

@Dao
interface RegularityDAO {

    @Tested
    @Query("SELECT * FROM regularity WHERE dayOfWeek = :currentDayOfWeek")
    suspend fun fetchStatesOfTrackiesForToday(currentDayOfWeek: String): List<Regularity>?

    @Tested
    @Query("SELECT * FROM regularity")
    suspend fun fetchWeeklyRegularity(): List<Regularity>?

    @Tested
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrackieToRegularity(regularity: Regularity)

    @Tested
    @Query("DELETE FROM Regularity WHERE name = :nameOfTrackie")
    suspend fun deleteTrackieFromRegularity(nameOfTrackie: String)

    @Tested
    @Update
    suspend fun markTrackieAsIngested(rowToUpdate: Regularity)

    @Tested
    @Query("UPDATE Regularity SET ingested = 0 WHERE dayOfWeek = :dayOfWeek")
    suspend fun resetRegularity(dayOfWeek: String)


    @Query("DELETE FROM Regularity")
    suspend fun deleteUsersRegularity()
}