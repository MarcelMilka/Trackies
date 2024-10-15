package com.example.trackies.auth.data

import com.example.trackies.isSignedOut.presentation.ui.signIn.signIn.SignInErrorsToReturn
import com.example.trackies.isSignedOut.presentation.ui.signUp.signUp.SignUpErrors

interface AuthenticationService {

    var initialDestination: String

    fun getSignedInUser(): String?

    fun signUpWithEmailAndPassword(
        email: String,
        password: String,
        signUpError: (SignUpErrors) -> Unit,
        verificationEmailGotSent: (Boolean) -> Unit
    )

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        onFailedToSignIn: (SignInErrorsToReturn) -> Unit,
        onSucceededToSignIn: (String) -> Unit
    )

    fun getEmailAddress(): String?

    fun signOut(
        onComplete: () -> Unit,
        onFailure: () -> Unit
    )

    fun deleteAccount(
        password: String,
        onComplete: () -> Unit,
        onFailure: (String) -> Unit,
    )

    fun recoverThePassword(
        email: String,
        successfullySentEmail: () -> Unit,
        failedToSendEmail: (String) -> Unit
    )

    fun authenticateViaPassword(
        password: String,
        onComplete: () -> Unit,
        onFailure: (String) -> Unit
    )

    fun changeThePassword(
        newPassword: String,
        onComplete: () -> Unit,
        onFailure: (String) -> Unit
    )
}