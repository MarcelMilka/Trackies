package com.example.trackies

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.trackies.isSignedIn.changePassword.insertNewPassword
import com.example.trackies.isSignedIn.deleteAccount.confirmDeletionOfTheAccount
import com.example.trackies.isSignedIn.homeScreen
import com.example.trackies.isSignedIn.settings
import com.example.trackies.isSignedIn.deleteAccount.verifyYourIdentityToDeleteAccount
import com.example.trackies.isSignedIn.deleteAccount.yourAccountGotDeleted
import com.example.trackies.isSignedIn.changePassword.verifyYourIdentityToChangePassword
import com.example.trackies.isSignedIn.changePassword.yourPasswordGotChanged
import com.example.trackies.navigation.Destinations
import com.example.trackies.auth.data.AuthenticationService
import com.example.trackies.isSignedOut.presentation.ui.signIn.signIn.SignInHints
import com.example.trackies.isSignedOut.presentation.ui.signUp.authenticate
import com.example.trackies.isSignedOut.presentation.ui.signIn.information
import com.example.trackies.isSignedOut.presentation.ui.signIn.recoverPassword
import com.example.trackies.isSignedOut.presentation.ui.signIn.signIn.SignInErrors
import com.example.trackies.isSignedOut.presentation.ui.signIn.signIn.signIn
import com.example.trackies.isSignedOut.presentation.ui.signUp.authenticationEmailWasNotSent
import com.example.trackies.isSignedOut.presentation.ui.signUp.signUp.SignUpErrors
import com.example.trackies.isSignedOut.presentation.ui.signUp.signUp.SignUpHints
import com.example.trackies.isSignedOut.presentation.ui.signUp.signUp.signUp
import com.example.trackies.isSignedOut.presentation.ui.welcomeScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authenticationService: AuthenticationService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            val navigationController = rememberNavController()

            NavHost(navController = navigationController, startDestination = authenticationService.initialDestination) {

                navigation(route = "SignedOut", startDestination = "WelcomeScreen") {

                    composable(
                        route = "WelcomeScreen",
                        enterTransition = {EnterTransition.None },
                        exitTransition = { ExitTransition.None }
                    ) {
                        welcomeScreen { navigationController.navigate(it) }
                    }

                    navigation(route = "SignUpRoute", startDestination = "SignUp") {

                        composable(
                            route = "SignUp",
                            enterTransition = {EnterTransition.None },
                            exitTransition = { ExitTransition.None }
                        ) {

                            var anErrorOccurred by remember { mutableStateOf(false) }
                            var errorDescription by remember { mutableStateOf("") }

                            signUp(
                                anErrorOccurred = anErrorOccurred,
                                errorDescription = errorDescription,
                                onHideError = {
                                    anErrorOccurred = false
                                },
                                onSignUp = { credentials ->

                                    authenticationService.signUpWithEmailAndPassword(
                                        email = credentials.email,
                                        password = credentials.password,
                                        signUpError = {

                                            when(it) {
                                                SignUpErrors.InvalidEmailFormat -> {
                                                    anErrorOccurred = true
                                                    errorDescription = SignUpHints.invalidEmailFormat
                                                }

                                                SignUpErrors.EmailIsAlreadyUsed -> {
                                                    anErrorOccurred = true
                                                    errorDescription = SignUpHints.emailIsAlreadyUsed
                                                }

                                                SignUpErrors.ExternalError -> {
                                                    anErrorOccurred = true
                                                    errorDescription = SignUpHints.externalError
                                                }
                                            }
                                        },
                                        verificationEmailGotSent = { waitingForAuthentication ->

                                            if (!waitingForAuthentication) {
                                                navigationController.navigate("Authenticate") {
                                                    popUpTo("WelcomeScreen") {inclusive = false}
                                                }
                                            }
                                            else {
                                                navigationController.navigate("Authenticate") {
                                                    popUpTo("WelcomeScreen") {inclusive = false}
                                                }
                                            }
                                        }
                                    )
                                }
                            )
                        }

                        composable(
                            route = "Authenticate",
                            enterTransition = {EnterTransition.None },
                            exitTransition = { ExitTransition.None }
                        ) {
                            authenticate {
                                navigationController.navigate("SignInRoute") {
                                    popUpTo(route = "WelcomeScreen") {inclusive = false}
                                }
                            }
                        }

                        composable(
                            route = "AuthenticationEmailWasNotSent",
                            enterTransition = {EnterTransition.None },
                            exitTransition = { ExitTransition.None }
                        ) {
                            authenticationEmailWasNotSent {
                                navigationController.navigate("SignInRoute") {
                                    popUpTo(route = "WelcomeScreen") {inclusive = false}
                                }
                            }
                        }
                    }

                    navigation(route = "SignInRoute", startDestination = "SignIn") {

                        composable(
                            route = "SignIn",
                            enterTransition = {EnterTransition.None },
                            exitTransition = { ExitTransition.None }
                        ) {

                            var emailRelatedError by remember { mutableStateOf(false) }
                            var emailRelatedErrorDescription by remember { mutableStateOf("") }

                            var passwordRelatedError by remember { mutableStateOf(false) }
                            var passwordRelatedErrorDescription by remember { mutableStateOf("") }

                            signIn(
                                emailRelatedError = emailRelatedError,
                                emailRelatedErrorDescription = emailRelatedErrorDescription,

                                passwordRelatedError = passwordRelatedError,
                                passwordRelatedErrorDescription = passwordRelatedErrorDescription,

                                onHideError = {

                                    when(it) {

                                        SignInErrors.EmailRelatedError -> {
                                            emailRelatedError = false
                                        }

                                        SignInErrors.PasswordRelatedError -> {
                                            passwordRelatedError = false
                                        }

                                        SignInErrors.ExternalError -> {}
                                    }
                                },

                                onSignIn = {
                                    authenticationService.signInWithEmailAndPassword(

                                        email = it.email,
                                        password = it.password,
                                        onFailedToSignIn = {

                                            emailRelatedError = false
                                            emailRelatedErrorDescription = ""

                                            passwordRelatedError = false
                                            passwordRelatedErrorDescription = ""

                                            if (it.email == SignInErrors.EmailRelatedError) {
                                                emailRelatedError = true
                                                emailRelatedErrorDescription = SignInHints.theEmailAddressIsBadlyFormated
                                            }

                                            if (it.email == SignInErrors.ExternalError) {
                                                emailRelatedError = true
                                                emailRelatedErrorDescription = SignInHints.externalError
                                            }

                                            if (it.password == SignInErrors.PasswordRelatedError) {
                                                passwordRelatedError = true
                                                passwordRelatedErrorDescription = SignInHints.thePasswordIsIncorrect
                                            }

                                            if (it.email == SignInErrors.ExternalError) {
                                                passwordRelatedError = true
                                                passwordRelatedErrorDescription = SignInHints.externalError
                                            }
                                        },

                                        onSucceededToSignIn = { uid ->

                                            navigationController.navigate("SignedIn") {
                                                popUpTo(Destinations.isSignedOut) { inclusive = true }
                                            }
                                        }
                                    )
                                },
                                onRecoverPassword = { navigationController.navigate("RecoverPassword") }
                            )
                        }

                        composable(
                            route = "RecoverPassword",
                            enterTransition = {EnterTransition.None },
                            exitTransition = { ExitTransition.None }
                        ) {
                            recoverPassword { email ->
                                authenticationService.recoverThePassword(
                                    email = email,
                                    successfullySentEmail = {
                                        navigationController.navigate("Information") {
                                            popUpTo(route = "SignIn") {inclusive = false}}
                                    },
                                    failedToSendEmail = {
                                        Log.d("Halla!", it)
                                    }
                                )
                            }
                        }

                        composable(
                            route = "Information",
                            enterTransition = {EnterTransition.None },
                            exitTransition = { ExitTransition.None }
                        ) {
                            information{ navigationController.navigate("SignIn") {
                                popUpTo(route = "SignIn") {inclusive = true}
                                }
                            }
                        }
                    }
                }

                navigation(route = Destinations.isSignedIn, startDestination = "HomeScreen") {

                    composable(
                        route = "HomeScreen",
                        enterTransition = {EnterTransition.None},
                        exitTransition = {ExitTransition.None}
                    ) {

                        homeScreen(
                            onOpenSettings = {navigationController.navigate(route = Destinations.settings)}
                        )
                    }

                    composable(
                        route = Destinations.settings,
                        enterTransition = {EnterTransition.None},
                        exitTransition = {ExitTransition.None}
                    ) {

                        settings(
                            usersEmail = authenticationService.getEmailAddress() ?: "An error occurred.",
                            onReturnHomeScreen = {
                                navigationController.navigateUp()
                            },
                            onChangeEmail = {},
                            onChangePassword = {
                                navigationController.navigate(route = Destinations.changePassword)
                            },
                            onDeleteAccount = {
                                navigationController.navigate(route = Destinations.deleteAccount)
                            },
                            onChangeLanguage = {},

                            onReportInAppBug = {},

                            onDisplayInfoAboutThisApp = {},

                            onLogout = {
                                authenticationService.signOut(
                                    onComplete = {
                                        navigationController.navigate(route = Destinations.isSignedOut) {
                                            popUpTo(route = Destinations.isSignedIn) {inclusive = false}
                                        }
                                    },

                                    onFailure = {}
                                )
                            }
                        )
                    }

                    navigation(route = Destinations.deleteAccount, startDestination = Destinations.confirmDeletionOfTheAccount) {

                        dialog(route = Destinations.confirmDeletionOfTheAccount) {

                            confirmDeletionOfTheAccount(
                                onConfirm = {
                                    navigationController.navigateUp()
                                    navigationController.navigate("VerifyYourIdentity")
                                },
                                onDecline = {
                                    navigationController.navigateUp()
                                }
                            )
                        }

                        dialog(route = Destinations.verifyYourIdentity) {

                            var anErrorOccurred by remember { mutableStateOf(false) }
                            var errorMessage by remember { mutableStateOf("") }

                            verifyYourIdentityToDeleteAccount(

                                anErrorOccurred = anErrorOccurred,
                                errorMessage = errorMessage,

                                onConfirm = {

                                    authenticationService.deleteAccount(
                                        password = it,
                                        onComplete = {
                                            navigationController.navigate(route = "YourAccountGotDeleted") {
                                                popUpTo(route = "YourAccountGotDeleted") {inclusive = true}
                                            }
                                        },
                                        onFailure = {
                                            Log.d("Halla!", it)
                                            errorMessage = it
                                            anErrorOccurred = true
                                        }
                                    )
                                },

                                onDecline = {
                                    navigationController.navigate(Destinations.settings) {
                                        popUpTo(Destinations.settings) {inclusive = true}
                                    }
                                },

                                onHideNotification = {
                                    anErrorOccurred = false
                                }
                            )
                        }

                        composable(route = "YourAccountGotDeleted") {

                            yourAccountGotDeleted {
                                navigationController.navigate(route = Destinations.isSignedOut) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        }
                    }

                    navigation(route = Destinations.changePassword, startDestination = Destinations.verifyYourIdentityToChangePassword) {

                        dialog(route = Destinations.verifyYourIdentityToChangePassword) {

                            var passwordIsIncorrect by remember { mutableStateOf(false) }

                            verifyYourIdentityToChangePassword(

                                passwordIsIncorrect = passwordIsIncorrect,

                                onConfirm = {
                                    authenticationService.authenticateViaPassword(
                                        password = it,
                                        onComplete = {
                                            navigationController.navigate(route = Destinations.insertNewPassword)
                                        },
                                        onFailure = {
                                            passwordIsIncorrect = true
                                        }
                                    )
                                },

                                onDecline = {
                                    navigationController.navigate(route = Destinations.settings) {
                                        popUpTo(route = Destinations.settings) {inclusive = true}
                                    }
                                },

                                onHideNotification = {
                                    passwordIsIncorrect = false
                                }
                            )
                        }

                        dialog(route = Destinations.insertNewPassword) {

                            var passwordIsIncorrect by remember { mutableStateOf(false) }

                            insertNewPassword(
                                passwordIsIncorrect = passwordIsIncorrect,
                                onConfirm = {
                                    authenticationService.changeThePassword(
                                        newPassword = it,
                                        onComplete = {
                                            navigationController.navigate(route = Destinations.yourPasswordGotChanged)
                                        },
                                        onFailure = {}
                                    )
                                },
                                onDecline = {
                                    navigationController.navigate(route = Destinations.settings) {
                                        popUpTo(route = Destinations.settings) {inclusive = true}
                                    }
                                },
                                onHideNotification = {
                                    passwordIsIncorrect = false
                                }
                            )
                        }

                        composable(
                            route = Destinations.yourPasswordGotChanged,
                            enterTransition = {EnterTransition.None},
                            exitTransition = {ExitTransition.None}
                        ) {

                            yourPasswordGotChanged {
                                navigationController.navigate(route = Destinations.settings) {
                                    popUpTo(route = Destinations.settings) {inclusive = true}
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}