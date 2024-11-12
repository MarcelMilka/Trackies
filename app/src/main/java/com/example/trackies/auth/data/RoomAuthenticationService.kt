package com.example.trackies.auth.data

import android.util.Log
import com.example.globalConstants.Destinations
import com.example.trackies.aRoom.db.RoomDatabase
import com.example.trackies.di.RoomAuthenticator
import com.example.trackies.isSignedOut.presentation.ui.signIn.signIn.SignInErrorsToReturn
import com.example.trackies.isSignedOut.presentation.ui.signUp.signUp.SignUpErrors
import javax.inject.Inject

@RoomAuthenticator
class RoomAuthenticationService @Inject constructor(
    val roomDatabase: RoomDatabase
): AuthenticationService {

    init {
        Log.d("Magnetic Man", "$this")
    }

    override var initialDestination: String = Destinations.IsSignedOut
        get() {

            return if (

                this.getSignedInUser() == null ||
                this.getSignedInUser() == "false"
            ) {
                Destinations.IsSignedOut
            }

            else {
                Destinations.IsSignedIn
            }
        }

    override fun getSignedInUser(): String? {

        // TODO: implement code which fetches LicenseModel entity
        // TODO: if LicenseModel license is active, then the user is signed in, else is signed out
        return "false"
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

        Log.d("Philodendron", email)
    }

    override fun getEmailAddress(): String? = "Local database user"

    override fun signOut(onComplete: () -> Unit, onFailure: () -> Unit) {}

    override fun deleteAccount(
        password: String,
        onComplete: () -> Unit,
        onFailure: (String) -> Unit
    ) {}

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
    ) {
        TODO("Not yet implemented")
    }

}