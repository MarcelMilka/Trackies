package com.example.trackies.isSignedIn.user.data

interface UserRepository {

    fun firstTimeInTheApp(anErrorOccurred: () -> Unit)
}