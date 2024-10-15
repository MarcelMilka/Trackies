package com.example.trackies.navigation

object Destinations {

    val IsSignedOut = "SignedOut"

        val WelcomeScren = "WelcomeScreen"

//      Sub-navigation to sign up
        val SignUpRoute = "SignUpRoute"
            val SignUp = "SignUp"



//      Sub-navigation to sign up


    val IsSignedIn = "SignedIn"

        val Settings = "Settings"

        val deleteAccount = "DeleteAccount"
            val confirmDeletionOfTheAccount = "ConfirmDeletionOfTheAccount"
            val verifyYourIdentity = "VerifyYourIdentity"

    val changePassword = "ChangePassword"
        val verifyYourIdentityToChangePassword = "VerifyYourIdentityToChangePassword"
        val insertNewPassword = "InsertNewPassword"
        val yourPasswordGotChanged = "YourPasswordGotChanged"
}