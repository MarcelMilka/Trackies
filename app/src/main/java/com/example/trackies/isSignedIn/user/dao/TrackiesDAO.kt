package com.example.trackies.isSignedIn.user.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.trackies.isSignedIn.user.buisness.entities.Trackie

@Dao
interface TrackiesDAO {

//  This method fetches all rows from the table "Trackies" which have value true
//  assigned to a particular column.
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


//  This method is simply responsible for adding new trackie.
//  Although UI does not allow to add a trackie named after one already existing,
//  OnConflictStrategy was added for the sake something goes wrong.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewTrackie(trackie: Trackie)


//  This method finds a row with name equal to the value of the parameter, deleting a Trackie
//  from the database "Trackies".
    @Query("DELETE FROM Trackies WHERE name = :nameOfTrackie")
    suspend fun deleteTrackie(nameOfTrackie: String)


//  This method gets all rows from the column "Trackies".
    @Query("SELECT * FROM Trackies")
    suspend fun getAllTrackies(): List<Trackie>


    @Query("SELECT name FROM Trackies")
    suspend fun getNamesOfAllTrackies(): List<String>

    @Query("DELETE FROM Trackies")
    suspend fun deleteUsersTrackies()
}