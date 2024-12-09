package com.example.globalConstants

object Destinations {

    val IsSignedOut = "SignedOut"

        val WelcomeScreen = "WelcomeScreen"

        val GuestModeInformation = "GuestModeInformation"

        val SignUpRoute = "SignUpRoute"
            val SignUp = "SignUp"
            val Authenticate = "Authenticate"
            val AuthenticationEmailWasNotSent = "AuthenticationEmailWasNotSent"

        val SignInRoute = "SignInRoute"
            val SignIn = "SignIn"
            val RecoverPassword = "RecoverPassword"
            val Information = "Information"



    val IsSignedIn = "SignedIn"

        val HomeScreen = "HomeScreen"

        val AddNewTrackieRoute = "AddNewTrackieRoute"
            val AddNewTrackie = "AddNewTrackie"
            val ScheduleIngestionTime = "ScheduleIngestionTime"
            val TrackiesPremiumDialog = "TrackiesPremiumDialog"

        val DisplayDetailedTrackieRoute = "DisplayDetailedTrackieRoute"
            val DetailedTrackie = "DetailedTrackie"
            val ConfirmDeletionOfTheTrackie = "ConfirmDeletionOfTheTrackie"

        val AllTrackies = "AllTrackies"

        val Settings = "Settings"

            val DeleteAccount = "DeleteAccount"
                val ConfirmDeletionOfTheAccount = "ConfirmDeletionOfTheAccount"
                val VerifyYourIdentity = "VerifyYourIdentity"
                val YourAccountGotDeleted = "YourAccountGotDeleted"

            val ChangePassword = "ChangePassword"
                val VerifyYourIdentityToChangePassword = "VerifyYourIdentityToChangePassword"
                val InsertNewPassword = "InsertNewPassword"
                val YourPasswordGotChanged = "YourPasswordGotChanged"
}