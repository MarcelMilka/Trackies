package com.example.trackies.isSignedIn.user.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.trackies.isSignedIn.user.buisness.entities.Trackie

@Dao
interface TrackiesDAO {

    @Query( """
    SELECT * FROM Trackies WHERE
    CASE :currentDayOfWeek
        WHEN 'monday' THEN monday
        WHEN 'tuesday' THEN tuesday
        WHEN 'wednesday' THEN wednesday
        WHEN 'thursday' THEN thursday
        WHEN 'friday' THEN friday
        WHEN 'saturday' THEN saturday
        WHEN 'sunday' THEN sunday
    END = 1
    """)
    suspend fun getTrackiesForToday(currentDayOfWeek: String): List<Trackie>?

    @Insert
    suspend fun addNewTrackie(trackie: Trackie)

    @Query("DELETE FROM Trackies WHERE name = :nameOfTrackie")
    suspend fun deleteTrackie(nameOfTrackie: String)
}