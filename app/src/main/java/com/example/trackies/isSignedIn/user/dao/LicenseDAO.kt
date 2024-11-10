package com.example.trackies.isSignedIn.user.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.trackies.isSignedIn.user.buisness.entities.License

@Dao
interface LicenseDAO {

    @Query("SELECT * FROM License WHERE first == 1")
    suspend fun isFirstTimeInTheApp(): License?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createLicense(license: License)

    @Query("SELECT * FROM License WHERE first == 1")
    suspend fun getLicense(): License?

    @Query("UPDATE License SET isSignedIn = 1 WHERE first == 1")
    suspend fun signInTheUser()

    @Query("UPDATE License SET totalAmountOfTrackies = :totalAmountOfTrackies WHERE first = 1")
    suspend fun increaseTotalAmountOfTrackiesByOne(totalAmountOfTrackies: Int)

    @Query("UPDATE License SET totalAmountOfTrackies = :totalAmountOfTrackies WHERE first = 1")
    suspend fun decreaseTotalAmountOfTrackiesByOne(totalAmountOfTrackies: Int)

    @Query("UPDATE License SET isSignedIn = 0 WHERE first == 1")
    suspend fun signOutTheUser()
}