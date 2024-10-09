package com.example.trackies.isSignedOut.data

interface AuthenticationService {

    var initialDestination: String

    fun getSignedInUser(): String?

    fun signUpWithEmailAndPassword(
        email: String,
        password: String,
        signUpError: (String) -> Unit,
        authenticationResult: (Boolean) -> Unit
    )

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        signInError: (String) -> Unit,
        authenticatedSuccessfully: (String) -> Unit
    )

    fun getEmailAddress(): String?

    fun signOut(
        onComplete: () -> Unit,
        onFailure: () -> Unit
    )

    fun deleteAccount(
        onComplete: () -> Unit,
        onFailure: (String) -> Unit,
    )

    fun recoverThePassword(
        email: String,
        successfullySentEmail: () -> Unit,
        failedToSendEmail: (String) -> Unit
    )
}