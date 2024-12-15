package com.example.trackies.auth.data

import com.example.globalConstants.Destinations
import com.example.globalConstants.annotationClasses.Tested
import com.example.trackies.aRoom.db.RoomDatabase
import com.example.trackies.di.RoomAuthenticator
import com.example.trackies.isSignedOut.presentation.ui.signIn.signIn.SignInErrorsToReturn
import com.example.trackies.isSignedOut.presentation.ui.signUp.signUp.SignUpErrors
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@Tested
@RoomAuthenticator
class RoomAuthenticationService @Inject constructor(
    val roomDatabase: RoomDatabase
): AuthenticationService {

    @Tested
    override var initialDestination: String = Destinations.IsSignedOut
        get() {

            return if (this.getSignedInUser() == null || this.getSignedInUser() == "false") {

                Destinations.IsSignedOut
            }

            else {
                Destinations.IsSignedIn
            }
        }

    @Tested
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

    @Tested
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
    }

    @Tested
    override fun getEmailAddress(): String? = "Local database user"

    @Tested
    override fun signOut(
        onComplete: () -> Unit,
        onFailure: () -> Unit
    ) {

        runBlocking {
            roomDatabase
                .licenseDAO()
                .signOut()
        }

        onComplete()
    }

    @Tested
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


    override fun signUpWithEmailAndPassword(
        email: String,
        password: String,
        signUpError: (SignUpErrors) -> Unit,
        verificationEmailGotSent: (Boolean) -> Unit
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
    ) {}

}