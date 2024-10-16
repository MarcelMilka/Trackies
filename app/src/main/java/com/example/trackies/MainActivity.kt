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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.trackies.isSignedIn.ui.changePassword.insertNewPassword
import com.example.trackies.isSignedIn.ui.deleteAccount.confirmDeletionOfTheAccount
import com.example.trackies.isSignedIn.homeScreen.ui.homeScreen
import com.example.trackies.isSignedIn.ui.settings
import com.example.trackies.isSignedIn.ui.deleteAccount.verifyYourIdentityToDeleteAccount
import com.example.trackies.isSignedIn.ui.deleteAccount.yourAccountGotDeleted
import com.example.trackies.isSignedIn.ui.changePassword.verifyYourIdentityToChangePassword
import com.example.trackies.isSignedIn.ui.changePassword.yourPasswordGotChanged
import com.example.trackies.navigation.Destinations
import com.example.trackies.auth.data.AuthenticationService
import com.example.trackies.isSignedIn.homeScreen.viewModel.HomeScreenViewModel
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

                navigation(route = Destinations.IsSignedOut, startDestination = Destinations.WelcomeScreen) {

                    composable(
                        route = Destinations.WelcomeScreen,
                        enterTransition = {EnterTransition.None },
                        exitTransition = { ExitTransition.None }
                    ) {
                        welcomeScreen (
                            onNavigateSignUp = {
                                navigationController.navigate(route = Destinations.SignUpRoute)
                            },
                            onNavigateSignIn = {
                                navigationController.navigate(route = Destinations.SignInRoute)
                            }
                        )
                    }

                    navigation(route = Destinations.SignUpRoute, startDestination = Destinations.SignUp) {

                        composable(
                            route = Destinations.SignUp,
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

                                            if (waitingForAuthentication) {
                                                navigationController.navigate(route = Destinations.Authenticate) {
                                                    popUpTo(route = Destinations.WelcomeScreen) {inclusive = false}
                                                }
                                            }
                                            else {
                                                navigationController.navigate(route = Destinations.Authenticate) {
                                                    popUpTo(route = Destinations.WelcomeScreen) {inclusive = false}
                                                }
                                            }
                                        }
                                    )
                                }
                            )
                        }

                        composable(
                            route = Destinations.Authenticate,
                            enterTransition = {EnterTransition.None },
                            exitTransition = { ExitTransition.None }
                        ) {
                            authenticate {
                                navigationController.navigate(route = Destinations.SignInRoute) {
                                    popUpTo(route = Destinations.WelcomeScreen) {inclusive = false}
                                }
                            }
                        }

                        composable(
                            route = Destinations.AuthenticationEmailWasNotSent,
                            enterTransition = {EnterTransition.None },
                            exitTransition = { ExitTransition.None }
                        ) {
                            authenticationEmailWasNotSent {
                                navigationController.navigate(route = Destinations.WelcomeScreen) {
                                    popUpTo(route = Destinations.WelcomeScreen) {inclusive = false}
                                }
                            }
                        }
                    }

                    navigation(route = Destinations.SignInRoute, startDestination = Destinations.SignIn) {

                        composable(
                            route = Destinations.SignIn,
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

                                            navigationController.navigate(Destinations.IsSignedIn) {
                                                popUpTo(Destinations.IsSignedOut) { inclusive = true }
                                            }
                                        }
                                    )
                                },
                                onRecoverPassword = {
                                    navigationController.navigate(route = Destinations.RecoverPassword)
                                }
                            )
                        }

                        composable(
                            route = Destinations.RecoverPassword,
                            enterTransition = {EnterTransition.None },
                            exitTransition = { ExitTransition.None }
                        ) {
                            recoverPassword { email ->
                                authenticationService.recoverThePassword(
                                    email = email,
                                    successfullySentEmail = {
                                        navigationController.navigate(route = Destinations.Information) {
                                            popUpTo(route = Destinations.SignIn) {inclusive = false}}
                                    },
                                    failedToSendEmail = {
                                        Log.d("Halla!", it)
                                    }
                                )
                            }
                        }

                        composable(
                            route = Destinations.Information,
                            enterTransition = {EnterTransition.None },
                            exitTransition = { ExitTransition.None }
                        ) {
                            information{
                                navigationController.navigate(route = Destinations.SignIn) {
                                    popUpTo(route = Destinations.SignIn) {inclusive = true}
                                }
                            }
                        }
                    }
                }

                navigation(route = Destinations.IsSignedIn, startDestination = Destinations.HomeScreen) {

                    composable(
                        route = Destinations.HomeScreen,
                        enterTransition = {EnterTransition.None},
                        exitTransition = {ExitTransition.None}
                    ) {

                        val homeScreenViewModel = hiltViewModel<HomeScreenViewModel>()

                        homeScreen(
                            onOpenSettings = {navigationController.navigate(route = Destinations.Settings)}
                        )
                    }

                    composable(
                        route = Destinations.Settings,
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
                                navigationController.navigate(route = Destinations.ChangePassword)
                            },
                            onDeleteAccount = {
                                navigationController.navigate(route = Destinations.DeleteAccount)
                            },
                            onChangeLanguage = {},
                            onReportInAppBug = {},
                            onDisplayInfoAboutThisApp = {},
                            onLogout = {
                                authenticationService.signOut(
                                    onComplete = {
                                        navigationController.navigate(route = Destinations.IsSignedOut) {
                                            popUpTo(route = Destinations.IsSignedIn) {inclusive = false}
                                        }
                                    },

                                    onFailure = {}
                                )
                            }
                        )
                    }

                    navigation(route = Destinations.DeleteAccount, startDestination = Destinations.ConfirmDeletionOfTheAccount) {

                        dialog(route = Destinations.ConfirmDeletionOfTheAccount) {

                            confirmDeletionOfTheAccount(
                                onConfirm = {
                                    navigationController.navigateUp()
                                    navigationController.navigate(route = Destinations.VerifyYourIdentity)
                                },
                                onDecline = {
                                    navigationController.navigateUp()
                                }
                            )
                        }

                        dialog(route = Destinations.VerifyYourIdentity) {

                            var anErrorOccurred by remember { mutableStateOf(false) }
                            var errorMessage by remember { mutableStateOf("") }

                            verifyYourIdentityToDeleteAccount(

                                anErrorOccurred = anErrorOccurred,
                                errorMessage = errorMessage,

                                onConfirm = {

                                    authenticationService.deleteAccount(
                                        password = it,
                                        onComplete = {
                                            navigationController.navigate(route = Destinations.YourAccountGotDeleted) {
                                                popUpTo(route = Destinations.YourAccountGotDeleted) {inclusive = true}
                                            }
                                        },
                                        onFailure = {
                                            errorMessage = it
                                            anErrorOccurred = true
                                        }
                                    )
                                },

                                onDecline = {
                                    navigationController.navigate(Destinations.Settings) {
                                        popUpTo(Destinations.Settings) {inclusive = true}
                                    }
                                },

                                onHideNotification = {
                                    anErrorOccurred = false
                                }
                            )
                        }

                        composable(route = Destinations.YourAccountGotDeleted) {

                            yourAccountGotDeleted {
                                navigationController.navigate(route = Destinations.IsSignedOut) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        }
                    }

                    navigation(route = Destinations.ChangePassword, startDestination = Destinations.VerifyYourIdentityToChangePassword) {

                        dialog(route = Destinations.VerifyYourIdentityToChangePassword) {

                            var passwordIsIncorrect by remember { mutableStateOf(false) }

                            verifyYourIdentityToChangePassword(

                                passwordIsIncorrect = passwordIsIncorrect,

                                onConfirm = {
                                    authenticationService.authenticateViaPassword(
                                        password = it,
                                        onComplete = {
                                            navigationController.navigate(route = Destinations.InsertNewPassword)
                                        },
                                        onFailure = {
                                            passwordIsIncorrect = true
                                        }
                                    )
                                },

                                onDecline = {
                                    navigationController.navigate(route = Destinations.Settings) {
                                        popUpTo(route = Destinations.Settings) {inclusive = true}
                                    }
                                },

                                onHideNotification = {
                                    passwordIsIncorrect = false
                                }
                            )
                        }

                        dialog(route = Destinations.InsertNewPassword) {

                            var passwordIsIncorrect by remember { mutableStateOf(false) }

                            insertNewPassword(
                                passwordIsIncorrect = passwordIsIncorrect,
                                onConfirm = {
                                    authenticationService.changeThePassword(
                                        newPassword = it,
                                        onComplete = {
                                            navigationController.navigate(route = Destinations.YourPasswordGotChanged)
                                        },
                                        onFailure = {}
                                    )
                                },
                                onDecline = {
                                    navigationController.navigate(route = Destinations.Settings) {
                                        popUpTo(route = Destinations.Settings) {inclusive = true}
                                    }
                                },
                                onHideNotification = {
                                    passwordIsIncorrect = false
                                }
                            )
                        }

                        composable(
                            route = Destinations.YourPasswordGotChanged,
                            enterTransition = {EnterTransition.None},
                            exitTransition = {ExitTransition.None}
                        ) {

                            yourPasswordGotChanged {
                                navigationController.navigate(route = Destinations.Settings) {
                                    popUpTo(route = Destinations.Settings) {inclusive = true}
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}