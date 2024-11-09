package com.example.trackies.isSignedIn.user.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface TestHelperDAO {

    @Query("DELETE FROM License")
    suspend fun deleteLicenseTable()

    @Query("DELETE FROM Trackies")
    suspend fun deleteTrackiesTable()

    @Query("DELETE FROM Regularity")
    suspend fun deleteRegularityTable()
}