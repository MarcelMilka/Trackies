package com.example.trackies.auth.providerOfAuthenticationMethod

import com.example.trackies.auth.buisness.AuthenticationMethod
import com.example.trackies.auth.data.AuthenticationService
import com.example.trackies.di.FirebaseAuthenticator
import com.example.trackies.di.RoomAuthenticator
import javax.inject.Inject

class AuthenticationMethodProvider @Inject constructor(
    @FirebaseAuthenticator private val firebaseAuthService: AuthenticationService,
    @RoomAuthenticator private val roomAuthService: AuthenticationService
) {

    private var currentMode: AuthenticationMethod = AuthenticationMethod.Firebase

    fun setAuthenticationMethod(mode: AuthenticationMethod) {
        currentMode = mode
    }

    fun getAuthenticationService(): AuthenticationService {

        return when (currentMode) {

            AuthenticationMethod.Firebase -> {
                firebaseAuthService
            }

            AuthenticationMethod.Room -> {
                roomAuthService
            }
        }
    }

    fun getAuthenticationMethod(): AuthenticationMethod =
        currentMode

    fun forceInjection() {


    }
}