package com.example.trackies.isSignedOut.data

import android.util.Patterns
import com.example.trackies.isSignedOut.buisness.Destinations
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object FirebaseAuthenticationService: AuthenticationService {

    private val authentication = Firebase.auth

    override var initialDestination: String = Destinations.isSignedOut
        get() {

            return if (getSignedInUser() == null) {
                Destinations.isSignedOut
            } else {
                Destinations.isSignedIn
            }
        }


    override fun getSignedInUser(): String? = authentication.currentUser?.run { this.uid }

    override fun signUpWithEmailAndPassword(
        email: String,
        password: String,
        signUpError: (String) -> Unit,
        authenticationResult: (Boolean) -> Unit
    ) {

        if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signUpError("Invalid email format")
        }

        authentication.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { process ->

                if (process.isSuccessful) {

                    sendEmailToVerifySigningUp { gotSent ->

                        if (gotSent) {
                            authenticationResult(true)
                        } else {
                            authenticationResult(false)
                        }
                    }
                } else {

                    when (val exception = process.exception) {

                        is FirebaseAuthInvalidCredentialsException -> {
                            signUpError("${exception.message}")
                        }

                        is FirebaseAuthUserCollisionException -> {
                            signUpError("$email is already used by another account.")
                        }

                        else -> {
                            signUpError("${exception?.message}")
                        }
                    }
                }
            }
    }

    override fun signInWithEmailAndPassword(
        email: String,
        password: String,
        signInError: (String) -> Unit,
        authenticatedSuccessfully: (String) -> Unit
    ) {

        try {

            authentication.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { process ->

                    if (process.isSuccessful) {

                        val user = authentication.currentUser

                        user?.isEmailVerified?.let { isVerified ->

                            if (isVerified) {
                                authenticatedSuccessfully(user.uid)
                            }
                        }
                    } else {
                        signInError("${process.exception}")
                    }
                }
        } catch (e: Exception) {

            when (e) {

                is java.lang.IllegalArgumentException -> {
                    signInError("the email and/or password is empty")
                }

                is FirebaseAuthInvalidCredentialsException -> {
                    signInError("the email address is badly formatted")
                }

                else -> {}
            }
        }
    }

    override fun getEmailAddress(): String? = authentication.currentUser?.email

    override fun signOut(onComplete: () -> Unit, onFailure: () -> Unit) {

        authentication.signOut()

        if (authentication.currentUser == null) {onComplete()}

        else {onFailure}
    }

    override fun deleteAccount(
        onComplete: () -> Unit,
        onFailure: (String) -> Unit,
    ) {
        authentication.currentUser?.delete()
            ?.addOnCompleteListener { process ->

                if (process.isSuccessful) {
                    onComplete()
                }
            }
            ?.addOnFailureListener { exception ->
                onFailure("$exception")
            }
    }

    override fun recoverThePassword(
        email: String,
        successfullySentEmail: () -> Unit,
        failedToSendEmail: (String) -> Unit
    ) {
        authentication.sendPasswordResetEmail(email)
            .addOnCompleteListener { process ->

                if (process.isSuccessful) {
                    successfullySentEmail()
                } else {
                    failedToSendEmail("${process.exception}")
                }
            }
    }

    private fun sendEmailToVerifySigningUp(verificationEmailGotSent: (Boolean) -> Unit) {

        authentication.currentUser?.sendEmailVerification()
            ?.addOnCompleteListener { process ->

                if (process.isSuccessful) {
                    verificationEmailGotSent(true)
                } else {
                    verificationEmailGotSent(false)
                }
            }
    }
}