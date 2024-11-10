package com.example.trackies.auth.data

import com.example.globalConstants.Destinations
import com.example.trackies.aRoom.db.RoomDatabase
import com.example.trackies.isSignedOut.presentation.ui.signIn.signIn.SignInErrorsToReturn
import com.example.trackies.isSignedOut.presentation.ui.signUp.signUp.SignUpErrors
import javax.inject.Inject

class RoomAuthenticationService @Inject constructor(
    var authenticator: RoomDatabase
) : AuthenticationService {

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

        return "false"
    }


    override fun signOut(onComplete: () -> Unit, onFailure: () -> Unit) {}

    override fun deleteAccount(
        password: String,
        onComplete: () -> Unit,
        onFailure: (String) -> Unit
    ) {}





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
    ) {}

    override fun getEmailAddress(): String? = "Local database user"

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