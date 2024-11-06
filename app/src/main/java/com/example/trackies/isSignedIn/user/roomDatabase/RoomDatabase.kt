package com.example.trackies.isSignedIn.user.roomDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.trackies.isSignedIn.user.buisness.entities.License
import com.example.trackies.isSignedIn.user.buisness.entities.Regularity
import com.example.trackies.isSignedIn.user.buisness.entities.Trackie
import com.example.trackies.isSignedIn.user.dao.LicenseDAO
import com.example.trackies.isSignedIn.user.dao.RegularityDAO
import com.example.trackies.isSignedIn.user.dao.TrackiesDAO

@Database(
    entities = [License::class, Regularity::class, Trackie::class],
    version = 1
)
abstract class RoomDatabase: RoomDatabase() {

    abstract fun licenseDAO(): LicenseDAO

    abstract fun regularityDAO(): RegularityDAO

    abstract fun trackiesDAO(): TrackiesDAO
}