package com.example.trackies.isSignedIn.user.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.globalConstants.annotationClasses.Tested
import com.example.trackies.isSignedIn.user.buisness.entities.License

@Dao
interface LicenseDAO {

//  Checks whether a row containing primary key of value 1 already exists.
//  Used to define whether a user already has an account. The method returns null - account does not exist.
    @Tested
    @Query("SELECT * FROM License WHERE first == 1")
    suspend fun isFirstTimeInTheApp(): License?

//  Creates or replaces a row with primary key of value 1 used to define whether a user is signed in,
//  cannot add more trackies due to maximum limit of a free account.
    @Tested
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createLicense(license: License)

//  Helper method during the process of adding new trackie. Returns license with primary key of value 1 to
//  give information how many Trackies there will be after adding another one.
    @Tested
    @Query("SELECT * FROM License WHERE first == 1")
    suspend fun getLicense(): License?

//  Via changing the value of 'isSignedIn' to true, tells in the next launches
//  of the app that a user is  signed in its room account.
    @Tested
    @Query("UPDATE License SET isSignedIn = 1 WHERE first = 1")
    suspend fun signIn()

//  Via changing the value of 'isSignedIn' to false, tells in the next launches
//  of the app that a user is not signed in its room account.
    @Tested
    @Query("UPDATE License SET isSignedIn = 0 WHERE first = 1")
    suspend fun signOut()

    @Tested
    @Query("UPDATE License SET totalAmountOfTrackies = :totalAmountOfTrackies WHERE first = 1")
    suspend fun increaseTotalAmountOfTrackiesByOne(totalAmountOfTrackies: Int)

    @Tested
    @Query("UPDATE License SET totalAmountOfTrackies = :totalAmountOfTrackies WHERE first = 1")
    suspend fun decreaseTotalAmountOfTrackiesByOne(totalAmountOfTrackies: Int)

    @Tested
    @Query("DELETE FROM License")
    suspend fun deleteUsersLicense()
}