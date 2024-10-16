package com.example.trackies.isSignedIn.user.data

import android.util.Log
import javax.inject.Inject

class FirebaseUserRepository @Inject constructor(
    var uniqueIdentifier: String
): UserRepository {

    init {
        Log.d("Halla!", "FirebaseUserRepository, ${this.uniqueIdentifier}")
    }

    override fun uid(): String? = uniqueIdentifier
}