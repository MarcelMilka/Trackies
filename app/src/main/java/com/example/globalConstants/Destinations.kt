package com.example.globalConstants

object Destinations {

//  Route "SignedOut"
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

//  Route "SignedIn"
    val IsSignedIn = "SignedIn"

        val HomeScreen = "HomeScreen"


        val AddNewTrackieRoute = "AddNewTrackieRoute"
            val AddNewTrackie = "AddNewTrackie"
            val ScheduleIngestionTime = "ScheduleIngestionTime"

        val DisplayDetailedTrackieRoute = "DisplayDetailedTrackieRoute"
            val DetailedTrackie = "DetailedTrackie"
            val ConfirmDeletionOfTheTrackie = "ConfirmDeletionOfTheTrackie"

        val AllTrackies = "AllTrackies"

        val Settings = "Settings"

//          Sub-route "DeleteAccount"
            val DeleteAccount = "DeleteAccount"
                val ConfirmDeletionOfTheAccount = "ConfirmDeletionOfTheAccount"
                val VerifyYourIdentity = "VerifyYourIdentity"
                val YourAccountGotDeleted = "YourAccountGotDeleted"

//          Sub-route "ChangePassword"
            val ChangePassword = "ChangePassword"
                val VerifyYourIdentityToChangePassword = "VerifyYourIdentityToChangePassword"
                val InsertNewPassword = "InsertNewPassword"
                val YourPasswordGotChanged = "YourPasswordGotChanged"
}