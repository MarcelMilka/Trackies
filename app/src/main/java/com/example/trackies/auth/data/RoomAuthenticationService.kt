package com.example.trackies.auth.data

import android.util.Patterns
import com.example.globalConstants.Destinations
import com.example.trackies.isSignedIn.settings.dialogs.deleteAccount.DeleteAccountHints
import com.example.trackies.isSignedOut.presentation.ui.signIn.signIn.SignInErrors
import com.example.trackies.isSignedOut.presentation.ui.signIn.signIn.SignInErrorsToReturn
import com.example.trackies.isSignedOut.presentation.ui.signUp.signUp.SignUpErrors
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object RoomAuthenticationService: AuthenticationService {

    override var initialDestination: String
        get() = "Local database user"
        set(value) {}

    override fun getSignedInUser(): String? = "Local database user"

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