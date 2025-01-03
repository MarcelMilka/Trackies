package com.example.trackies.auth.authenticationMethodProvider

import com.example.globalConstants.Destinations
import com.example.globalConstants.annotationClasses.Tested
import com.example.trackies.auth.buisness.AuthenticationMethod
import com.example.trackies.auth.data.AuthenticationService
import com.example.trackies.di.FirebaseAuthenticator
import com.example.trackies.di.RoomAuthenticator
import javax.inject.Inject

@Tested
class AuthenticationMethodProvider @Inject constructor(
    @FirebaseAuthenticator private val firebaseAuthService: AuthenticationService,
    @RoomAuthenticator private val roomAuthService: AuthenticationService
) {

    private var currentMode: AuthenticationMethod = AuthenticationMethod.Firebase

    @Tested
    fun setAuthenticationMethod(mode: AuthenticationMethod) {
        currentMode = mode
    }

    @Tested
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

    @Tested
    fun getAuthenticationMethod(): AuthenticationMethod = currentMode

    @Tested
    fun getInitialDestination(): String {

        val firebaseInitialDestination = firebaseAuthService.initialDestination // ( Destinations.IsSignedOut / Destinations.IsSignedIn )

        val roomInitialDestination = roomAuthService.initialDestination // ( Destinations.IsSignedOut / Destinations.IsSignedIn )

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

    @Tested
    fun getFirebaseUniqueID(): String? = firebaseAuthService.getSignedInUser()
}