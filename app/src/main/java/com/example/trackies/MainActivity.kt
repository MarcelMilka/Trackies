package com.example.trackies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.trackies.isSignedOut.authenticate.Authenticate
import com.example.trackies.isSignedOut.signIn.SignIn
import com.example.trackies.isSignedOut.signUp.SignUp
import com.example.trackies.isSignedOut.welcomeScreen.WelcomeScreen

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
                        WelcomeScreen { navigationController.navigate(it) }
                    }

                    navigation(route = "SignUpRoute", startDestination = "SignUp") {

                        composable(
                            route = "SignUp",
                            enterTransition = {EnterTransition.None },
                            exitTransition = { ExitTransition.None }
                        ) {
                            SignUp { navigationController.navigate("Authenticate") {popUpTo("WelcomeScreen") {inclusive = false}}}
                        }

                        composable(
                            route = "Authenticate",
                            enterTransition = {EnterTransition.None },
                            exitTransition = { ExitTransition.None }
                        ) {
                            Authenticate { navigationController.navigate("SignInRoute") {popUpTo(route = "WelcomeScreen") {inclusive = false}}}
                        }
                    }

                    navigation(route = "SignInRoute", startDestination = "SignIn") {

                        composable(
                            route = "SignIn",
                            enterTransition = {EnterTransition.None },
                            exitTransition = { ExitTransition.None }
                        ) { SignIn {  } }
                    }
                }
            }
        }
    }
}