package com.example.trackies.isSignedIn.user.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.trackies.isSignedIn.user.buisness.entities.License

@Dao
interface LicenseDAO {

//  The method below is responsible for checking information if the user already has a created local database.
    @Query("SELECT * FROM License WHERE first == 1")
    suspend fun isFirstTimeInTheApp(): License?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createLicense(license: License)

    @Query("SELECT * FROM License WHERE first == 1")
    suspend fun getLicense(): License?

    @Query("UPDATE License SET isSignedIn = 1 WHERE first = 1")
    suspend fun signIn()

    @Query("UPDATE License SET isSignedIn = 0 WHERE first = 1")
    suspend fun signOut()

    @Query("UPDATE License SET totalAmountOfTrackies = :totalAmountOfTrackies WHERE first = 1")
    suspend fun increaseTotalAmountOfTrackiesByOne(totalAmountOfTrackies: Int)

    @Query("UPDATE License SET totalAmountOfTrackies = :totalAmountOfTrackies WHERE first = 1")
    suspend fun decreaseTotalAmountOfTrackiesByOne(totalAmountOfTrackies: Int)

    @Query("DELETE FROM License")
    fun deleteLicense()

    @Query("SELECT * FROM License")
    suspend fun testGetAllLicenses(): List<License>
}