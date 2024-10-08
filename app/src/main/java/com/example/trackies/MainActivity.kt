package com.example.trackies

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.trackies.isSignedOut.data.AuthenticationService
import com.example.trackies.isSignedOut.presentation.ui.signUp.authenticate
import com.example.trackies.isSignedOut.presentation.ui.signIn.information
import com.example.trackies.isSignedOut.presentation.ui.signIn.recoverPassword
import com.example.trackies.isSignedOut.presentation.ui.signIn.signIn
import com.example.trackies.isSignedOut.presentation.ui.signUp.signUp
import com.example.trackies.isSignedOut.presentation.ui.welcomeScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            val navigationController = rememberNavController()

            NavHost(navController = navigationController, startDestination = "SignedOut") {

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
                            signUp { navigationController.navigate("Authenticate") {popUpTo("WelcomeScreen") {inclusive = false}}}
                        }

                        composable(
                            route = "Authenticate",
                            enterTransition = {EnterTransition.None },
                            exitTransition = { ExitTransition.None }
                        ) {
                            authenticate { navigationController.navigate("SignInRoute") {popUpTo(route = "WelcomeScreen") {inclusive = false}}}
                        }
                    }

                    navigation(route = "SignInRoute", startDestination = "SignIn") {

                        composable(
                            route = "SignIn",
                            enterTransition = {EnterTransition.None },
                            exitTransition = { ExitTransition.None }
                        ) {

                            signIn(
                                onSignIn = {},
                                onRecoverPassword = { navigationController.navigate("RecoverPassword") }
                            )
                        }

                        composable(
                            route = "RecoverPassword",
                            enterTransition = {EnterTransition.None },
                            exitTransition = { ExitTransition.None }
                        ) { recoverPassword {
                            navigationController.navigate("Information"){popUpTo(route = "SignIn") {inclusive = false}}
                            }
                        }

                        composable(
                            route = "Information",
                            enterTransition = {EnterTransition.None },
                            exitTransition = { ExitTransition.None }
                        ) {information{navigationController.navigate("SignIn") {popUpTo(route = "SignIn") {inclusive = false}}}}
                    }
                }
            }
        }
    }
}