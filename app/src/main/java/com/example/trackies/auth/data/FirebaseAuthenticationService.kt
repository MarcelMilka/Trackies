package com.example.trackies.auth.data

import android.util.Patterns
import com.example.trackies.isSignedIn.settings.dialogs.deleteAccount.DeleteAccountHints
import com.example.globalConstants.Destinations
import com.example.trackies.di.FirebaseAuthenticator
import com.example.trackies.isSignedOut.presentation.ui.signIn.signIn.SignInErrorsToReturn
import com.example.trackies.isSignedOut.presentation.ui.signIn.signIn.SignInErrors
import com.example.trackies.isSignedOut.presentation.ui.signUp.signUp.SignUpErrors
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@FirebaseAuthenticator
object FirebaseAuthenticationService: AuthenticationService {

    private var authentication = Firebase.auth

    override var initialDestination: String = Destinations.IsSignedOut
        get() {

            return if (getSignedInUser() == null) {
                Destinations.IsSignedOut
            }
            else {
                Destinations.IsSignedIn
            }
        }


    override fun getSignedInUser(): String? = authentication.currentUser?.run {
        this.uid
    }

    override fun signUpWithEmailAndPassword(
        email: String,
        password: String,
        signUpError: (SignUpErrors) -> Unit,
        verificationEmailGotSent: (Boolean) -> Unit
    ) {

        if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signUpError(SignUpErrors.InvalidEmailFormat)
        }

        authentication.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { process ->

                if (process.isSuccessful) {

                    sendEmailToVerifySigningUp { gotSent ->

                        if (gotSent) {
                            verificationEmailGotSent(true)
                        } else {
                            verificationEmailGotSent(false)
                        }
                    }
                }

                else {

                    when (process.exception) {

                        is FirebaseAuthInvalidCredentialsException -> {
                            signUpError(SignUpErrors.InvalidEmailFormat)
                        }

                        is FirebaseAuthUserCollisionException -> {
                            signUpError(SignUpErrors.EmailIsAlreadyUsed)
                        }

                        else -> {
                            signUpError(SignUpErrors.ExternalError)
                        }
                    }
                }
            }
    }

    override fun signInWithEmailAndPassword(
        email: String,
        password: String,
        onFailedToSignIn: (SignInErrorsToReturn) -> Unit,
        onSucceededToSignIn: (String) -> Unit
    ) {

        authentication.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { process ->

                if (process.isSuccessful) {

                    val user = authentication.currentUser

                    user?.isEmailVerified?.let { isVerified ->

                        if (isVerified) {
                            onSucceededToSignIn(user.uid)
                        }
                    }
                }

                else {

                    val exceptionMessage = process.exception?.message

                    val emailError: SignInErrors? = when {
                        exceptionMessage?.contains("The email address is badly formatted", true) == true -> {
                            SignInErrors.EmailRelatedError
                        }

                        else -> null
                    }

                    val passwordError: SignInErrors? = when {
                        exceptionMessage?.contains("The supplied auth credential is incorrect, malformed or has expired", true) == true -> {
                            SignInErrors.PasswordRelatedError
                        }

                        else -> null
                    }

                    if (emailError == null && passwordError == null) {
                        onFailedToSignIn(

                            SignInErrorsToReturn(
                                email = SignInErrors.ExternalError,
                                password = SignInErrors.ExternalError
                            )
                        )
                    }

                    else {
                        onFailedToSignIn(
                            SignInErrorsToReturn(
                                email = emailError,
                                password = passwordError
                            )
                        )
                    }
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
        password: String,
        onComplete: () -> Unit,
        onFailure: (String) -> Unit,
    ) {

        val user = authentication.currentUser

        // If the user is logged in
        if (user != null) {

            val credential = EmailAuthProvider.getCredential(user.email!!, password)

            user.reauthenticate(credential)

                .addOnCompleteListener { reauthentication ->

                    if (reauthentication.isSuccessful) {
                        onComplete()
                        user.delete()
                            .addOnCompleteListener { deleteTask ->
                                if (deleteTask.isSuccessful) {
                                    onComplete()
                                }
                                else {
                                    onFailure("Account deletion failed: ${deleteTask.exception?.message}")
                                }
                            }
                            .addOnFailureListener { exception ->
                                onFailure("Account deletion error: $exception")
                            }
                    }

                    else {
                        onFailure("Reauthentication failed: ${reauthentication.exception?.message}")
                    }
                }

                .addOnFailureListener { exception ->

                    when (exception) {

                        is FirebaseAuthInvalidCredentialsException -> {
                            onFailure(DeleteAccountHints.invalidCredentialsException)
                        }

                        is FirebaseTooManyRequestsException -> {
                            onFailure(DeleteAccountHints.tooManyRequestsException)
                        }

                        else -> {
                            onFailure("An external error occurred, try again later.")
                        }
                    }
                }
        }

        else {
            onFailure("No authenticated user found")
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

    override fun authenticateViaPassword(
        password: String,
        onComplete: () -> Unit,
        onFailure: (String) -> Unit
    ) {

        val user = authentication.currentUser

        if (user != null) {

            val credential = EmailAuthProvider.getCredential(user.email!!, password)

            user.reauthenticate(credential)

                .addOnCompleteListener { reauthentication ->

                    if (reauthentication.isSuccessful) {
                        onComplete()
                    }

                    else {
                        onFailure("Reauthentication failed: ${reauthentication.exception?.message}")
                    }
                }

                .addOnFailureListener { exception ->
                    onFailure("Reauthentication error: $exception")
                }
        }

        else {
            onFailure("No authenticated user found")
        }
    }

    override fun changeThePassword(
        newPassword: String,
        onComplete: () -> Unit,
        onFailure: (String) -> Unit
    ) {

        val user = authentication.currentUser

        if (user != null) {

            user.updatePassword(newPassword)
                .addOnSuccessListener {
                    onComplete()
                }
                .addOnFailureListener {
                    onFailure("Failed to update password, because ${it.cause}")
                }
        }

        else {
            onFailure("No authenticated user found")
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