package com.example.trackies.auth.providerOfAuthenticationMethod

import com.example.globalConstants.Destinations
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

    fun getInitialDestination(): String {

        val firebaseInitialDestination = firebaseAuthService.initialDestination // ( Destinations.IsSignedOut / Destinations.IsSignedIn )

        val roomInitialDestination = roomAuthService.initialDestination // ( true / false )

        return if (firebaseInitialDestination == Destinations.IsSignedIn) {

            Destinations.IsSignedIn
        }

        else {

            if (roomInitialDestination == Destinations.IsSignedIn) {

                setAuthenticationMethod(mode = AuthenticationMethod.Room)
                Destinations.IsSignedIn
            }

            else {

                Destinations.IsSignedOut
            }
        }
    }

}