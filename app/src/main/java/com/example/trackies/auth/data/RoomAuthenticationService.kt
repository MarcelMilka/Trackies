package com.example.trackies.auth.data

import android.util.Log
import com.example.globalConstants.Destinations
import com.example.trackies.aRoom.db.RoomDatabase
import com.example.trackies.di.RoomAuthenticator
import com.example.trackies.isSignedOut.presentation.ui.signIn.signIn.SignInErrorsToReturn
import com.example.trackies.isSignedOut.presentation.ui.signUp.signUp.SignUpErrors
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@RoomAuthenticator
class RoomAuthenticationService @Inject constructor(
    val roomDatabase: RoomDatabase
): AuthenticationService {

    init {
        Log.d("Magnetic Man", "$this provides authentication service repository")
    }

    override var initialDestination: String = Destinations.IsSignedOut
        get() {

            return if (this.getSignedInUser() == null || this.getSignedInUser() == "false") {

                Destinations.IsSignedOut
            }

            else {
                Destinations.IsSignedIn
            }
        }

    override fun getSignedInUser(): String? = runBlocking {

        val license =
            roomDatabase
                .licenseDAO()
                .getLicense()

        if (license != null) {

            if (license.isSignedIn) {
                "true"
            }

            else {
                "false"
            }
        }

        else {
            null
        }
    }

    override fun signUpWithEmailAndPassword(
        email: String,
        password: String,
        signUpError: (SignUpErrors) -> Unit,
        verificationEmailGotSent: (Boolean) -> Unit
    ) {}

    override fun signInWithEmailAndPassword(
        email: String,
        password: String,
        onFailedToSignIn: (SignInErrorsToReturn) -> Unit,
        onSucceededToSignIn: (String) -> Unit
    ) {

        runBlocking {

            roomDatabase
                .licenseDAO()
                .signIn()
        }

        onSucceededToSignIn("succeeded to sign in the user")
    }

    override fun getEmailAddress(): String? = "Local database user"

    override fun signOut(onComplete: () -> Unit, onFailure: () -> Unit) {

        runBlocking {
            roomDatabase
                .licenseDAO()
                .signOut()
        }

        onComplete()
    }

    override fun deleteAccount(
        password: String,
        onComplete: () -> Unit,
        onFailure: (String) -> Unit
    ) {

        runBlocking {

            try {

                roomDatabase
                    .licenseDAO()
                    .deleteUsersLicense()

                roomDatabase
                    .regularityDAO()
                    .deleteUsersRegularity()

                roomDatabase
                    .trackiesDAO()
                    .deleteUsersTrackies()

                onComplete()
            }

            catch (e: Exception) {

                onFailure("$e")
            }
        }
    }

    override fun recoverThePassword(
        email: String,
        successfullySentEmail: () -> Unit,
        failedToSendEmail: (String) -> Unit
    ) {}

    override fun authenticateViaPassword(
        password: String,
        onComplete: () -> Unit,
        onFailure: (String) -> Unit
    ) {}

    override fun changeThePassword(
        newPassword: String,
        onComplete: () -> Unit,
        onFailure: (String) -> Unit
    ) {}

}